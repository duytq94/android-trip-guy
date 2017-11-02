package com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.blurry.Blurry;

import static android.app.Activity.RESULT_OK;
import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PLACE_AUTO_COMPLETE;
import static com.dfa.vinatrip.utils.AppUtil.exifToDegrees;
import static com.dfa.vinatrip.utils.Constants.FOLDER_AVATAR_USER;
import static com.sangcomz.fishbun.define.Define.ALBUM_REQUEST_CODE;

@EFragment(R.layout.fragment_update_user_profile)
public class UpdateUserProfileFragment extends BaseFragment<UpdateUserProfileView, UpdateUserProfilePresenter>
        implements UpdateUserProfileView {

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
    @ViewById(R.id.fragment_update_user_profile_spn_sex)
    protected Spinner spnSex;
    @ViewById(R.id.fragment_update_user_profile_tv_birthday)
    protected TextView tvBirthday;
    @ViewById(R.id.fragment_update_user_profile_sv_root)
    protected ScrollView svRoot;

    private StorageReference storageReference;
    private User currentUser;
    private Calendar calendar;
    private Bitmap adjustedBitmap;
    private ArrayList<Uri> photo;
    private String linkNewAvatar;

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
        photo = new ArrayList<>();
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

    @Click(R.id.fragment_update_user_profile_btn_done)
    public void onBtnDoneClick() {
        svRoot.scrollTo(0, svRoot.getBottom());

        if (photo.get(0) != null) {
            showHUD();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayPhoto = baos.toByteArray();

            // Get the path and name photo be upload
            storageReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl("gs://tripguy-10864.appspot.com")
                    .child(FOLDER_AVATAR_USER)
                    .child(currentUser.getId() + ".jpg");

            UploadTask uploadTask = storageReference.putBytes(byteArrayPhoto);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl == null) {
                    linkNewAvatar = currentUser.getAvatar();
                } else {
                    linkNewAvatar = downloadUrl.toString();
                }
                sendNewUser();
            })
                    .addOnFailureListener(e -> {
                        sendNewUser();
                    });
        } else {
            sendNewUser();
        }
    }

    public void sendNewUser() {
        User newUser = new User(etNickname.getText().toString(),
                linkNewAvatar,
                tvBirthday.getText().toString(),
                etIntroduceYourSelf.getText().toString(),
                spnSex.getSelectedItemPosition() + 1,
                null,
                null);
        presenter.editProfile(newUser);
    }

    @Click(R.id.fragment_update_user_profile_btn_cancel)
    public void onBtnCancelClick() {
        getActivity().finish();
    }

    @Click(R.id.fragment_update_user_profile_iv_change_avatar)
    public void onIvChangeAvatarClick() {
        FishBun.with(this)
                .MultiPageMode()
                .setMaxCount(1)
                .setMinCount(1)
                .setPickerSpanCount(4)
                .setActionBarColor(Color.parseColor("#228B22"), Color.parseColor("#156915"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setArrayPaths(photo)
                .setAlbumSpanCount(1, 2)
                .setButtonInAlbumActivity(true)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_back_white_24dp))
                .setOkButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_check_white_48dp))
                .setAllViewTitle("Tất cả ảnh")
                .setActionBarTitle("Chọn ảnh")
                .textOnNothingSelected("Hãy chọn ít nhất 1 ảnh")
                .startAlbum();
    }

    @Click(R.id.fragment_update_user_profile_ll_birthday)
    public void onLlBirthdayClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            tvBirthday.setText(String.format("%s/%s/%s", dayOfMonth, month + 1, year));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ALBUM_REQUEST_CODE) {
                photo = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                Picasso.with(getActivity())
                        .load(photo.get(0))
                        .into(target);
            }
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
            bitmap = AppUtil.scaleDown(bitmap, 1080, true);
            try {
                String realPath = AppUtil.getRealPath(getActivity(), photo.get(0));
                ExifInterface exif = new ExifInterface(realPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation != 0f) {
                    Matrix matrix = new Matrix();
                    int rotationInDegrees = exifToDegrees(orientation);
                    matrix.preRotate(rotationInDegrees);
                    adjustedBitmap = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    adjustedBitmap = AppUtil.scaleDown(adjustedBitmap, 1080, true);
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
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editProfileSuccess(User newUser) {
        newUser.setAccessToken(currentUser.getAccessToken());
        currentUser = newUser;
        presenter.setCurrentUser(newUser);
        Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }
}
