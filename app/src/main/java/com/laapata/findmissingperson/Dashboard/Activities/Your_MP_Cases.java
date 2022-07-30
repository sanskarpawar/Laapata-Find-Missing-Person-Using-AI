package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laapata.findmissingperson.Dashboard.Adapters.Your_mp_cases_adapter;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.R;

import java.util.ArrayList;
import java.util.List;

public class Your_MP_Cases extends AppCompatActivity {
    private RecyclerView recyclerView ;
    private Your_mp_cases_adapter userAdapter;
    private List<GigsData> mUsers;

    private FloatingActionButton fab ;
    private FirebaseAuth auth ;
    private TextView emptyTv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_your__m_p__cases );
        fab = findViewById(R.id.fab_add_explore);
        emptyTv =findViewById(R.id.empty_tv_id_explore);
        recyclerView =findViewById(R.id.recyclerview_id_explore);
        auth = FirebaseAuth.getInstance();
        mUsers = new ArrayList<>();
        //Display
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Create_Mp_Case.class);
                startActivity(intent);

            }
        });

   
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }
    public void loadData() {
//        super.onStart();
        // data is found
        final ProgressDialog dialog = new ProgressDialog(Your_MP_Cases.this);
        dialog.setMessage("Loading Data...");
        dialog.show();

        final String userId = auth.getCurrentUser().getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userId).child("MissingPersonPosts");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("MissingUsers");

        try {
            ref1.addValueEventListener(new ValueEventListener() {
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
                        userAdapter = new Your_mp_cases_adapter(Your_MP_Cases.this, mUsers);
                        recyclerView.setAdapter(userAdapter);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }


////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
