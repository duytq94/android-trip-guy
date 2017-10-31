package com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AppUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.blurry.Blurry;

import static android.app.Activity.RESULT_OK;
import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PICK_IMAGE;
import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PLACE_AUTO_COMPLETE;
import static com.dfa.vinatrip.utils.AppUtil.exifToDegrees;

@EFragment(R.layout.fragment_update_user_profile)
public class UpdateUserProfileFragment extends BaseFragment<UpdateUserProfileView, UpdateUserProfilePresenter>
        implements UpdateUserProfileView {

    @ViewById(R.id.fragment_update_user_profile_tv_percent)
    protected TextView tvPercent;
    @ViewById(R.id.fragment_update_user_profile_et_nickname)
    protected EditText etNickname;
    @ViewById(R.id.fragment_update_user_profile_tv_city)
    protected TextView tvCity;
    @ViewById(R.id.fragment_update_user_profile_et_introduce_your_self)
    protected EditText etIntroduceYourSelf;
    @ViewById(R.id.fragment_update_user_profile_iv_avatar)
    protected ImageView ivAvatar;
    @ViewById(R.id.fragment_update_user_profile_iv_blur_avatar)
    protected ImageView ivBlurAvatar;
    @ViewById(R.id.fragment_update_user_profile_progressBar)
    protected ProgressBar progressBar;
    @ViewById(R.id.fragment_update_user_profile_spn_sex)
    protected Spinner spnSex;
    @ViewById(R.id.fragment_update_user_profile_tv_birthday)
    protected TextView tvBirthday;
    @ViewById(R.id.fragment_update_user_profile_sv_root)
    protected ScrollView svRoot;

    private StorageReference storageReference;
    private User currentUser;
    private Calendar calendar;
    private Uri uri;
    private Bitmap adjustedBitmap;

    @App
    protected MainApplication application;
    @Inject
    protected UpdateUserProfilePresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerUpdateUserProfileComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public UpdateUserProfilePresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        adjustedBitmap = null;

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.sex_array, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapter);

        currentUser = presenter.getCurrentUser();

        calendar = Calendar.getInstance();

        if (currentUser != null) {
            if (currentUser.getAvatar() != null) {
                Picasso.with(getActivity())
                        .load(currentUser.getAvatar())
                        .into(target);
            }
            if (currentUser.getUsername() != null) {
                etNickname.setText(currentUser.getUsername());
            }
            if (currentUser.getCity() != null) {
                tvCity.setText(currentUser.getCity());
            }
            if (currentUser.getBirthday() != null) {
                tvBirthday.setText(currentUser.getBirthday());
            } else {
                setCurrentDayForView();
            }
            if (currentUser.getIntro() != null) {
                etIntroduceYourSelf.setText(currentUser.getIntro());
            }
            switch (currentUser.getSex()) {
                case 0:
                    spnSex.setSelection(0);
                    break;
                case 1:
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
        svRoot.scrollTo(0, svRoot.getBottom());
        progressBar.setVisibility(View.VISIBLE);
        AppUtil.setEnableAllViews(svRoot, false);
        tvPercent.setVisibility(View.VISIBLE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayPhoto = baos.toByteArray();

        // Get the path and name photo be upload
        storageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                .child("AvatarProfileUser")
                .child(currentUser.getId() + ".jpg");

        UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
        uploadTask
                .addOnProgressListener(taskSnapshot -> {
                    long progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if (isAdded()) {
                        tvPercent.setText(progress + "%");
                    }
                })
                .addOnFailureListener(exception -> {
                    progressBar.setVisibility(View.GONE);
                    AppUtil.setEnableAllViews(svRoot, true);
                    tvPercent.setVisibility(View.GONE);
                    Toasty.error(getActivity(),
                            "Cập nhật không thành công\nBạn vui lòng thử lại",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String linkAvatar;
                    if (downloadUrl == null) linkAvatar = currentUser.getAvatar();
                    else {
                        linkAvatar = downloadUrl.toString();
                    }

                    if (isAdded()) {
                        User newUserProfile =
                                new User(etNickname.getText().toString(),
                                        linkAvatar,
                                        tvBirthday.getText().toString(),
                                        etIntroduceYourSelf.getText().toString(),
                                        spnSex.getSelectedItemPosition(),
                                        "",
                                        tvCity.getText().toString());
                        //TODO post new user profile
                        progressBar.setVisibility(View.GONE);
                        AppUtil.setEnableAllViews(svRoot, true);
                        tvPercent.setVisibility(View.GONE);
                        Toasty.success(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
    }

    @Click(R.id.fragment_update_user_profile_btn_done)
    public void onBtnDoneClick() {
        // Check if user update info but don't update avatar
        if (uri == null) {
            //TODO update info
        } else {
            uploadUserAvatar();
        }
    }

    @Click(R.id.fragment_update_user_profile_btn_cancel)
    public void onBtnCancelClick() {
        getActivity().finish();
    }

    @Click(R.id.fragment_update_user_profile_iv_change_avatar)
    public void onIvChangeAvatarClick() {
        askPermission();
        if (checkPermission()) {
            Intent intentToLibrary = new Intent();
            intentToLibrary.setType("image/*");
            intentToLibrary.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentToLibrary, REQUEST_PICK_IMAGE);
        }
    }

    @Click(R.id.fragment_update_user_profile_ll_birthday)
    public void onLlBirthdayClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            tvBirthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            calendar.set(year, month, dayOfMonth);
        };

        // Set current position time when start dialog
        String strDate[] = tvBirthday.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dialog.setTitle("Chọn ngày sinh");
        dialog.show();
    }

    @Click(R.id.fragment_update_user_profile_ll_city)
    public void onLlCityClick() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("VN").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter).build(getActivity());
            startActivityForResult(intent, REQUEST_PLACE_AUTO_COMPLETE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnActivityResult(REQUEST_PICK_IMAGE)
    public void onResultImage(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            // Show avatar be chosen for user first, not upload yet
            Picasso.with(getActivity())
                    .load(uri)
                    .into(target);
        } else {
            Toasty.error(getActivity(), "Không thể chọn được hình, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    @OnActivityResult(REQUEST_PLACE_AUTO_COMPLETE)
    public void onResultPlace(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            tvCity.setText(place.getAddress());
        } else if (data == null) {
            Toasty.error(getActivity(), "Không chọn được địa điểm, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmap = AppUtil.scaleDown(bitmap, 300, true);
            try {
                String realPath = AppUtil.getRealPath(getActivity(), uri);
                ExifInterface exif = new ExifInterface(realPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation != 0f) {
                    Matrix matrix = new Matrix();
                    int rotationInDegrees = exifToDegrees(orientation);
                    matrix.preRotate(rotationInDegrees);
                    adjustedBitmap = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    adjustedBitmap = AppUtil.scaleDown(adjustedBitmap, 300, true);
                } else {
                    adjustedBitmap = bitmap;
                }
            } catch (Exception e) {
            }

            if (adjustedBitmap != null) {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                        .from(adjustedBitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(adjustedBitmap);
            } else {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                        .from(bitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(getActivity(), permissions, 10);
            }
        }
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }

}
