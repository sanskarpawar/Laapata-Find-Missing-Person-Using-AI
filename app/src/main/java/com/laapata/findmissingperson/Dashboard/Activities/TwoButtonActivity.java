package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.laapata.findmissingperson.Dashboard.Fragments.CasesViewActivity;
import com.laapata.findmissingperson.Dashboard.Fragments.FP_Case_Fragment;
import com.laapata.findmissingperson.Dashboard.MainActivity;
import com.laapata.findmissingperson.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TwoButtonActivity extends AppCompatActivity {
      CardView reportmissingperson,foundpersson;
      CircleImageView backarrowicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_two_button );
        backarrowicon = findViewById(R.id.backarrowicon);
        backarrowicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        reportmissingperson=findViewById( R.id.reportmissingperson);
        foundpersson=findViewById( R.id.foundaperson);
        foundpersson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( TwoButtonActivity.this, CasesViewActivity.class );
                startActivity(intent);
            }
        } );
        reportmissingperson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Create_Fp_Case.class);
                startActivity(intent);
            }
        } );
    }
}
