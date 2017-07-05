package com.dfa.vinatrip.domains.main.share.make_share.image_multi_select;

/**
 * Created by duytq on 7/4/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class BitmapHelper {
    public static void getArrayBitmap(Context context, String[] imageSelect) {
        callBackBmHelper onCallback = (callBackBmHelper) context;

        int count = 0;
        for (String imagePath : imageSelect) {
            if (imagePath != null) {
                count = count + 1;
            } else {
                break;
            }
        }
        Bitmap[] imageBitmap = new Bitmap[count];
        int j = 0;
        for (int i = 0; i < count; i++) {
            try {
                imageBitmap[i] = Bitmap.createBitmap(
                        MediaStore.Images.Media.getBitmap(
                                context.getContentResolver(),
                                Uri.parse("file://" + imageSelect[i])));
                j = j + 1;
                if (j == count) {
                    onCallback.getBitmapSuccess(imageBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface callBackBmHelper {
        void getBitmapSuccess(Bitmap[] arrayBitmap);
    }
}