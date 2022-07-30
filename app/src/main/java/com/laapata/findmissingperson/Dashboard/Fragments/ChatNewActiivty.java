package com.laapata.findmissingperson.Dashboard.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.laapata.findmissingperson.Dashboard.Adapters.UserAdapter;
import com.laapata.findmissingperson.ModelClasses.Chatlist;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.Notification.Token;
import com.laapata.findmissingperson.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatNewActiivty extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<UsersData> mUsers;
    private TextView emptyTv ;

    private FirebaseUser fuser;
    private DatabaseReference reference;
    FloatingActionButton fab ;
    private FirebaseAuth auth ;
    DatabaseReference refStatus ;
    //    ValueEventListener seenListener;
    private List<Chatlist> usersList;
    CircleImageView backarrowicon;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat_new_actiivty );
        recyclerView = findViewById(R.id.recyclerview_id_chat_fragment);
        fab = findViewById(R.id.fab_add_chat_fragment);
        emptyTv = findViewById(R.id.empty_tv_id_chat_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        backarrowicon = findViewById(R.id.backarrowicon);
        backarrowicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        usersList = new ArrayList<>();
        /*....*/
        fab.setVisibility( View.GONE);
        fab.setBackgroundTintList( ColorStateList.valueOf( ContextCompat.getColor(getApplicationContext(), R.color.patientColorMain)));

        ////////////////////

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(auth.getCurrentUser().getUid());
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usersList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chatlist chatlist = snapshot.getValue(Chatlist.class);
                        usersList.add(chatlist);
                    }

                    chatList();

//                    if (userType.equals("doctor")){
//                        chatListDoctors();
//                    }else if (userType.equals("patient")){
//                        chatListPatients();
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }

        updateToken( FirebaseInstanceId.getInstance().getToken());

    }

    /*..............*/
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatListDoctors() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users_List");
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UsersData user = snapshot.getValue(UsersData.class);
                        if (user.getUserType().equals("patient")){
                            for (Chatlist chatlist : usersList){
                                try {
                                    if (user.getIdPush().equals(chatlist.getId())){
                                        mUsers.add(user);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (mUsers.isEmpty()) {
                                recyclerView.setVisibility(View.INVISIBLE);
                                emptyTv.setVisibility(View.VISIBLE);
                            } else {

                                recyclerView.setVisibility(View.VISIBLE);
                                emptyTv.setVisibility(View.INVISIBLE);
                                userAdapter = new UserAdapter(ChatNewActiivty.this, mUsers, true);
                                recyclerView.setAdapter(userAdapter);
                            }
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }
    /*.....*/
    private void chatListPatients() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users_List");
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UsersData user = snapshot.getValue(UsersData.class);
                        if (user.getUserType().equals("doctor")){
                            for (Chatlist chatlist : usersList){
                                try {
                                    if (user.getIdPush().equals(chatlist.getId())){
                                        mUsers.add(user);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }}
                    }
                    if (mUsers.isEmpty()) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        emptyTv.setVisibility(View.VISIBLE);
                    } else {

                        recyclerView.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.INVISIBLE);
                        userAdapter = new UserAdapter(ChatNewActiivty.this, mUsers, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }
    /*...............*/
    private void currentUser(String userid){
        SharedPreferences.Editor editor = ChatNewActiivty.this.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    /*.....*/
    private void chatList() {
        mUsers = new ArrayList<>();
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users_List");
//        reference = FirebaseDatabase.getInstance().getReference("Users_List");
        try {
            ref3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UsersData user = snapshot.getValue(UsersData.class);
                        for (Chatlist chatlist : usersList){
                            try {
                                if (user.getIdPush().equals(chatlist.getId())){
                                    mUsers.add(user);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (mUsers.isEmpty()) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        emptyTv.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.INVISIBLE);
                        userAdapter = new UserAdapter(ChatNewActiivty.this, mUsers, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }

}


