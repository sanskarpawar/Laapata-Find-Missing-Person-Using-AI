package com.laapata.findmissingperson.Dashboard.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fp_Report_Details extends AppCompatActivity {
    private TextView mpname,mpheight, mpfathername, mpplace, mppermanentadress, mpcontactnumber,edtmpage;
    private Button saveBtn ;
    private FirebaseAuth auth ;
    String caseid;
    String cruserid;
    RelativeLayout videoplayer;
    VideoView mpvedio;
    String vedioLink;
    Uri vedioUri;
    ImageView mapbtn;
    String userpermanentadress;
    MediaController mediaController;
    CircleImageView mpimage,btncall,btnmessage,btnplay;
    ProgressBar  progressBarLandScape;
    CircleImageView backarrowicon;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fp__report__details );
        backarrowicon = findViewById(R.id.backarrowicon);
        backarrowicon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        mpname = findViewById(R.id.edtmpname);
        mpfathername = findViewById(R.id.edtmpfathername);
        mpheight = findViewById(R.id.edtmpheight);
        edtmpage = findViewById(R.id.edtmpage);
        mpplace = findViewById(R.id.edtmpplace);
        mppermanentadress = findViewById(R.id.edtmppadress);
        mpcontactnumber = findViewById(R.id.edtmpcontactnumber);
        saveBtn = findViewById(R.id.save_btn_create_gig);
        mpimage=findViewById( R.id.mpimage );
        btncall=findViewById( R.id.btncall );
        btnmessage=findViewById(R.id.btnchatmessage);
        mapbtn=findViewById( R.id.mapbtn );
        progressBarLandScape=findViewById( R.id.progressBarLandScape );
        auth = FirebaseAuth.getInstance();
        ///////
        btnplay=findViewById( R.id.btnplay);
        videoplayer=findViewById( R.id.videoplayer );
        mpvedio=findViewById( R.id.mpvedio);
    //    Toast.makeText( getApplicationContext(),vedioLink,Toast.LENGTH_SHORT ).show();

        btnplay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vedioUri= Uri.parse(vedioLink);
                mpvedio.setVideoURI(vedioUri);
                mpvedio.requestFocus();
                mpvedio.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setOnVideoSizeChangedListener( new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                mediaController=new MediaController( Fp_Report_Details.this);
                                mpvedio.setMediaController(mediaController);
                                mediaController.setAnchorView(mpvedio);

                            }
                        } );
                    }
                } );
                mpvedio.start();
                btnplay.setVisibility( View.GONE);
                //progressBarLandScape.setVisibility( View.VISIBLE);
            }
        } );

        mpvedio.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == mp.MEDIA_INFO_BUFFERING_END) {
                    progressBarLandScape.setVisibility(View.GONE);
                    btnplay.setVisibility(View.GONE);
                }
                else if(what == mp.MEDIA_INFO_BUFFERING_START){
                    progressBarLandScape.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        btnmessage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getApplicationContext(), MessageActivity.class);
                intent.putExtra( "cruserid",cruserid);
                startActivity(intent);
            }

        } );
        btncall.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number =mpcontactnumber.getText().toString().trim();
                Log.i("Make call", "");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                try {
                    startActivity(intent);
                    Log.i("Finished making a call", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        } );
        mapbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc =userpermanentadress;
                Geocoder geocoder = new Geocoder(getApplicationContext());
                List<Address> list = new ArrayList<>();
                try {
                    list = geocoder.getFromLocationName(loc, 1);
                } catch (IOException e) {
                    Log.e("Fp Details", "geoLocate: IOException: " + e.getMessage() );
                }
                if (list.size() > 0) {
                    Address address = list.get(0);
                    Log.d("Fp Details", "geoLocate: found a location: " + address.toString());
                    loc = address.getAddressLine(0);
                    Toast.makeText(getApplicationContext(), loc, Toast.LENGTH_SHORT).show();
                    final String fLoc = loc ;
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+fLoc);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        } );
    }
    @Override
    protected void onStart() {
        super.onStart();
        caseid=getIntent().getStringExtra( "caseid");
        String status=getIntent().getStringExtra( "completed");
        if (status!=null)
        {
            final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("ResolvedCases").child(caseid);
            try {
                ref2.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FoundPersonModel mpdata = dataSnapshot.getValue(FoundPersonModel.class);
                        mpname.setText( mpdata.getMpname());
                        mpfathername.setText( mpdata.getMpfathername());
                        mpheight.setText( mpdata.getMpheight() );
                        edtmpage.setText( mpdata.getMpage());
                        mpplace.setText( mpdata.getMpplace());
                        mppermanentadress.setText( mpdata.getMppermanentadress());
                        userpermanentadress=mpdata.getMppermanentadress();
                        mpcontactnumber.setText(mpdata.getMpcontactnumber());
                        vedioLink=mpdata.getMpvedio();

                        if (vedioLink!=null)
                        {
                            videoplayer.setVisibility( View.VISIBLE);

                        }
                        cruserid=mpdata.getCrid();
                        Glide.with(getApplicationContext()).load(mpdata.getMpimage()).placeholder(R.drawable.sample).into(mpimage);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference( "FoundedPersonsCases" ).child( caseid );
            try {
                ref2.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FoundPersonModel mpdata = dataSnapshot.getValue( FoundPersonModel.class );
                        if(mpdata!=null)
                        {

                            mpname.setText(mpdata.getMpname() );
                            mpfathername.setText( mpdata.getMpfathername() );
                            mpheight.setText( mpdata.getMpheight() );
                            edtmpage.setText( mpdata.getMpage() );
                            mpplace.setText( mpdata.getMpplace() );
                            mppermanentadress.setText( mpdata.getMppermanentadress() );
                            userpermanentadress = mpdata.getMppermanentadress();
                            mpcontactnumber.setText( mpdata.getMpcontactnumber() );
                            vedioLink = mpdata.getMpvedio();

                            if (vedioLink != null) {
                                videoplayer.setVisibility( View.VISIBLE );

                            }
                            cruserid = mpdata.getCrid();
                            Glide.with( getApplicationContext() ).load( mpdata.getMpimage() ).placeholder( R.drawable.sample ).into( mpimage );

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
