package com.laapata.findmissingperson.Dashboard.Fragments;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.laapata.findmissingperson.Dashboard.Activities.Create_Mp_Case;
import com.laapata.findmissingperson.Dashboard.Adapters.RecyclerAdapter_Mp_Reports;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class Mp_Case_Fragment extends Fragment {
    private static FragmentManager fragmentManager ;

    private RecyclerView recyclerView ;
    private RecyclerAdapter_Mp_Reports userAdapter;
    private List<GigsData> mUsers;

    private FloatingActionButton fab ;
    private FirebaseAuth auth ;
    private TextView emptyTv ;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mp_fragment, null);
        fragmentManager = getActivity().getSupportFragmentManager();
        fab = rootView.findViewById(R.id.fab_add_explore);
        emptyTv = rootView.findViewById(R.id.empty_tv_id_explore);
        recyclerView = rootView.findViewById(R.id.recyclerview_id_explore);
        auth = FirebaseAuth.getInstance();
        mUsers = new ArrayList<>();
        //Display
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Create_Mp_Case.class);
                    startActivity(intent);

            }
        });

        loadData();
/////////////////////////
        return rootView ;
    }
    /*..............*/
//    @Override
    public void loadData() {
//        super.onStart();
        // data is found
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading Data...");
        dialog.show();

        final String userId = auth.getCurrentUser().getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userId).child("MissingPersonPosts");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("MissingUsers");

            try {
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mUsers.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            GigsData user = snapshot.getValue(GigsData.class);
                            mUsers.add(user);

                        }
                        if (mUsers.isEmpty()) {
                            dialog.dismiss();
                            recyclerView.setVisibility(View.INVISIBLE);
                            emptyTv.setVisibility(View.VISIBLE);
                        } else {
                            dialog.dismiss();
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTv.setVisibility(View.INVISIBLE);
                            userAdapter = new RecyclerAdapter_Mp_Reports(getActivity(), mUsers);
                            recyclerView.setAdapter(userAdapter);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }});
            } catch (Exception e) { e.printStackTrace(); }


////////////////////////////////////////////////////////////////////////////////////////////////////
    }


}
