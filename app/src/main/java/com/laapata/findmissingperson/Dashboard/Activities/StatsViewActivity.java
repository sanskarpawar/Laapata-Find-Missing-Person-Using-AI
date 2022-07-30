package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laapata.findmissingperson.Dashboard.Adapters.RecyclerAdapter_Fp_Reports;
import com.laapata.findmissingperson.Dashboard.Adapters.StatsAdpter;
import com.laapata.findmissingperson.Dashboard.Fragments.CasesViewActivity;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.ModelClasses.StatsModel;
import com.laapata.findmissingperson.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatsViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    StatsAdpter statsAdpter;
    TextView tvadminstate;
    List<StatsModel> statsModels;
    CircleImageView backarrowicon;
    private FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stats_view );
        recyclerView = findViewById(R.id.recyclerview_id_explore);
        tvadminstate=findViewById( R.id.tvadminstate );
        statsModels=new ArrayList<>(  );
        backarrowicon = findViewById(R.id.backarrowicon);
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        backarrowicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
      /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StatsViewActivity.this,RecyclerView.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);*/
        recyclerView.setHasFixedSize(true);
        loadData();
        if (userId.equals( "Si5Iu2YcYeW9PCWahs8I5yNOfLG2"))
        {
            tvadminstate.setVisibility( View.VISIBLE );
        }
        else if (userId.equals("bbqF50A17qeq5ywFcYI0Uqrjppx2"))
        {
            tvadminstate.setVisibility( View.VISIBLE);
        }
        tvadminstate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getApplicationContext(),StatsActivity.class );
                startActivity(intent);
            }
        } );
    }
    public void loadData() {
//        super.onStart();
        // data is found
        final ProgressDialog dialog = new ProgressDialog( StatsViewActivity.this);
        dialog.setMessage("Loading Data...");
        dialog.show();

        final DatabaseReference ref2 =  FirebaseDatabase.getInstance().getReference("Stats");

        try {
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    statsModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        StatsModel model = snapshot.getValue(StatsModel.class);
                        statsModels.add(model);

                    }
                    if (statsModels.isEmpty()) {
                        dialog.dismiss();
                        recyclerView.setVisibility( View.INVISIBLE);
                    //    emptyTv.setVisibility(View.VISIBLE);
                    } else {
                        dialog.dismiss();
                        recyclerView.setVisibility(View.VISIBLE);
                     //   emptyTv.setVisibility(View.INVISIBLE);
                        statsAdpter = new StatsAdpter(StatsViewActivity.this, statsModels);
                        recyclerView.setAdapter(statsAdpter);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }});
        } catch (Exception e) { e.printStackTrace(); }


////////////////////////////////////////////////////////////////////////////////////////////////////
    }

}
