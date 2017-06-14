package com.dfa.vinatrip.domains.main.share.make_share;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.share.Share;
import com.dfa.vinatrip.domains.search.SearchActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.TripGuyUtils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PICK_IMAGE1;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PICK_IMAGE2;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PICK_IMAGE3;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PICK_IMAGE4;
import static com.dfa.vinatrip.utils.TripGuyUtils.REQUEST_PROVINCE;
import static com.dfa.vinatrip.utils.TripGuyUtils.exifToDegrees;

@EActivity(R.layout.activity_make_share)
public class MakeShareActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Bean
    DataService dataService;

    @NotEmpty
    @ViewById(R.id.activity_make_share_tv_province)
    TextView tvProvince;

    @ViewById(R.id.activity_make_share_spn_type)
    Spinner spnType;

    @NotEmpty
    @ViewById(R.id.activity_make_share_et_destination)
    EditText etDestination;

    @NotEmpty
    @ViewById(R.id.activity_make_share_et_content)
    EditText etContent;

    @NotEmpty
    @ViewById(R.id.activity_make_share_et_name)
    EditText etName;

    @ViewById(R.id.activity_make_share_progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.activity_make_share_iv_photo1)
    ImageView ivPhoto1;

    @ViewById(R.id.activity_make_share_iv_photo2)
    ImageView ivPhoto2;

    @ViewById(R.id.activity_make_share_iv_photo3)
    ImageView ivPhoto3;

    @ViewById(R.id.activity_make_share_iv_photo4)
    ImageView ivPhoto4;

    private DatabaseReference databaseReference;
    private Validator validator;
    private Share share;
    private List<Bitmap> adjustedBitmapList;
    private Uri uri;
    private Bitmap adjustedBitmap;

    @AfterViews
    void init() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.type_array, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        validator = new Validator(this);
        validator.setValidationListener(this);
        share = new Share();
        adjustedBitmapList = new ArrayList<>();
    }

    @Click(R.id.activity_make_share_ll_province)
    void onLlProvinceClick() {
        SearchActivity_.intent(this).fromView("MakeShareActivity").startForResult(REQUEST_PROVINCE);
    }

    @OnActivityResult(REQUEST_PROVINCE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            tvProvince.setText(data.getStringExtra("nameProvince"));
        }
    }

    @Click(R.id.activity_make_share_btn_done)
    void onBtnDoneClick() {
        validator.validate();
    }

    @Click(R.id.activity_make_share_btn_cancel)
    void onBtnCancelClick() {
        super.onBackPressed();
    }

    @Click(R.id.activity_make_share_iv_photo1)
    void onIvPhoto1Click() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE1);


        }
    }

    @Click(R.id.activity_make_share_iv_photo2)
    void onIvPhoto2Click() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE2);
        }
    }

    @Click(R.id.activity_make_share_iv_photo3)
    void onIvPhoto3Click() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE3);
        }
    }

    @Click(R.id.activity_make_share_iv_photo4)
    void onIvPhoto4Click() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE4);
        }
    }

    @OnActivityResult(REQUEST_PICK_IMAGE1)
    void onResultImage1(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();
        }
    }

    @Override
    public void onValidationSucceeded() {
        progressBar.setVisibility(View.VISIBLE);
        share.setId(String.valueOf(System.currentTimeMillis()));
        share.setContent(etContent.getText().toString());
        share.setDestination(etDestination.getText().toString());
        share.setName(etName.getText().toString());
        share.setUid(dataService.getCurrentUser().getUid());
        share.setAvatar(dataService.getCurrentUser().getAvatar());
        share.setEmail(dataService.getCurrentUser().getEmail());
        share.setNickname(dataService.getCurrentUser().getNickname());

        databaseReference.child("Share").child(tvProvince.getText().toString()).child(share.getId())
                         .setValue(share, new DatabaseReference.CompletionListener() {
                             @Override
                             public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                 if (databaseError != null) {
                                     Toasty.error(MakeShareActivity.this,
                                                  "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                                     progressBar.setVisibility(View.GONE);
                                 } else {
                                     Toasty.success(MakeShareActivity.this, "Chia sẻ của bạn đã được tạo",
                                                    Toast.LENGTH_SHORT).show();
                                     finish();
                                 }
                             }
                         });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = "Không được để trống";
            if (view instanceof TextView) {
                ((TextView) view).setError(message);
            } else {
                ((EditText) view).setError(message);
            }
        }
    }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, 10);
            }
        }
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this,
                                                  Manifest.permission.ACCESS_FINE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this,
                                                  Manifest.permission.ACCESS_COARSE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED;
    }
}
