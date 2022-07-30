package com.laapata.findmissingperson.Dashboard.Fragments;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.laapata.findmissingperson.Dashboard.Adapters.UserAdapter;
import com.laapata.findmissingperson.ModelClasses.Chatlist;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.Notification.Token;
import com.laapata.findmissingperson.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
public class ChatFragment extends Fragment {
    private static FragmentManager fragmentManager ;
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

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_fragment, null);
        fragmentManager = getActivity().getSupportFragmentManager();

        recyclerView = view.findViewById(R.id.recyclerview_id_chat_fragment);
        fab = view.findViewById(R.id.fab_add_chat_fragment);
        emptyTv = view.findViewById(R.id.empty_tv_id_chat_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        usersList = new ArrayList<>();
        /*....*/
        fab.setVisibility(View.GONE);
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.patientColorMain)));

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

        updateToken(FirebaseInstanceId.getInstance().getToken());


/////////////////////////
        return view ;
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
                                userAdapter = new UserAdapter(getActivity(), mUsers, true);
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
                        userAdapter = new UserAdapter(getActivity(), mUsers, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }
/*...............*/
private void currentUser(String userid){
    SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREFS", MODE_PRIVATE).edit();
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
                        userAdapter = new UserAdapter(getActivity(), mUsers, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }
    }

}

