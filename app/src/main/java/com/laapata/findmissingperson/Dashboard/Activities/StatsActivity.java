package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laapata.findmissingperson.Dashboard.MainActivity;
import com.laapata.findmissingperson.ModelClasses.StatsModel;
import com.laapata.findmissingperson.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatsActivity extends AppCompatActivity {
    EditText edtyear,edtdescription;
    CardView btnsave;;
    CircleImageView backarrowicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stats );
        backarrowicon = findViewById(R.id.backarrowicon);
        backarrowicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
         edtyear=findViewById( R.id.edtyear );
         edtdescription=findViewById( R.id.edtdescription );
         btnsave=findViewById( R.id.btnsave);
         btnsave.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String year=edtyear.getText().toString().trim();
                 String Description=edtdescription.getText().toString().trim();
                 if (year.isEmpty())
                 {
                     edtyear.setError( "Feild is empty" );
                 }
                else if (Description.isEmpty())
                 {
                     edtdescription.setError( "Feild is empty" );
                 }
                else
                 {
                     Long currentTime = System.currentTimeMillis(); //getting current time in millis
                     Calendar cal = Calendar.getInstance();
                     cal.setTimeInMillis(currentTime);
                     String showTime = String.format("%1$tI:%1$tM:%1$tS %1$Tp" + "\n", cal);
                     Date now = new Date();
                     long timestamp = now.getTime();
                     SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                     String dateStr = sdf.format(timestamp);
                     String dateStamp = showTime + dateStr;
                     DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Stats");
                     final String pushKey = ref1.push().getKey();
                     StatsModel statsModel=new StatsModel( year,Description,pushKey,dateStamp);
                     ref1.child( pushKey).setValue(statsModel).addOnCompleteListener( new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Toast.makeText( getApplicationContext(),"Data uploaded",Toast.LENGTH_SHORT ).show();
                             Intent intent=new Intent( getApplicationContext(), StatsViewActivity.class);
                             startActivity( intent );
                             fileList();
                         }
                     } );

                 }
             }
         } );
    }
}
