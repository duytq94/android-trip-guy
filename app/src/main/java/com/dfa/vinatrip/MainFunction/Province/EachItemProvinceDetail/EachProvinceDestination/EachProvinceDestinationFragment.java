package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceDestination;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.FullPhoto.ShowFullPhotoActivity_;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating.RatingActivity_;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating.RatingAdapter;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating.UserRating;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.MapActivity_;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.TripGuyUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_each_province_destination)
public class EachProvinceDestinationFragment extends Fragment {

    @ViewById(R.id.fragment_each_province_destination_tv_address)
    TextView tvAddress;

    @ViewById(R.id.fragment_each_province_destination_tv_comment_not_available)
    TextView tvCommentNotAvailable;

    @ViewById(R.id.fragment_each_province_destination_tv_rate)
    TextView tvRate;

    @ViewById(R.id.fragment_each_province_destination_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_each_province_destination_rv_photos)
    RecyclerView rvProvinceDestinationPhotos;

    @ViewById(R.id.fragment_each_province_destination_rv_user_ratings)
    RecyclerView rvUserRatings;

    @ViewById(R.id.fragment_each_province_destination_iv_map)
    ImageView ivMap;

    @ViewById(R.id.fragment_each_province_destination_exptv_schedule_and_fee)
    ExpandableTextView expTvScheduleAndFee;

    @ViewById(R.id.fragment_each_province_destination_exptv_description)
    ExpandableTextView expTvDescription;

    @Click(R.id.fragment_each_province_destination_ll_address)
    void onLlAddressClick() {
        Intent intentMap = new Intent(getActivity(), MapActivity_.class);

        // Send DetailDestination to MapActivity
        intentMap.putExtra("DetailDestination", detailDestination);
        startActivity(intentMap);
    }

    @Click(R.id.fragment_each_province_destination_tv_rate)
    void onTvRateClick() {
        Intent intentRate = new Intent(getActivity(), RatingActivity_.class);

        // Send DetailDestination to RatingActivity
        intentRate.putExtra("DetailDestination", detailDestination);
        // Send ListUserRatings to know user has comment yet?
        intentRate.putParcelableArrayListExtra("ListUserRatings", (ArrayList<? extends Parcelable>) listUserRatings);
        // Make RateActivity notify when it finish
        startActivityForResult(intentRate, NOTIFY_UPDATE_REQUEST);
    }

    private ProvinceDestination detailDestination;
    private List<String> listUrlPhotos;
    private List<UserRating> listUserRatings;
    private ProvinceDestinationPhotoAdapter provinceDestinationPhotoAdapter;
    private RatingAdapter ratingAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    static final int NOTIFY_UPDATE_REQUEST = 2;

    @AfterViews
    void onCreateView() {
        // Get the DetailDestination be chosen from EachItemProvinceDetailActivity
        detailDestination = getArguments().getParcelable("DetailDestination");

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (TripGuyUtils.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvinceDestinationPhoto();
            loadProvinceDestinationRating();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    listUserRatings.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceDestinationPhoto();
                    loadProvinceDestinationRating();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        setContentViews();

        setOnClickListener();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvProvinceDestinationPhotos.setLayoutManager(manager);

        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvUserRatings.setLayoutManager(manager1);
    }

    public void setOnClickListener() {
        // Catch event when click item on RecyclerView
        rvProvinceDestinationPhotos.addOnItemTouchListener
                (new RecyclerItemClickListener(getActivity(), rvProvinceDestinationPhotos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intentShowFull =
                                        new Intent(getActivity(), ShowFullPhotoActivity_.class);

                                // Send ListUrlPhotos to ShowFullPhotoActivity
                                intentShowFull.putStringArrayListExtra("ListUrlPhotos",
                                        (ArrayList<String>) listUrlPhotos);
                                // Send the Position photo user click
                                intentShowFull.putExtra("Position", position);
                                // Send DetailDestination to ShowFullPhotoActivity
                                intentShowFull.putExtra("DetailDestination", detailDestination);

                                startActivity(intentShowFull);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Reload data
        if (requestCode == NOTIFY_UPDATE_REQUEST) {
            if (TripGuyUtils.isNetworkConnected(getActivity())) {
                listUserRatings.clear();
                loadProvinceDestinationRating();
            }
        }
    }

    public void setContentViews() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // tvRate be disabled until listUserRatings load done
        tvRate.setVisibility(View.GONE);

        expTvDescription.setText(Html.fromHtml(detailDestination.getDescription()));
        expTvScheduleAndFee.setText(Html.fromHtml(detailDestination.getScheduleAndFee()));
        tvAddress.setText(detailDestination.getAddress());

        listUrlPhotos = new ArrayList<>();
        provinceDestinationPhotoAdapter = new ProvinceDestinationPhotoAdapter(getActivity(), listUrlPhotos);
        rvProvinceDestinationPhotos.setAdapter(provinceDestinationPhotoAdapter);

        listUserRatings = new ArrayList<>();
        ratingAdapter = new RatingAdapter(getActivity(), listUserRatings);
        rvUserRatings.setAdapter(ratingAdapter);

        // Load static map
        String url = "http://maps.google.com/maps/api/staticmap?center="
                + detailDestination.getLatitude()
                + "," + detailDestination.getLongitude()
                + "&zoom=15&size=100x120&sensor=false";
        Picasso.with(getActivity()).load(url)
                .into(ivMap);
    }

    public void loadProvinceDestinationPhoto() {
        DatabaseReference referencePhoto = firebaseDatabase.getReference();
        // if no Internet, this method will not run
        referencePhoto.child("ProvinceDestinationPhoto").child(detailDestination.getProvince()).child(detailDestination.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.getValue().toString();

                        listUrlPhotos.add(url);
                        provinceDestinationPhotoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        referencePhoto.child("ProvinceDestinationPhoto").child(detailDestination.getProvince()).child(detailDestination.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            srlReload.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadProvinceDestinationRating() {
        DatabaseReference referenceRating = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        referenceRating.child("ProvinceDestinationRating").child(detailDestination.getProvince())
                .child(detailDestination.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserRating userRating = dataSnapshot.getValue(UserRating.class);

                        if (isAdded()) {
                            rvUserRatings.setVisibility(View.VISIBLE);
                            tvCommentNotAvailable.setVisibility(View.GONE);
                        }
                        listUserRatings.add(userRating);
                        ratingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // This method to be called after all the onChildAdded() calls have happened
        referenceRating.child("ProvinceDestinationRating")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvRate.setVisibility(View.VISIBLE);

                        // If user has comment, chnage text of tvRate
                        for (int i = 0; i < listUserRatings.size(); i++) {
                            UserRating userRating = listUserRatings.get(i);
                            if (firebaseUser != null) {
                                if (userRating.getUid().equals(firebaseUser.getUid())) {
                                    tvRate.setText(R.string.update_rating);
                                    return;
                                }
                            }
                        }
                        tvRate.setText(R.string.rating);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}