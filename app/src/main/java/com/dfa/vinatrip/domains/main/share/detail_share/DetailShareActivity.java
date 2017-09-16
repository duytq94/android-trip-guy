package com.dfa.vinatrip.domains.main.share.detail_share;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.share.Share;
import com.dfa.vinatrip.utils.MapActivity_;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

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

    @ViewById(R.id.activity_detail_share_tv_destination)
    TextView tvDestination;

    @ViewById(R.id.activity_detail_share_indicator)
    CircleIndicator indicator;

    @Extra
    Share share;

    private ActionBar actionBar;
    private ViewPagerAdapter viewPagerAdapter;

    @AfterViews
    void init() {
        setupActionBar();

        tvDestination.setText(share.getDestination());
        tvName.setText(share.getName());
        tvAddress.setText(share.getAddress());
        Picasso.with(this).load(share.getAvatar())
               .placeholder(R.drawable.ic_loading)
               .error(R.drawable.photo_not_available)
               .into(civAvatar);
        tvNickname.setText(share.getNickname());
        tvContent.setText(share.getContent());

        viewPagerAdapter = new ViewPagerAdapter(this);

        vpSlideShow.setAdapter(viewPagerAdapter);
        indicator.setViewPager(vpSlideShow);
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

    @Click(R.id.activity_detail_share_tv_address)
    void onTvAddressClick() {
        List<Address> addressList;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocationName(share.getAddress(), 1);
            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
            MapActivity_.intent(this).latLng(latLng).detailShare(share).start();
        } catch (Exception e) {
            Toasty.error(this, "Không định vị được địa điểm").show();
            e.printStackTrace();
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

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Context context;

        public ViewPagerAdapter(Context context) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.item_photo_slide_show, container, false);
            switch (position) {
                case 0:
                    Picasso.with(context).load(share.getPhoto1()).placeholder(R.drawable.ic_loading)
                           .error(R.drawable.photo_not_available).into(imageView);
                    break;
                case 1:
                    Picasso.with(context).load(share.getPhoto2()).placeholder(R.drawable.ic_loading)
                           .error(R.drawable.photo_not_available).into(imageView);
                    break;
                case 2:
                    Picasso.with(context).load(share.getPhoto3()).placeholder(R.drawable.ic_loading)
                           .error(R.drawable.photo_not_available).into(imageView);
                    break;
                case 3:
                    Picasso.with(context).load(share.getPhoto4()).placeholder(R.drawable.ic_loading)
                           .error(R.drawable.photo_not_available).into(imageView);
                    break;
            }

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }
}
