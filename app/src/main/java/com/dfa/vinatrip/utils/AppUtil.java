package com.dfa.vinatrip.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.dfa.vinatrip.utils.Constants.FORMAT_DAY_VN;
import static com.dfa.vinatrip.utils.Constants.MILLISECOND_IN_DAY;

/**
 * Created by duonghd on 9/27/2017.
 */

public class AppUtil {
    public static final int REQUEST_PICK_IMAGE = 1;
    public static final int REQUEST_PLACE_AUTO_COMPLETE = 2;
    public static final int REQUEST_PROVINCE = 3;
    public static final int NOTIFY_UPDATE_REQUEST = 4;
    // To notify update fragment me when user back from UserProfileDetailActivity
    public static final int REQUEST_UPDATE_INFO = 5;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static DisplayMetrics dm;

    public static void init(Context context) {
        AppUtil.context = context;
    }

    public Context getContext() {
        return context;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // Filter the friend has accepted (because some friend not accepted yet)
    public static List<UserFriend> filterListFriends(List<UserFriend> userFriendList) {
        for (int i = 0; i < userFriendList.size(); i++) {
            if (!userFriendList.get(i).getState().equals("friend")) {
                userFriendList.remove(i);
            }
        }
        return userFriendList;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPath(final Context context, final Uri uri) {
        boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        String column = "_data";
        String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void requestTurnOnGPS(Context context) {
        GoogleApiClient googleApiClient;

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            final LocationSettingsStates state = result1.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult((Activity) context, 1000);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    break;
            }
        });
    }

    // 0 is today, 1 is yesterday, 2 is past
    // It auto parse to current device zone
    public static int getDateType(long timeStamp) {
        if (DateUtils.isToday(timeStamp)) {
            return 0;
        }
        if (DateUtils.isToday(timeStamp + MILLISECOND_IN_DAY)) {
            return 1;
        }
        return 2;
    }

    public static String formatTime(String pattern, final long timeStamp) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
        Date date = new Date(timeStamp);
        return dateFormat.format(date);
    }

    // Convert string date, 20/11/2011 - > 1232112334
    public static long stringDateToTimestamp(String date) {
        Date localTime;
        try {
            localTime = new SimpleDateFormat(FORMAT_DAY_VN, Locale.getDefault()).parse(date);
            return localTime.getTime();
        } catch (java.text.ParseException e) {
            return 0;
        }
    }

    public static int dpToPx(Context context, float dp) {
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static String convertPrice(double price) {
        DecimalFormat decimalFormat;
        decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();
        decimalFormat.applyPattern("#,###,##0");
        return decimalFormat.format(price);
    }

    public static void setupUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyBoard(v);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideKeyBoard(View view) {
        if (context == null)
            throw new RuntimeException("Context is null, init(Context)");
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyBoard(Activity ctx) {
        if (context == null)
            throw new RuntimeException("Context is null, init(Context)");
        View viewFocus = ctx.getCurrentFocus();
        if (viewFocus != null) {
            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
        }
    }

    public static String convertStringQuery(String s) {
        char[] listU = {'ú', 'ù', 'ủ', 'ũ', 'ụ', 'ư', 'ứ', 'ừ', 'ử', 'ữ', 'ự'};
        char[] listE = {'é', 'è', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ế', 'ề', 'ể', 'ễ', 'ệ'};
        char[] listO = {'ó', 'ò', 'ỏ', 'õ', 'ọ', 'ô', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ơ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ'};
        char[] listA = {'á', 'à', 'ả', 'ã', 'ạ', 'â', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ'};
        char[] listI = {'í', 'ì', 'ỉ', 'ĩ', 'ị'};
        char[] listY = {'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ'};
        char[] listD = {'đ'};

        ArrayList<char[]> arrTest = new ArrayList<>(Arrays.asList(listU, listE, listO, listA, listI, listY, listD));
        char[] listCharacter = s.replace(" ", "")
                .replace(".", "")
                .replace(",", "")
                .toLowerCase().toCharArray();
        for (int i = 0; i < listCharacter.length; i++) {
            boolean kt = false;
            for (int j = 0; j < arrTest.size(); j++) {
                for (int k = 0; k < arrTest.get(j).length; k++) {
                    if (arrTest.get(j)[k] == listCharacter[i]) {
                        kt = true;
                        break;
                    }
                }
                if (kt) {
                    switch (j) {
                        case 0:
                            listCharacter[i] = 'u';
                            break;
                        case 1:
                            listCharacter[i] = 'e';
                            break;
                        case 2:
                            listCharacter[i] = 'o';
                            break;
                        case 3:
                            listCharacter[i] = 'a';
                            break;
                        case 4:
                            listCharacter[i] = 'i';
                            break;
                        case 5:
                            listCharacter[i] = 'y';
                            break;
                        case 6:
                            listCharacter[i] = 'd';
                            break;
                    }
                    break;
                }
            }
        }
        return String.copyValueOf(listCharacter);
    }

    public static float getDensity(Context context) {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm.density;
    }

    public static int getWidth(Context context) {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm.widthPixels;
    }
}
