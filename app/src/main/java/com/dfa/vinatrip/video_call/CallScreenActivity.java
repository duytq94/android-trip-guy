package com.dfa.vinatrip.video_call;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallScreenActivity extends BaseVideoCallActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId;
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private boolean mVideoViewsAdded = false;
    private UserInPlan remoteUser;

    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    private LinearLayout llSwitchCamera;
    private VideoController videoController;
    private RippleBackground rippleBackground;
    private RelativeLayout rlContent;

    private class UpdateCallDurationTask extends TimerTask {
        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(() -> updateCallDuration());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);

        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        LinearLayout endCallButton = (LinearLayout) findViewById(R.id.activity_call_screen_btn_end_call);
        llSwitchCamera = (LinearLayout) findViewById(R.id.activity_call_screen_btn_switch_camera);

        CircleImageView civRemoteAvatar = (CircleImageView) findViewById(R.id.activity_call_screen_civ_remote_user_avatar);

        rippleBackground = (RippleBackground) findViewById(R.id.activity_call_screen_rb);
        rippleBackground.setVisibility(View.VISIBLE);
        rippleBackground.startRippleAnimation();

        rlContent = (RelativeLayout) findViewById(R.id.activity_call_screen_rl_content);
        rlContent.setVisibility(View.GONE);

        llSwitchCamera.setEnabled(false);

        endCallButton.setOnClickListener(v -> endCall());
        llSwitchCamera.setOnClickListener(v -> switchCamera());

        remoteUser = (UserInPlan) getIntent().getSerializableExtra("remoteUser");
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        if (remoteUser != null) {
            Picasso.with(this).load(remoteUser.getAvatar()).into(civRemoteAvatar);
        }

        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

        updateUI();
    }

    //method to update video feeds in the UI
    public void updateUI() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallerName.setText(call.getRemoteUserId());
            mCallState.setText("ĐANG KẾT NỐI");
            if (call.getState() == CallState.ESTABLISHED) {
                //when the call is established, addVideoViews configures the video to  be shown
                addVideoViews();
            }
        }
    }

    //stop the timer when call is ended
    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
        removeVideoViews();
    }

    //start the timer for the call duration here
    @Override
    public void onStart() {
        super.onStart();

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(true);
        }

        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
        updateUI();
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    //method to end the call
    public void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    public void switchCamera() {
        videoController.toggleCaptureDevicePosition();
    }

    public String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    //method to update live duration of the call
    public void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    //method which sets up the video feeds from the server to the UI of the activity
    private void addVideoViews() {
        if (mVideoViewsAdded || getSinchServiceInterface() == null) {
            return; //early
        }

        videoController = getSinchServiceInterface().getVideoController();
        if (videoController != null) {
            rippleBackground.startRippleAnimation();
            rippleBackground.setVisibility(View.GONE);

            rlContent.setVisibility(View.VISIBLE);

            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.addView(videoController.getLocalView());

            RelativeLayout view = (RelativeLayout) findViewById(R.id.remoteVideo);
            view.addView(videoController.getRemoteView());
            mVideoViewsAdded = true;
        }
        llSwitchCamera.setEnabled(true);
    }

    //removes video feeds from the app once the call is terminated
    public void removeVideoViews() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            RelativeLayout view = (RelativeLayout) findViewById(R.id.remoteVideo);
            view.removeView(vc.getRemoteView());

            RelativeLayout localView = (RelativeLayout) findViewById(R.id.localVideo);
            localView.removeView(vc.getLocalView());
            mVideoViewsAdded = false;
        }
    }

    public class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            Toast.makeText(CallScreenActivity.this,
                    String.format("Kết thúc cuộc gọi sau %ss.\nLý do: %s", call.getDetails().getDuration(), call.getDetails().getEndCause()),
                    Toast.LENGTH_LONG).show();

            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText("ĐANG TRÒ CHUYỆN");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            audioController.enableSpeaker();

            mCallStart = System.currentTimeMillis();
            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            Log.d(TAG, "Video track added");
            addVideoViews();
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }
}