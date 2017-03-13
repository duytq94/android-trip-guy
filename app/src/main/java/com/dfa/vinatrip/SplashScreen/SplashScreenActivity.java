package com.dfa.vinatrip.SplashScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.MainActivity_;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    List<Province> provinceList;
    List<UserProfile> userProfileList;
    List<UserFriend> userFriendList;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @AfterViews
    void onCreate() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            loadProvinceAndMore();
        } else {
            loadProvince();
        }
    }

    public void loadProvince() {
        provinceList = new ArrayList<>();

        // If no Internet, this method will not run
        databaseReference.child("Province").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Province province = dataSnapshot.getValue(Province.class);
                        provinceList.add(province);
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
        databaseReference.child("Province").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataService.setProvinceList(provinceList);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadProvinceAndMore() {
        provinceList = new ArrayList<>();

        // If no Internet, this method will not run
        databaseReference.child("Province").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Province province = dataSnapshot.getValue(Province.class);
                        provinceList.add(province);
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
        databaseReference.child("Province").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataService.setProvinceList(provinceList);
                Toast.makeText(SplashScreenActivity.this, "province done", Toast.LENGTH_SHORT).show();
                loadUserProfile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadUserProfile() {
        userProfileList = new ArrayList<>();

        // If no Internet, this method will not run
        databaseReference.child("UserProfile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                userProfileList.add(userProfile);
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
        databaseReference.child("UserProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataService.setUserProfileList(userProfileList);
                        loadUserFriend();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadUserFriend() {
        userFriendList = new ArrayList<>();

        // If no Internet, this method will not run
        databaseReference.child("UserFriend").child(firebaseUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);

                        // Don't add the friend not agree yet to list
                        if (!userFriend.getFriendId().equals(firebaseUser.getUid()) &&
                                userFriend.getState().equals("friend")) {
                            userFriendList.add(userFriend);
                        }
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
        databaseReference.child("UserFriend").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataService.setUserFriendList(userFriendList);
                        getCurrentUserProfile();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getCurrentUserProfile() {
        for (UserProfile userProfile : userProfileList) {
            if (userProfile.getUid().equals(firebaseUser.getUid())) {
                dataService.setCurrentUser(userProfile);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
                break;
            }
        }
    }
}
