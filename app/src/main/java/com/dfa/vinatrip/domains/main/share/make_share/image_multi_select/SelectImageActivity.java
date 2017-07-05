package com.dfa.vinatrip.domains.main.share.make_share.image_multi_select;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by duytq on 7/4/2017.
 */

@EActivity(R.layout.activity_select_image)
public class SelectImageActivity extends AppCompatActivity implements ImageAdapter.callbackMainActivity {
    @ViewById(R.id.activity_main_tv_done)
    protected TextView tvDone;
    @ViewById(R.id.activity_main_recycler_view)
    protected RecyclerView recyclerView;

    @ViewById(R.id.activity_main_iv_select_1)
    protected ImageView ivSelect1;
    @ViewById(R.id.activity_main_iv_select_2)
    protected ImageView ivSelect2;
    @ViewById(R.id.activity_main_iv_select_3)
    protected ImageView ivSelect3;
    @ViewById(R.id.activity_main_iv_select_4)
    protected ImageView ivSelect4;

    private String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
    private String orderBy = MediaStore.Images.Media._ID;

    private int count;
    private long[] arrayId;
    private String[] arrPath;

    private int dx;
    private String[] imageSelect = new String[4];
    private boolean choseImage = false;

    @AfterViews
    void init() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dx = size.x;

        Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        this.count = imagecursor.getCount();
        this.arrayId = new long[this.count];
        this.arrPath = new String[this.count];

        for (int i = 0; i < this.count; i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int id = imagecursor.getInt(image_column_index);
            arrayId[i] = id;
            arrPath[this.count - 1 - i] = imagecursor.getString(dataColumnIndex);
        }

        imagecursor.close();
        showImage();
    }

    private void showImage() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new ImageAdapter(this, arrPath, arrayId, (dx - 4) / 4));
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void selectClick(String imagePath) {
        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] == null) {
                imageSelect[i] = imagePath;
                break;
            } else {
                if (imageSelect[i].equals(imagePath)) {
                    if (i == imageSelect.length - 1) {
                        imageSelect[i] = null;
                    } else {
                        for (int j = i; j < imageSelect.length - 1; j++) {
                            if (imageSelect[j + 1] != null) {
                                imageSelect[j] = imageSelect[j + 1];
                            } else {
                                imageSelect[j] = null;
                                break;
                            }
                        }
                        imageSelect[imageSelect.length - 1] = null;
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < imageSelect.length; i++) {
            if (imageSelect[i] != null) {
                switch (i) {
                    case 0:
                        ivSelect1.setVisibility(View.VISIBLE);
                        Picasso.with(this).load("file://" + imageSelect[i])
                               .resize(96, 96)
                               .centerCrop()
                               .into(ivSelect1);

                        tvDone.setBackgroundResource(R.drawable.bg_tv_done);
                        tvDone.setText("DONE");
                        tvDone.setTextColor(Color.parseColor("#ffffff"));
                        choseImage = true;
                        break;
                    case 1:
                        ivSelect2.setVisibility(View.VISIBLE);
                        Picasso.with(this).load("file://" + imageSelect[i])
                               .resize(96, 96)
                               .centerCrop()
                               .into(ivSelect2);
                        break;
                    case 2:
                        ivSelect3.setVisibility(View.VISIBLE);
                        Picasso.with(this).load("file://" + imageSelect[i])
                               .resize(96, 96)
                               .centerCrop()
                               .into(ivSelect3);
                        break;
                    case 3:
                        ivSelect4.setVisibility(View.VISIBLE);
                        Picasso.with(this).load("file://" + imageSelect[i])
                               .resize(96, 96)
                               .centerCrop()
                               .into(ivSelect4);
                        break;
                }
            } else {
                switch (i) {
                    case 0:
                        ivSelect1.setVisibility(View.INVISIBLE);
                        ivSelect2.setVisibility(View.GONE);
                        ivSelect3.setVisibility(View.GONE);
                        ivSelect4.setVisibility(View.GONE);
                        tvDone.setBackgroundResource(R.drawable.bg_tv_cancel);
                        tvDone.setText("CANCEL");
                        tvDone.setTextColor(Color.parseColor("#000000"));
                        choseImage = false;
                        break;
                    case 1:
                        ivSelect2.setVisibility(View.GONE);
                        ivSelect3.setVisibility(View.GONE);
                        ivSelect4.setVisibility(View.GONE);
                        break;
                    case 2:
                        ivSelect3.setVisibility(View.GONE);
                        ivSelect4.setVisibility(View.GONE);
                        break;
                    case 3:
                        ivSelect4.setVisibility(View.GONE);
                        break;
                }
                break;
            }
        }
    }

    @Override
    public void selectMax() {
        Toast.makeText(this, "Max 4 image", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.activity_main_tv_done)
    void choseClick() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("imageSelectURL", imageSelect);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
