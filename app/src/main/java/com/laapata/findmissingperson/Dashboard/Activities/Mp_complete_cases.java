package com.laapata.findmissingperson.Dashboard.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.laapata.findmissingperson.Dashboard.Adapters.RecyclerAdapter_Fp_Reports;

import com.laapata.findmissingperson.Dashboard.Adapters.RecyclerAdapter_Mp_complete_cases;
import com.laapata.findmissingperson.Dashboard.Adapters.Your_fp_cases_Adapter;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class Mp_complete_cases extends AppCompatActivity {
            private RecyclerView recyclerView ;
            private RecyclerAdapter_Mp_complete_cases userAdapter;
            private List<FoundPersonModel> mUsers;
            //    private FloatingActionButton fab ;
            private FirebaseAuth auth ;
            private TextView emptyTv ;
            CircleImageView backarrowicon;
    androidx.appcompat.widget.SearchView searchView ;
    RelativeLayout relativeLayout ;
    DatabaseReference ref2;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate( savedInstanceState );
                setContentView( R.layout.activity_mp__report_destail );
                //      fab = findViewById(R.id.fab_add_explore);
                emptyTv = findViewById(R.id.empty_tv_id_explore);
                recyclerView = findViewById(R.id.recyclerview_id_explore);
                auth = FirebaseAuth.getInstance();
                mUsers = new ArrayList<>();
                backarrowicon = findViewById(R.id.backarrowicon);
                backarrowicon.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                } );

                //Display
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setStackFromEnd(true);
                linearLayoutManager.setReverseLayout(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                searchView = findViewById(R.id.searchEt_explore);
                relativeLayout = findViewById(R.id.root_searchview_explore);
                searchView.setQueryHint("search...");
                relativeLayout.requestFocus();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String s) {
                        searchUsers(s);
                        return false;
                    }
                });
    /*    fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Create_Fp_Case.class);
                startActivity(intent);

            }
        });
*/
                loadData();
            }

    private void searchUsers(final String s) {
        Query query = FirebaseDatabase.getInstance().getReference("ResolvedCases").orderByChild("mpname")
                .startAt(s)
                .endAt(s+"\uf8ff");
        ref2.keepSynced(true);
        hideSoftKeyboard();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoundPersonModel user = snapshot.getValue(FoundPersonModel.class);
                    mUsers.add(user);
                }
                userAdapter = new RecyclerAdapter_Mp_complete_cases( Mp_complete_cases.this, mUsers);
                recyclerView.setAdapter(userAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});
    }
    private void hideSoftKeyboard(){
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
            public void loadData() {
//        super.onStart();
                // data is found
                final ProgressDialog dialog = new ProgressDialog( Mp_complete_cases.this);
                dialog.setMessage("Loading Data...");
                dialog.show();

                final String userId = auth.getCurrentUser().getUid();
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userId).child("FoundedPersonsposts");
                ref2 = FirebaseDatabase.getInstance().getReference("ResolvedCases");

                try {
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mUsers.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                FoundPersonModel user = snapshot.getValue(FoundPersonModel.class);
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
                                userAdapter = new RecyclerAdapter_Mp_complete_cases( Mp_complete_cases.this, mUsers);
                                recyclerView.setAdapter(userAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }});
                } catch (Exception e) { e.printStackTrace(); }


////////////////////////////////////////////////////////////////////////////////////////////////////
            }

        }
