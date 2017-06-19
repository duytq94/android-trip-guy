package com.dfa.vinatrip.domains.main.share.detail_share;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.share.Share;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_detail_share)
public class DetailShareActivity extends AppCompatActivity {

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.activity_detail_share_vp_slide_show)
    ViewPager vpSlideShow;

    @ViewById(R.id.activity_detail_share_tv_name)
    TextView tvName;

    @ViewById(R.id.activity_detail_share_tv_address)
    TextView tvAddress;

    @ViewById(R.id.activity_detail_share_civ_avatar)
    CircleImageView civAvatar;

    @ViewById(R.id.activity_detail_share_tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.activity_detail_share_tv_content)
    TextView tvContent;

    @Extra
    Share share;

    private ActionBar actionBar;

    @AfterViews
    void init() {
        setupActionBar();

        tvName.setText(share.getName());
        tvAddress.setText(share.getAddress());
        Picasso.with(this).load(share.getAvatar())
               .placeholder(R.drawable.ic_loading)
               .error(R.drawable.photo_not_available)
               .into(civAvatar);
        tvNickname.setText(share.getNickname());
        tvContent.setText(share.getContent());
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(share.getProvince());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#228B22")));

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
