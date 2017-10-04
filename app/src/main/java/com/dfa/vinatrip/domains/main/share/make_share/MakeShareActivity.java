package com.dfa.vinatrip.domains.main.share.make_share;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.share.Share;
import com.dfa.vinatrip.domains.main.share.make_share.image_multi_select.BitmapHelper;
import com.dfa.vinatrip.domains.main.share.make_share.image_multi_select.SelectImageActivity_;
import com.dfa.vinatrip.domains.search.SearchActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AppUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PROVINCE;

@EActivity(R.layout.activity_make_share)
public class MakeShareActivity extends AppCompatActivity
        implements Validator.ValidationListener, BitmapHelper.callBackBmHelper {

    public static final int SELECT_IMAGE_REQUEST = 989;

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

    @NotEmpty
    @ViewById(R.id.activity_make_share_et_address)
    EditText etAddress;

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

    @ViewById(R.id.activity_make_share_ll_root)
    LinearLayout llRoot;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Validator validator;
    private Share share;
    private List<Bitmap> adjustedBitmapList;
    private Bitmap adjustedBitmap;
    private int count;

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
        imageClick();
    }

    @Click(R.id.activity_make_share_iv_photo2)
    void onIvPhoto2Click() {
        imageClick();
    }

    @Click(R.id.activity_make_share_iv_photo3)
    void onIvPhoto3Click() {
        imageClick();
    }

    @Click(R.id.activity_make_share_iv_photo4)
    void onIvPhoto4Click() {
        imageClick();
    }

    public void imageClick() {
        askPermission();
        if (checkPermission()) {
            SelectImageActivity_.intent(this).startForResult(SELECT_IMAGE_REQUEST);
        }
    }

    @OnActivityResult(SELECT_IMAGE_REQUEST)
    void selected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            String[] imageSelect = data.getStringArrayExtra("imageSelectURL");
            BitmapHelper.getArrayBitmap(this, imageSelect);
        }
    }

    @Override
    public void getBitmapSuccess(Bitmap[] arrayBitmap) {
        switch (arrayBitmap.length) {
            case 1:
                ivPhoto1.setImageBitmap(arrayBitmap[0]);
                adjustedBitmapList.add(arrayBitmap[0]);
                break;
            case 2:
                ivPhoto1.setImageBitmap(arrayBitmap[0]);
                ivPhoto2.setImageBitmap(arrayBitmap[1]);
                adjustedBitmapList.add(arrayBitmap[0]);
                adjustedBitmapList.add(arrayBitmap[1]);
                break;
            case 3:
                ivPhoto1.setImageBitmap(arrayBitmap[0]);
                ivPhoto2.setImageBitmap(arrayBitmap[1]);
                ivPhoto3.setImageBitmap(arrayBitmap[2]);
                adjustedBitmapList.add(arrayBitmap[0]);
                adjustedBitmapList.add(arrayBitmap[1]);
                adjustedBitmapList.add(arrayBitmap[2]);
                break;
            case 4:
                ivPhoto1.setImageBitmap(arrayBitmap[0]);
                ivPhoto2.setImageBitmap(arrayBitmap[1]);
                ivPhoto3.setImageBitmap(arrayBitmap[2]);
                ivPhoto4.setImageBitmap(arrayBitmap[3]);
                adjustedBitmapList.add(arrayBitmap[0]);
                adjustedBitmapList.add(arrayBitmap[1]);
                adjustedBitmapList.add(arrayBitmap[2]);
                adjustedBitmapList.add(arrayBitmap[3]);
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        progressBar.setVisibility(View.VISIBLE);
        AppUtil.setEnableAllViews(llRoot, false);

        share.setId(String.valueOf(System.currentTimeMillis()));
        share.setContent(etContent.getText().toString());
        share.setDestination(etDestination.getText().toString());
        share.setName(etName.getText().toString());
        share.setAddress(etAddress.getText().toString());
        share.setProvince(tvProvince.getText().toString());
        share.setType(spnType.getSelectedItem().toString());
        share.setUid(dataService.getCurrentUser().getUid());
        share.setAvatar(dataService.getCurrentUser().getAvatar());
        share.setEmail(dataService.getCurrentUser().getEmail());
        share.setNickname(dataService.getCurrentUser().getNickname());

        count = 0;

        if (adjustedBitmapList.size() > 0) {
            // Resize bitmap
            for (int i = 0; i < adjustedBitmapList.size(); i++) {
                adjustedBitmapList.set(i, AppUtil.scaleDown(adjustedBitmapList.get(i), 500, true));
            }

            for (int i = 0; i < adjustedBitmapList.size(); i++) {
                adjustedBitmap = adjustedBitmapList.get(i);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArrayPhoto = baos.toByteArray();

                // Set the path and name photo be upload
                storageReference = FirebaseStorage
                        .getInstance()
                        .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                        .child("PhotoUserShare")
                        .child(dataService.getCurrentUser().getUid() + i + ".jpg");

                UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
                final int finalI = i;
                uploadTask
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                AppUtil.setEnableAllViews(llRoot, true);
                                Toasty.error(MakeShareActivity.this,
                                             "Upload hinh" + finalI + "không thành công\nBạn vui lòng thử lại",
                                             Toast.LENGTH_SHORT).show();
                                count++;
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                if (downloadUrl != null) {
                                    switch (finalI) {
                                        case 0:
                                            share.setPhoto1(downloadUrl.toString());
                                            break;
                                        case 1:
                                            share.setPhoto2(downloadUrl.toString());
                                            break;
                                        case 2:
                                            share.setPhoto3(downloadUrl.toString());
                                            break;
                                        case 3:
                                            share.setPhoto4(downloadUrl.toString());
                                            break;
                                    }
                                    count++;
                                    if (count == 4) {
                                        databaseReference
                                                .child("Share")
                                                .child(share.getId())
                                                .setValue(share, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError,
                                                                           DatabaseReference databaseReference) {
                                                        if (databaseError != null) {
                                                            Toasty.error(MakeShareActivity.this,
                                                                         "Lỗi đường truyền, bạn hãy gửi lại!",
                                                                         Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        } else {
                                                            Toasty.success(MakeShareActivity.this,
                                                                           "Chia sẻ của bạn đã được tạo",
                                                                           Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        } else {
            databaseReference
                    .child("Share")
                    .child(share.getId())
                    .setValue(share, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toasty.error(MakeShareActivity.this,
                                             "Lỗi đường truyền, bạn hãy gửi lại!",
                                             Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toasty.success(MakeShareActivity.this,
                                               "Chia sẻ của bạn đã được tạo",
                                               Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
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
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
               PackageManager.PERMISSION_GRANTED &&
               ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
               PackageManager.PERMISSION_GRANTED;
    }
}
