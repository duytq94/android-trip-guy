package com.dfa.vinatrip.MainFunction.Me.UserDetail.UpdateProfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.wasabeef.blurry.Blurry;

@EFragment(R.layout.fragment_update_user_profile)
public class UpdateUserProfileFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_update_user_profile_tv_percent)
    TextView tvPercent;

    @ViewById(R.id.fragment_update_user_profile_et_nickname)
    EditText etNickname;

    @ViewById(R.id.fragment_update_user_profile_et_city)
    EditText etCity;

    @ViewById(R.id.fragment_update_user_profile_et_introduce_your_self)
    EditText etIntroduceYourSelf;

    @ViewById(R.id.fragment_update_user_profile_iv_avatar)
    ImageView ivAvatar;

    @ViewById(R.id.fragment_update_user_profile_iv_blur_avatar)
    ImageView ivBlurAvatar;

    @ViewById(R.id.fragment_update_user_profile_progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.fragment_update_user_profile_spn_sex)
    Spinner spnSex;

    @ViewById(R.id.fragment_update_user_profile_tv_birthday)
    TextView tvBirthday;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private UserProfile currentUser;
    private Calendar calendar;
    private Uri uri;
    private int PICK_IMAGE = 1;

    @Click
    void fragment_update_user_profile_btn_done() {
        // Check if user update info but don't update avatar
        if (uri == null) {
            UserProfile newUserProfile = new UserProfile(etNickname.getText().toString(),
                    currentUser.getAvatar(),
                    etIntroduceYourSelf.getText().toString(),
                    etCity.getText().toString(),
                    tvBirthday.getText().toString(),
                    currentUser.getUid(),
                    spnSex.getSelectedItem().toString(),
                    currentUser.getEmail());

            databaseReference.child("UserProfile").child(currentUser.getUid()).setValue(newUserProfile);
            dataService.setCurrentUser(newUserProfile);
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            uploadUserAvatar();
        }
    }

    @Click
    void fragment_update_user_profile_btn_cancel() {
        getActivity().finish();
    }

    @Click
    void fragment_update_user_profile_iv_change_avatar() {
        Intent intentToLibrary = new Intent();
        intentToLibrary.setType("image/*");
        intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentToLibrary, PICK_IMAGE);
    }

    @Click
    void fragment_update_user_profile_ll_birthday() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener
                = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvBirthday.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                listener, year, month, day);
        dialog.setTitle("Chọn ngày sinh");
        dialog.show();
    }

    @AfterViews
    void onCreateView() {
        setContentViews();
    }

    public void setContentViews() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sex_array,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);

        currentUser = dataService.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendar = Calendar.getInstance();

        if (currentUser != null) {
            if (!currentUser.getAvatar().equals("")) {
                Picasso.with(getActivity())
                        .load(currentUser.getAvatar())
                        .into(target);
            }
            etNickname.setText(currentUser.getNickname());
            etCity.setText(currentUser.getCity());
            if (!currentUser.getBirthday().equals("")) {
                tvBirthday.setText(currentUser.getBirthday());
            } else {
                setCurrentDayForView();
            }
            etIntroduceYourSelf.setText(currentUser.getIntroduceYourSelf());
            switch (currentUser.getSex()) {
                case "Nam":
                    spnSex.setSelection(0);
                    break;
                case "Nữ":
                    spnSex.setSelection(1);
                    break;
                default:
                    spnSex.setSelection(2);
                    break;
            }
        } else {
            setCurrentDayForView();
        }
    }

    public void setCurrentDayForView() {
        // Set current day for tvBirthday
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvBirthday.setText(strDate);
    }

    public void uploadUserAvatar() {
        progressBar.setVisibility(View.VISIBLE);
        tvPercent.setVisibility(View.VISIBLE);

        Bitmap bmPhoto;
        try {
            bmPhoto = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmPhoto = scaleDown(bmPhoto, 300, true);
            bmPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayPhoto = baos.toByteArray();

            // Get the path and name photo be upload
            storageReference = firebaseStorage
                    .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                    .child("AvatarProfileUser")
                    .child(currentUser.getUid() + ".jpg");

            UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
            uploadTask
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            tvPercent.setText(progress + "%");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressBar.setVisibility(View.GONE);
                            tvPercent.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),
                                    "Cập nhật thất bại\nHÃy thử lại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String linkAvatar;
                            if (downloadUrl == null) linkAvatar = currentUser.getAvatar();
                            else {
                                linkAvatar = downloadUrl.toString();
                            }
                            UserProfile newUserProfile =
                                    new UserProfile(etNickname.getText().toString(),
                                            linkAvatar,
                                            etIntroduceYourSelf.getText().toString(),
                                            etCity.getText().toString(),
                                            tvBirthday.getText().toString(),
                                            currentUser.getUid(),
                                            spnSex.getSelectedItem().toString(),
                                            currentUser.getEmail());
                            databaseReference.child("UserProfile").child(currentUser.getUid())
                                    .setValue(newUserProfile);
                            dataService.setCurrentUser(newUserProfile);
                            progressBar.setVisibility(View.GONE);
                            tvPercent.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            // Show avatar be chosen for user first, not upload yet
            Picasso.with(getActivity())
                    .load(uri)
                    .into(target);
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmap = scaleDown(bitmap, 300, true);
            Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                    .from(bitmap).into(ivBlurAvatar);
            ivAvatar.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
