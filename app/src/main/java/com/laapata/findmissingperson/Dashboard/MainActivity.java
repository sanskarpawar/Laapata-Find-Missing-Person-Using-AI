package com.laapata.findmissingperson.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.laapata.findmissingperson.Authentication.AuthActivity;
import com.laapata.findmissingperson.Dashboard.Activities.Create_Fp_Case;
import com.laapata.findmissingperson.Dashboard.Activities.FindPoliceStationAct;
import com.laapata.findmissingperson.Dashboard.Activities.MessageActivity;
import com.laapata.findmissingperson.Dashboard.Activities.Mp_complete_cases;
import com.laapata.findmissingperson.Dashboard.Activities.MyProfile;
import com.laapata.findmissingperson.Dashboard.Activities.StatsViewActivity;
import com.laapata.findmissingperson.Dashboard.Activities.TwoButtonActivity;
import com.laapata.findmissingperson.Dashboard.Activities.Your_MP_Cases;
import com.laapata.findmissingperson.Dashboard.Activities.Your_fp_cases;
import com.laapata.findmissingperson.Dashboard.Fragments.CasesViewActivity;
import com.laapata.findmissingperson.Dashboard.Fragments.ChatFragment;
import com.laapata.findmissingperson.Dashboard.Fragments.ChatNewActiivty;
import com.laapata.findmissingperson.Dashboard.Fragments.FP_Case_Fragment;
import com.laapata.findmissingperson.Dashboard.Fragments.Mp_Case_Fragment;
import com.laapata.findmissingperson.Helper.UserLastSeenTime;
import com.laapata.findmissingperson.ModelClasses.Chat;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.Notification.APIService;
import com.laapata.findmissingperson.Notification.Client;
import com.laapata.findmissingperson.Notification.Token;
import com.laapata.findmissingperson.R;
import com.laapata.findmissingperson.SplashScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  //  BottomNavigationView navigation ;
    private FirebaseAuth auth ;
    private APIService apiService;
    CircleImageView profile_img_drawer_user;
    private String currentUserId ;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    TextView title_tv_drawer_user,email_tv_drawer_user;
    private NavigationView navigationView;
    CardView btnmissingperson,btnaddcase,btnstats,btncomplaints,btn5Police,btn6_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.customedashbaord );
       // navigation = findViewById(R.id.bottom_navigation_id);
        auth = FirebaseAuth.getInstance();
        profile_img_drawer_user=findViewById( R.id.profile_img_drawer_user );
        currentUserId = auth.getCurrentUser().getUid();
        toolbar =findViewById(R.id.toolbar_dashboard);
        email_tv_drawer_user=findViewById( R.id.email_tv_drawer_user );
        title_tv_drawer_user=findViewById( R.id.title_tv_drawer_user );
        drawerLayout =findViewById(R.id.drawer_layout_id);
        navigationView =findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        btnmissingperson=findViewById( R.id.btnmissingperson);
        btnaddcase=findViewById( R.id.btnaddcase );
        //btncomplaints=findViewById( R.id.btncomplaints);
        //btnstats=findViewById( R.id.btnstats);
        btn5Police=findViewById( R.id.btn5Police);
        btn6_scan=findViewById( R.id.btn6_scan);
        toggleDrawer();

        btnmissingperson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      //          Toast.makeText( getApplicationContext(),"Missin person",Toast.LENGTH_SHORT ).show();
                    Intent intent=new Intent(MainActivity.this, CasesViewActivity.class );
                     startActivity(intent);
            }
        } );
        btnaddcase.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TwoButtonActivity.class);
                startActivity(intent);
       //         Toast.makeText( getApplicationContext(),"cases",Toast.LENGTH_SHORT ).show();

            }
        } );
//        btnstats.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent( getApplicationContext(), StatsViewActivity.class );
//                startActivity(intent);
//      //          Toast.makeText( getApplicationContext(),"stats",Toast.LENGTH_SHORT ).show();
//            }
//        } );
//        btncomplaints.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent( getApplicationContext(), MessageActivity.class);
//                intent.putExtra( "cruserid","Si5Iu2YcYeW9PCWahs8I5yNOfLG2");
//                startActivity(intent);
////                Toast.makeText( getApplicationContext(),"comaplaints",Toast.LENGTH_SHORT ).show();
//
//            }
//        } );

        btn5Police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FindPoliceStationAct.class);
                startActivity(intent);
            }
        });

        btn6_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FaceRecognizeActivity.class);
                startActivity(intent);
            }
        });
        /*....................*/
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_List").child(userId);
//        try {
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()){
//                        UsersData data = dataSnapshot.getValue(UsersData.class);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) { }});
//        } catch (Exception e) { e.printStackTrace(); }


        updateToken( FirebaseInstanceId.getInstance().getToken());
       // apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String userid = firebaseUser.getUid();
        final String userEmail = firebaseUser.getEmail();

        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference( "AllUsersAccount" ).child( userid ).child( "Profile_Info" );
        try {
            ref2.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UsersData data = dataSnapshot.getValue( UsersData.class );
                        title_tv_drawer_user.setText( data.getFull_name() );
                        email_tv_drawer_user.setText( data.getEmail());

                        try {
                            Glide.with( getApplicationContext() ).load( data.getImgUrl() ).placeholder( R.drawable.sample ).into( profile_img_drawer_user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText( getApplicationContext(), "Oops ! Some Network Issue.", Toast.LENGTH_SHORT ).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            } );
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int unread = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chat chat = snapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(auth.getCurrentUser().getUid()) && !chat.isIsseen()){
                            unread++;
                        }
                    }

                    if (unread == 0){
                        //navigation.getMenu().getItem(1).setIcon(R.drawable.chat_white);
//                        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                    } else {
                        //navigation.getMenu().getItem(1).setIcon(R.drawable.plus_one_white);
                        //navigation.getMenu().getItem(1).setTitle(("("+unread+") Chats"));
//                        viewPagerAdapter.addFragment(new ChatsFragment(), "("+unread+") Chats");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }


    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            return true;
        }
        return false ;
    }

    Long firstClick = 1L;
    Long secondClick = 0L;
