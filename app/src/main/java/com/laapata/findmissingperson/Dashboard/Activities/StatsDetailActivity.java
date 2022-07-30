package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.laapata.findmissingperson.R;

public class StatsDetailActivity extends AppCompatActivity {
    TextView tvdescription,tvyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stats_detail );
        Toolbar toolbar = findViewById(R.id.toolbar_dashboard);
        toolbar.setTitle("Statistics");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvdescription=findViewById( R.id.tvdescription);
        tvyear=findViewById( R.id.tvyear);
        tvdescription.setText( getIntent().getStringExtra( "description"));
        tvyear.setText( getIntent().getStringExtra( "tvyear"));

    }
}
