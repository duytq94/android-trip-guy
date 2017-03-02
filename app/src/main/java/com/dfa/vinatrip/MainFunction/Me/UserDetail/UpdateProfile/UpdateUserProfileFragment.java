package com.dfa.vinatrip.MainFunction.Me.UserDetail.UpdateProfile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jp.wasabeef.blurry.Blurry;

public class UpdateUserProfileFragment extends Fragment {

    private ImageView ivAvatar, ivBlurAvatar, ivChangeAvatar;
    private EditText etNickname, etCity, etIntroduceYourSelf;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Spinner spnSex;
    private Button btnDone, btnCancel;
    private ProgressBar progressBar;
    private LinearLayout llBirthday;
    private TextView tvBirthday;
    private UserProfile userProfile;
    private Calendar calendar;
    private Uri uri;
    private int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user_profile, container, false);

        findViewByIds(view);
        setContentViews();
        onClickListener();

        return view;
    }

    public void findViewByIds(View view) {
        etNickname = (EditText) view.findViewById(R.id.fragment_update_user_profile_et_nickname);
        etCity = (EditText) view.findViewById(R.id.fragment_update_user_profile_et_city);
        etIntroduceYourSelf = (EditText) view.findViewById(R.id.fragment_update_user_profile_et_introduce_your_self);
        ivAvatar = (ImageView) view.findViewById(R.id.fragment_update_user_profile_iv_avatar);
        ivBlurAvatar = (ImageView) view.findViewById(R.id.fragment_update_user_profile_iv_blur_avatar);
        ivChangeAvatar = (ImageView) view.findViewById(R.id.fragment_update_user_profile_iv_change_avatar);
        btnDone = (Button) view.findViewById(R.id.fragment_update_user_profile_btn_done);
        btnCancel = (Button) view.findViewById(R.id.fragment_update_user_profile_btn_cancel);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_update_user_profile_progressBar);
        spnSex = (Spinner) view.findViewById(R.id.fragment_update_user_profile_spn_sex);
        llBirthday = (LinearLayout) view.findViewById(R.id.fragment_update_user_profile_ll_birthday);
        tvBirthday = (TextView) view.findViewById(R.id.fragment_update_user_profile_tv_birthday);
    }

    public void setContentViews() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sex_array,
                R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        calendar = Calendar.getInstance();

        // Get UserProfile from UserProfileDetailActivity to show it to views
        userProfile = (UserProfile) getArguments().getSerializable("UserProfile");
        if (userProfile != null) {
            if (!userProfile.getAvatar().equals("")) {
                Picasso.with(getActivity())
                        .load(userProfile.getAvatar())
                        .into(target);
            }
            etNickname.setText(userProfile.getNickname());
            etCity.setText(userProfile.getCity());
            if (!userProfile.getBirthday().equals("")) {
                tvBirthday.setText(userProfile.getBirthday());
            } else {
                setCurrentDayForView();
            }
            etIntroduceYourSelf.setText(userProfile.getIntroduceYourSelf());
            switch (userProfile.getSex()) {
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

    public void onClickListener() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user update info but don't update avatar
                if (uri == null) {
                    UserProfile newUserProfile = new UserProfile(etNickname.getText().toString(),
                            userProfile.getAvatar(),
                            etIntroduceYourSelf.getText().toString(),
                            etCity.getText().toString(),
                            tvBirthday.getText().toString(),
                            firebaseUser.getUid(),
                            spnSex.getSelectedItem().toString(),
                            userProfile.getEmail());
                    databaseReference.child("UserProfile").child(firebaseUser.getUid())
                            .setValue(newUserProfile);

                    Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    uploadUserAvatarToFirebase();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ivChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLibrary = new Intent();
                intentToLibrary.setType("image/*");
                intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentToLibrary, PICK_IMAGE);
            }
        });

        llBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        spnSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setCurrentDayForView() {
        // Set current day for tvBirthday
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvBirthday.setText(strDate);
    }

    public void uploadUserAvatarToFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        Bitmap bmPhoto;
        try {
            bmPhoto = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayPhoto = baos.toByteArray();

            // Get the path and name photo be upload
            storageReference = firebaseStorage
                    .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                    .child("AvatarProfileUser")
                    .child(firebaseUser.getUid() + ".jpg");

            UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
            uploadTask
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            String linkAvatar;
                            if (downloadUrl == null) linkAvatar = userProfile.getAvatar();
                            else {
                                linkAvatar = downloadUrl.toString();
                            }
                            UserProfile newUserProfile =
                                    new UserProfile(etNickname.getText().toString(),
                                            linkAvatar,
                                            etIntroduceYourSelf.getText().toString(),
                                            etCity.getText().toString(),
                                            tvBirthday.getText().toString(),
                                            firebaseUser.getUid(),
                                            spnSex.getSelectedItem().toString(),
                                            userProfile.getEmail());
                            databaseReference.child("UserProfile").child(firebaseUser.getUid())
                                    .setValue(newUserProfile);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(20)
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
