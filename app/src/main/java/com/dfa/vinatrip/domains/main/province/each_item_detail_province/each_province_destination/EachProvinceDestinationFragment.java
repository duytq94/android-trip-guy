package com.dfa.vinatrip.domains.main.province.each_item_detail_province.each_province_destination;

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
import android.widget.Toast;

import com.dfa.vinatrip.R;
//import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestination;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.RatingActivity_;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.RatingAdapter;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.UserRating;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.MapActivity_;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.ShowFullPhotoLocalActivity_;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static com.dfa.vinatrip.utils.AppUtil.NOTIFY_UPDATE_REQUEST;

@EFragment(R.layout.fragment_each_province_destination)
public class EachProvinceDestinationFragment extends Fragment {

//    @Bean
//    DataService dataService;
//
//    @ViewById(R.id.fragment_each_province_destination_tv_address)
//    TextView tvAddress;
//
//    @ViewById(R.id.fragment_each_province_destination_tv_comment_not_available)
//    TextView tvCommentNotAvailable;
//
//    @ViewById(R.id.fragment_each_province_destination_tv_rate)
//    TextView tvRate;
//
//    @ViewById(R.id.fragment_each_province_destination_srlReload)
//    SwipeRefreshLayout srlReload;
//
//    @ViewById(R.id.fragment_each_province_destination_rv_photos)
//    RecyclerView rvProvinceDestinationPhotos;
//
//    @ViewById(R.id.fragment_each_province_destination_rv_user_ratings)
//    RecyclerView rvUserRatings;
//
//    @ViewById(R.id.fragment_each_province_destination_iv_map)
//    ImageView ivMap;
//
//    @ViewById(R.id.fragment_each_province_destination_exptv_schedule_and_fee)
//    ExpandableTextView expTvScheduleAndFee;
//
//    @ViewById(R.id.fragment_each_province_destination_exptv_description)
//    ExpandableTextView expTvDescription;
//
//    private ProvinceDestinationDetail destinationDetail;
//    private ProvinceDestination destination;
//    private List<String> listUrlPhotos;
//    private List<UserRating> listUserRatings;
//    private ProvinceDestinationPhotoAdapter provinceDestinationPhotoAdapter;
//    private RatingAdapter ratingAdapter;
//    private DatabaseReference databaseReference;
//    private ChildEventListener childEventListenerPhoto;
//    private ChildEventListener childEventListenerDetail;
//    private ChildEventListener childEventListenerRating;
//    private ValueEventListener valueEventListenerPhoto;
//    private ValueEventListener valueEventListenerRating;

    @AfterViews
    void init() {
        // Get the Destination be chosen from EachItemProvinceDetailActivity
//        destination = getArguments().getParcelable("Destination");
//
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        setContentViews();
//
//        srlReload.setColorSchemeResources(R.color.colorMain);
//        if (AppUtil.isNetworkConnected(getActivity())) {
//            srlReload.setRefreshing(true);
//            loadProvinceDestinationPhoto();
//            loadProvinceDestinationRating();
//            loadProvinceDestinationDetail();
//        }
//        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (AppUtil.isNetworkConnected(getActivity())) {
//                    listUrlPhotos.clear();
//                    listUserRatings.clear();
//                    srlReload.setRefreshing(true);
//                    loadProvinceDestinationPhoto();
//                    loadProvinceDestinationRating();
//                    loadProvinceDestinationDetail();
//                } else {
//                    srlReload.setRefreshing(false);
//                }
//            }
//        });
//
//        setOnClickListener();
//
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        rvProvinceDestinationPhotos.setLayoutManager(manager);
//
//        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rvUserRatings.setLayoutManager(manager1);
    }

    public void setOnClickListener() {
        // Catch event when click item on RecyclerView
//        rvProvinceDestinationPhotos.addOnItemTouchListener
//                (new RecyclerItemClickListener(
//                        getActivity(), rvProvinceDestinationPhotos,
//                        new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                ShowFullPhotoLocalActivity_.intent(getActivity()).destination(destination).position(position)
//                                                      .listUrlPhotos((ArrayList<String>) listUrlPhotos).start();
//                            }
//
//                            @Override
//                            public void onLongItemClick(View view, int position) {
//
//                            }
//                        }));
    }

    @OnActivityResult(NOTIFY_UPDATE_REQUEST)
    void onResult(int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && data != null) {
//            if (AppUtil.isNetworkConnected(getActivity())) {
//                listUserRatings.clear();
//                loadProvinceDestinationRating();
//            }
//        } else {
//            Toasty.error(getActivity(), "Không thể cập nhật đánh giá, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
//        }
    }

    @Click(R.id.fragment_each_province_destination_ll_address)
    void onLlAddressClick() {
        // Send DetailDestination to MapActivity
        //MapActivity_.intent(getActivity()).detailDestination(destinationDetail).start();
    }

