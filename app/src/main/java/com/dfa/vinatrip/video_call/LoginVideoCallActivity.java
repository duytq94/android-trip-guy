package com.dfa.vinatrip.video_call;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Build;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.models.response.user.User;
import com.sinch.android.rtc.SinchError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_login_video_call)
public class LoginVideoCallActivity extends BaseVideoCallActivity implements SinchService.StartFailedListener {

    @Extra
    protected Plan plan;
    @Extra
    protected User currentUser;

    private ProgressDialog mSpinner;

    @AfterViews
    public void init() {
        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }
    }

    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        loginClicked();
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void loginClicked() {
        if (!currentUser.getEmail().isEmpty()) {
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(currentUser.getEmail());
                showSpinner();
            } else {
                openPlaceCallActivity();
            }
        }
    }

    //Once the connection is made to the Sinch Service, It takes you to the next activity where you enter the name of the user to whom the call is to be placed
    private void openPlaceCallActivity() {
        PlaceCallActivity_.intent(this).currentUser(currentUser).plan(plan).start();
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}