/*
    @Override
    public void onBackPressed() {
        secondClick = System.currentTimeMillis();
        if ((secondClick - firstClick) / 1000 < 2) {
            super.onBackPressed();
        } else {
            firstClick = System.currentTimeMillis();
            Toast.makeText(Dashboard.this, "Touch again to exit", Toast.LENGTH_SHORT).show();
        }
    }*/
    /*...................*/
///////////////////////////////////////////////////////////////////////////////////////////////

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        if (auth.getCurrentUser()!=null) {
            reference.child(auth.getCurrentUser().getUid()).setValue(token1);
        }
    }
    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    /*...............*/

    private void status(String status){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_List").child(id);
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users_List").child(currentUserId);
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(currentUserId).child("Profile_Info");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

//        reference.updateChildren(hashMap);
        ref3.updateChildren(hashMap);
        ref1.updateChildren(hashMap);


    }
    private void toggleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen( GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            secondClick = System.currentTimeMillis();
            if ((secondClick - firstClick) / 1000 < 2) {
                super.onBackPressed();
            } else {
                firstClick = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "Touch again to exit", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.add_credit_id:
                startActivity(new Intent(getApplicationContext(), MyProfile.class));
                closeDrawer();
                break;
            case R.id.chatitem:
                startActivity(new Intent(getApplicationContext(), ChatNewActiivty.class));
                closeDrawer();
                break;
            case R.id.payment_details_id:
                startActivity(new Intent(getApplicationContext(), Your_fp_cases.class));
                closeDrawer();
                break;

            case R.id.resolvedcasesitem:
                startActivity(new Intent(getApplicationContext(), Mp_complete_cases.class));
         //       Toast.makeText( getApplicationContext(),"awais",Toast.LENGTH_SHORT ).show();;
                closeDrawer();
                break;
//            case R.id.policy_id:
//           //     Toast.makeText( getApplicationContext(),"awais",Toast.LENGTH_SHORT ).show();;
//                closeDrawer();
//                break;
            case R.id.logout_id:
                auth.signOut();
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                ((ProgressDialog) dialog).setMessage("Please wait...");
                dialog.show();
                new Handler().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                        startActivity(intent);
                        finish();
                    }
                },1500);

                deSelectCheckedState();
                closeDrawer();
                break;
            case R.id.police_stations:
                deSelectCheckedState();
                closeDrawer();
                Intent intent = new Intent(MainActivity.this, FindPoliceStationAct.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    /**
     * Checks if the navigation drawer is open - if so, close it
     */
    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void deSelectCheckedState() {
        int noOfItems = navigationView.getMenu().size();
        for (int i = 0; i < noOfItems; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
//        currentUser(msg_Recever_Id);
    }

    //
    @Override
    protected void onPause() {
        super.onPause();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_List").child(fuser.getUid());
//        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Doctor_Accounts").child(id).child("Profile_Info");
//        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Patient_Accounts").child(id).child("Profile_Info");
//        if (userType.equals("doctor")){
//            reference.removeEventListener(seenListener);
//            ref1.removeEventListener(seenListener);
//
//        }else if (userType.equals("patient")){
//            reference.removeEventListener(seenListener);
//            ref2.removeEventListener(seenListener);
//        }

        /////////
        Long currentTime = System.currentTimeMillis(); //getting current time in millis
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        String showTime = String.format("%1$tI:%1$tM:%1$tS %1$Tp" + "  ", cal);
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format(timestamp);
        String dateStamp = showTime + dateStr;
        //////////
        UserLastSeenTime lastSeenTime = new UserLastSeenTime();
//        long last_seen = Long.parseLong(lastSeenTime.toString());
        String lastSeenOnScreenTime = lastSeenTime.getTimeAgo(timestamp, getApplicationContext());
        String st = String.valueOf(timestamp);
        status(st);
        currentUser("none");
    }

    /*////////////*/
}