    @Click(R.id.fragment_each_province_destination_tv_rate)
    void onTvRateClick() {
//        Intent intentRate = new Intent(getActivity(), RatingActivity_.class);
//
//        // Send DetailDestination to RatingActivity
//        intentRate.putExtra("DetailDestination", destinationDetail);
//        // Send ListUserRatings to know user has comment yet?
//        intentRate.putParcelableArrayListExtra("ListUserRatings", (ArrayList<? extends Parcelable>) listUserRatings);
//        // Make RateActivity notify when it finish
//        startActivityForResult(intentRate, NOTIFY_UPDATE_REQUEST);
    }

    public void setContentViews() {
        // tvRate be disabled until listUserRatings load done
//        tvRate.setVisibility(View.GONE);
//
//        listUrlPhotos = new ArrayList<>();
//        provinceDestinationPhotoAdapter = new ProvinceDestinationPhotoAdapter(getActivity(), listUrlPhotos);
//        rvProvinceDestinationPhotos.setAdapter(provinceDestinationPhotoAdapter);
//
//        listUserRatings = new ArrayList<>();
//        ratingAdapter = new RatingAdapter(getActivity(), listUserRatings, dataService.getCurrentUser());
//        rvUserRatings.setAdapter(ratingAdapter);
//
//        childEventListenerPhoto = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String url;
//                url = dataSnapshot.getValue().toString();
//
//                listUrlPhotos.add(url);
//                provinceDestinationPhotoAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        childEventListenerDetail = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                destinationDetail = dataSnapshot.getValue(ProvinceDestinationDetail.class);
//                if (isAdded()) {
//                    expTvDescription.setText(Html.fromHtml(destinationDetail.getDescription()));
//                    expTvScheduleAndFee.setText(Html.fromHtml(destinationDetail.getScheduleAndFee()));
//                    tvAddress.setText(destinationDetail.getAddress());
//
//                    // Load static map
//                    String url = "http://maps.google.com/maps/api/staticmap?center="
//                                 + destinationDetail.getLatitude()
//                                 + "," + destinationDetail.getLongitude()
//                                 + "&zoom=15&size=100x120&sensor=false";
//                    Picasso.with(getActivity()).load(url).into(ivMap);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        childEventListenerRating = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                UserRating userRating = dataSnapshot.getValue(UserRating.class);
//
//                if (isAdded()) {
//                    rvUserRatings.setVisibility(View.VISIBLE);
//                    tvCommentNotAvailable.setVisibility(View.GONE);
//                }
//                listUserRatings.add(userRating);
//                ratingAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        valueEventListenerPhoto = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (isAdded()) {
//                    srlReload.setRefreshing(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        valueEventListenerRating = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (isAdded()) {
//                    tvRate.setVisibility(View.VISIBLE);
//
//                    // If user has comment, change text of tvRate
//                    for (int i = 0; i < listUserRatings.size(); i++) {
//                        UserRating userRating = listUserRatings.get(i);
//                        if (dataService.getCurrentUser() != null) {
//                            if (userRating.getUid().equals(dataService.getCurrentUser().getUid())) {
//                                tvRate.setText(R.string.update_rating);
//                                return;
//                            }
//                        }
//                    }
//                    tvRate.setText(R.string.rating);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
    }

    public void loadProvinceDestinationPhoto() {
        // if no Internet, this method will not run
//        databaseReference.child("ProvinceDestinationPhoto").child(destination.getProvince())
//                         .child(destination.getName())
//                         .addChildEventListener(childEventListenerPhoto);
//
//        databaseReference.child("ProvinceDestinationPhoto").child(destination.getProvince())
//                         .child(destination.getName())
//                         .addListenerForSingleValueEvent(valueEventListenerPhoto);
    }

    public void loadProvinceDestinationDetail() {
//        databaseReference.child("ProvinceDestinationDetail").child(destination.getProvince())
//                         .addChildEventListener(childEventListenerDetail);
    }

    public void loadProvinceDestinationRating() {
        // if no Internet, this method will not run
//        databaseReference.child("ProvinceDestinationRating").child(destination.getProvince())
//                         .child(destination.getName())
//                         .addChildEventListener(childEventListenerRating);
//
//        // This method to be called after all the onChildAdded() calls have happened
//        databaseReference.child("ProvinceDestinationRating")
//                         .addListenerForSingleValueEvent(valueEventListenerRating);
    }

    @Override
    public void onPause() {
        super.onPause();
//        databaseReference.removeEventListener(childEventListenerDetail);
//        databaseReference.removeEventListener(childEventListenerPhoto);
//        databaseReference.removeEventListener(childEventListenerRating);
//        databaseReference.removeEventListener(valueEventListenerRating);
//        databaseReference.removeEventListener(valueEventListenerPhoto);
    }
}