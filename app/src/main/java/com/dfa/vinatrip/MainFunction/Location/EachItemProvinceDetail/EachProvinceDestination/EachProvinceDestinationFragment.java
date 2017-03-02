package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceDestination;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.FullPhoto.ShowFullPhotoActivity;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.Rating.RatingActivity;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.Rating.RatingAdapter;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.Rating.UserRating;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.MapActivity;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EachProvinceDestinationFragment extends Fragment {

    private TextView tvAddress, tvRate, tvCommentNotAvailable;
    private ProvinceDestination detailDestination;
    private LinearLayout llAddress;
    private RecyclerView rvProvinceDestinationPhotos, rvUserRatings;
    private SwipeRefreshLayout srlReload;
    private List<String> listUrlPhotos;
    private List<UserRating> listUserRatings;
    private ProvinceDestinationPhotoAdapter provinceDestinationPhotoAdapter;
    private RatingAdapter ratingAdapter;
    private ImageView ivMap;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private ExpandableTextView expTvScheduleAndFee, expTvDescription;
    static final int NOTIFY_UPDATE_REQUEST = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_each_province_destination, container, false);

        findViewByIds(view);

        // Get the DetailDestination be chosen from EachItemProvinceDetailActivity
        detailDestination =
                (ProvinceDestination) getArguments().getSerializable("DetailDestination");

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceDestinationPhoto();
            loadProvinceDestinationRating();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    listUserRatings.clear();
                    loadProvinceDestinationPhoto();
                    loadProvinceDestinationRating();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        setContentViews();

        setOnClickListener();

        StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvProvinceDestinationPhotos.setLayoutManager(manager);

        StaggeredGridLayoutManager manager1 =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvUserRatings.setLayoutManager(manager1);

        return view;
    }

    public void setOnClickListener() {
        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(getActivity(), MapActivity.class);

                // Send DetailDestination to MapActivity
                intentMap.putExtra("DetailDestination", detailDestination);
                startActivity(intentMap);
            }
        });

        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRate = new Intent(getActivity(), RatingActivity.class);

                // Send DetailDestination to RatingActivity
                intentRate.putExtra("DetailDestination", detailDestination);
                // Send ListUserRatings to know user has comment yet?
                intentRate.putExtra("ListUserRatings", (Serializable) listUserRatings);
                // Make RateActivity notify when it finish
                startActivityForResult(intentRate, NOTIFY_UPDATE_REQUEST);
            }
        });

        // Catch event when click item on RecyclerView
        rvProvinceDestinationPhotos.addOnItemTouchListener
                (new RecyclerItemClickListener(getActivity(), rvProvinceDestinationPhotos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intentShowFull =
                                        new Intent(getActivity(), ShowFullPhotoActivity.class);

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
            if (CheckNetwork.isNetworkConnected(getActivity())) {
                listUserRatings.clear();
                loadProvinceDestinationRating();
            }
        }
    }

    public void findViewByIds(View view) {
        tvAddress = (TextView)
                view.findViewById(R.id.fragment_each_province_destination_tv_address);
        tvCommentNotAvailable = (TextView)
                view.findViewById(R.id.fragment_each_province_destination_tv_comment_not_available);
        tvRate = (TextView) view.findViewById(R.id.fragment_each_province_destination_tv_rate);
        llAddress = (LinearLayout)
                view.findViewById(R.id.fragment_each_province_destination_ll_address);
        srlReload = (SwipeRefreshLayout)
                view.findViewById(R.id.fragment_each_province_destination_srlReload);
        rvProvinceDestinationPhotos = (RecyclerView)
                view.findViewById(R.id.fragment_each_province_destination_rv_photos);
        rvUserRatings = (RecyclerView)
                view.findViewById(R.id.fragment_each_province_destination_rv_user_ratings);
        ivMap = (ImageView) view.findViewById(R.id.fragment_each_province_destination_iv_map);
        expTvScheduleAndFee = (ExpandableTextView)
                view.findViewById(R.id.fragment_each_province_destination_exptv_schedule_and_fee);
        expTvDescription = (ExpandableTextView)
                view.findViewById(R.id.fragment_each_province_destination_exptv_description);
    }

    public void setContentViews() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // tvRate be disabled until listUserRatings load done
        tvRate.setVisibility(View.GONE);

        expTvDescription.setText(Html.fromHtml(detailDestination.getDescription()));
        expTvScheduleAndFee.setText(Html.fromHtml(detailDestination.getScheduleAndFee()));
        tvAddress.setText(detailDestination.getAddress());

        listUrlPhotos = new ArrayList<>();
        provinceDestinationPhotoAdapter =
                new ProvinceDestinationPhotoAdapter(getActivity(), listUrlPhotos, srlReload);
        rvProvinceDestinationPhotos.setAdapter(provinceDestinationPhotoAdapter);

        listUserRatings = new ArrayList<>();
        ratingAdapter =
                new RatingAdapter(getActivity(), listUserRatings);
        rvUserRatings.setAdapter(ratingAdapter);

        // Load static map
        String url = "http://maps.google.com/maps/api/staticmap?center="
                + detailDestination.getLatitude()
                + "," + detailDestination.getLongitude()
                + "&zoom=15&size=100x120&sensor=false";
        Picasso.with(getActivity()).load(url)
                .into(ivMap, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void loadProvinceDestinationPhoto() {
        srlReload.setRefreshing(true);
        DatabaseReference referencePhoto = firebaseDatabase.getReference();
        // if no Internet, this method will not run
        referencePhoto
                .child("ProvinceDestinationPhoto")
                .child(detailDestination.getProvince())
                .child(detailDestination.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.child("url").getValue().toString();

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
    }

    public void loadProvinceDestinationRating() {
        // if no Internet, this method will not run
        DatabaseReference referenceRating = firebaseDatabase.getReference();
        referenceRating
                .child("ProvinceDestinationRating")
                .child(detailDestination.getProvince())
                .child(detailDestination.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String uid, nickname, avatar, email, content, numStars, date;

                        uid = dataSnapshot.child("uid").getValue().toString();
                        nickname = dataSnapshot.child("nickname").getValue().toString();
                        avatar = dataSnapshot.child("avatar").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();
                        content = dataSnapshot.child("content").getValue().toString();
                        numStars = dataSnapshot.child("numStars").getValue().toString();
                        date = dataSnapshot.child("date").getValue().toString();

                        UserRating userRating = new UserRating(uid, nickname, avatar,
                                email, content, numStars, date);

                        rvUserRatings.setVisibility(View.VISIBLE);
                        tvCommentNotAvailable.setVisibility(View.GONE);
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
        referenceRating
                .child("ProvinceDestinationRating")
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