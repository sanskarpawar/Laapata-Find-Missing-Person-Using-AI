package com.laapata.findmissingperson.Dashboard.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.laapata.findmissingperson.Dashboard.Fragments.CasesViewActivity;
import com.laapata.findmissingperson.Helper.ImagePickerActivity;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.ModelClasses.GigsData;
import com.laapata.findmissingperson.Notification.APIService;
import com.laapata.findmissingperson.Notification.Client;
import com.laapata.findmissingperson.Notification.Data;
import com.laapata.findmissingperson.Notification.MyResponse;
import com.laapata.findmissingperson.Notification.MySingleton;
import com.laapata.findmissingperson.Notification.Sender;
import com.laapata.findmissingperson.Notification.Token;
import com.laapata.findmissingperson.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class Create_Fp_Case extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 100;
    public static final int PICK_VEDIO_CODE = 3;
    private EditText mpname,mpheight, mpfathername, mpplace, mppermanentadress, mpcontactnumber,edtmpage;
    private Button saveBtn ;
    private FirebaseAuth auth ;
    ImageView mpimage;
    StorageReference storageReference,storageReference2;
    Uri imageUri ,vedioUri;
    Uri downloadUri;
    String imgUrlStr,vedioUrlstr;
    Button mpbtnupload,btnuploadimage;
    VideoView mpvedio;
    String permanenetadress;
    MediaController mediaController;
    CircleImageView backarrowicon;
    ////////
    APIService apiService;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAI4E1fw8:APA91bEJ7OJJEqpXwXemU_UILyeqe1uqkQU5SAsY1bkdPolLISI2CZgY4X-83BGeKeYoGuKVq1ydQzw27afhLXrDTc26r7ho4Dp9OmcN_LCY2xiuejm10HA7gykFvPSZ_sMX2iW2gixG";
    // final private String serverKey = "key=" + "AAAAHrEsq44:APA91bF8KKcWVLG5kLOqVJY5ru69Tqf-758w5j61vgAFgbvsSUASt72LDKG8v0W1E84y12-wCykEuq1xN_L68U3kbNN3A_NAsxbWcHjn7NSzf8FVODuT4mVYNCHiVTFLtkbghJyBZFdb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.doctorColorMain));

        }
        setContentView(R.layout.activity_create_fp_cases );
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
        mpimage=findViewById( R.id.mpimage);
        mpbtnupload=findViewById( R.id.mpbtnupload );
        btnuploadimage=findViewById( R.id.btnuploadimage );
//        mpvedio=findViewById( R.id.mpvedio );
        auth = FirebaseAuth.getInstance();
        //apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        ///////
        permanenetadress=getIntent().getStringExtra("getlocation");
        if (permanenetadress!=null) {
            mpname.setText( getIntent().getStringExtra( "fpname" ) );
            mpfathername.setText( getIntent().getStringExtra( "fmpfathername" ) );
            mpheight.setText( getIntent().getStringExtra( "fmpheight" ) );
            edtmpage.setText( getIntent().getStringExtra( "fmpage" ) );
            mpplace.setText( getIntent().getStringExtra( "fmpplace" ) );
            mpcontactnumber.setText( getIntent().getStringExtra( "fmpcontactnumber" ) );
            mppermanentadress.setText( getIntent().getStringExtra( "address" ) );
            imgUrlStr = getIntent().getStringExtra( "imgUrlStr" );
            vedioUrlstr = getIntent().getStringExtra( "vedioUrlstr" );
        }
        ////
        mppermanentadress.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getApplicationContext(),UpheavMapActivity.class );
                String fmpname       = mpname.getText().toString();
                String fmpfathername    = mpfathername.getText().toString();
                String fmpheight        = mpheight.getText().toString();
                String fmpage  =         edtmpage.getText().toString();
                String fmpplace  = mpplace.getText().toString();
                String fmpcontactnumber  = mpcontactnumber.getText().toString();
                intent.putExtra( "fpname",fmpname);
                intent.putExtra( "fmpfathername",fmpfathername);
                intent.putExtra( "fmpheight",fmpheight);
                intent.putExtra( "fmpage",fmpage);
                intent.putExtra( "fmpplace",fmpplace);
                intent.putExtra( "fmpcontactnumber",fmpcontactnumber);
                intent.putExtra( "imageuri",imgUrlStr);
                intent.putExtra( "vedioUrlstr",vedioUrlstr);
                intent.putExtra( "getlocation","permanenetadress");
                startActivity( intent);
                finish();
            }
        } );
        storageReference2 = FirebaseStorage.getInstance().getReference("Vedios");
//        mpvedio.setOnPreparedListener( new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setOnVideoSizeChangedListener( new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        mediaController=new MediaController( Create_Fp_Case.this);
//                        mpvedio.setMediaController(mediaController);
//                        mediaController.setAnchorView(mpvedio);
//
//                    }
//                } );
//            }
//        } );
//        mpvedio.start();

//        mpbtnupload.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadvedio();
//            }
//        } );
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        btnuploadimage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity( Create_Fp_Case.this)
                        .withPermissions(Manifest.permission.CAMERA)
                       // , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                } else if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        } );
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAppointment();
            }
        });


    }
    public void uploadvedio()
    {
        Intent intent=new Intent(  );
        intent.setType( "video/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT);
        startActivityForResult( Intent.createChooser( intent,"Select a video"),PICK_VEDIO_CODE);
    }


    /*......Create Gig Database Method.......*/
    public void CreateAppointment(){
        String fmpname       = mpname.getText().toString();
        String fmpfathername    = mpfathername.getText().toString();
        String fmpheight        = mpheight.getText().toString();
        String fmpage  =         edtmpage.getText().toString();
        String fmpplace  = mpplace.getText().toString();
        String fmppermanentadre  = mppermanentadress.getText().toString();
        String fmpcontactnumber  = mpcontactnumber.getText().toString();
        double latitude=getIntent().getDoubleExtra( "latitude",0);
        double longitude=getIntent().getDoubleExtra( "longitude",0 );
        double platitude=0;
        double plongitude=0;
        /*.......................................*/
        Long currentTime = System.currentTimeMillis(); //getting current time in millis
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        String showTime = String.format("%1$tI:%1$tM:%1$tS %1$Tp" + "\n", cal);
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format(timestamp);
        String dateStamp = showTime + dateStr;
        /*.........................................*/
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userId).child("FoundedPersonsposts");
        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FoundedPersonsCases");
        final String pushKey = ref1.push().getKey();

        if (TextUtils.isEmpty(fmpname)){
            mpname.setError("Empty");
        }else if (TextUtils.isEmpty(fmpfathername)){
            mpfathername.setError("Empty");
        }else if (TextUtils.isEmpty(fmpheight)){
            mpheight.setError("Empty");
        }
        else if (TextUtils.isEmpty(fmpage)){
            edtmpage.setError("Empty");
        }
        else if (TextUtils.isEmpty(fmpplace)){
            mpplace.setError("Empty");
        }else if (TextUtils.isEmpty(fmppermanentadre)){
            mppermanentadress.setError("Empty");
        }else if (TextUtils.isEmpty(fmpcontactnumber)) {
            mpcontactnumber.setError( "Empty" );
        }
        else if (imgUrlStr==null)
        {
            Toast.makeText( getApplicationContext(),"Please upload the Image",Toast.LENGTH_SHORT ).show();
        }
        else {
            // data is found
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Preparing...");
            dialog.show();
            /*..........*/
            final FoundPersonModel data = new FoundPersonModel(
                    pushKey, userId,"", dateStamp, fmpname, fmpfathername, fmpheight, fmpage, fmpplace,latitude,longitude,platitude,plongitude,
                    fmppermanentadre, fmpcontactnumber, imgUrlStr,vedioUrlstr
            );
            ref1.child(pushKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        ref2.child(pushKey).setValue(data);
                        dialog.dismiss();
                 //       onBackPressed();
                        TOPIC = "/topics/userABC"; //topic has to match what the receiver subscribed to
                        NOTIFICATION_TITLE = "New case reported";;
                        NOTIFICATION_MESSAGE = "New case of "+mpname.getText().toString().trim()+" is reported";
                        JSONObject notification = new JSONObject();
                        JSONObject notifcationBody = new JSONObject();
                        System.out.println(imgUrlStr+ "gggggggggggggggggggggggggggggggggggggggggggg");
                        try {
                            notifcationBody.put("title", NOTIFICATION_TITLE);
                            notifcationBody.put("message", NOTIFICATION_MESSAGE);
                            notifcationBody.put("image-url", imgUrlStr);
                            notifcationBody.put("notify", "notify");
                            notification.put("to", TOPIC);
                            notification.put("data", notifcationBody);
                        } catch (JSONException e) {
                            Log.e(TAG, "onCreate: " + e.getMessage());
                        }
                        sendNotification(notification);
                        Intent intent=new Intent( getApplicationContext(), CasesViewActivity.class );
                        startActivity( intent );
                        finish();
                        Toast.makeText( Create_Fp_Case.this, "Case Successfully Created", Toast.LENGTH_SHORT).show();
                    }else {
                        dialog.dismiss();
                        Toast.makeText( Create_Fp_Case.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i( TAG, "onResponse: " + response.toString() );
                        // edtTitle.setText("");
                        //edtMessage.setText( "" );
                   //     Toast.makeText(Create_Fp_Case.this, "NOTI SEND", Toast.LENGTH_LONG).show();
                    }
                },

                (VolleyError error) -> {
                    Toast.makeText(Create_Fp_Case.this, "Some Network Problem", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

  /*  private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    String userId = auth.getCurrentUser().getUid();
                    Data data = new Data(userId, R.mipmap.ic_launcher, username+": "+message, "New Message",
                            msg_Recever_Id,"", "msg",0);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(Create_Fp_Case.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
  */  //==================================================================================================
//.................................... img dialog ..................................................
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }
    //.............................. choose from Gallery ...............................................
    private void launchGalleryIntent() {
        Intent intent = new Intent( Create_Fp_Case.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //............................... choose from Camera ...............................................
    private void launchCameraIntent() {
        Intent intent = new Intent( Create_Fp_Case.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //.................................
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( Create_Fp_Case.this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Create_Fp_Case.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }
    //......................... On Activity Result............................
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode==PICK_VEDIO_CODE && resultCode==RESULT_OK && data!=null)
//        {
//            vedioUri=data.getData();
//            mpvedio.setVideoURI(vedioUri);
//            Uploadvideotofirebase(vedioUri);
//        }

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getParcelableExtra("path");
                imgUrlStr = imageUri.toString();
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Glide.with(getApplicationContext()).load(bitmap).into(mpimage);
                    uploadImage();
                    Toast.makeText(getApplicationContext(), imageUri.toString()+"imgruri", Toast.LENGTH_SHORT).show();


                    // loading profile image from local cache
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void Uploadvideotofirebase(Uri vedioUri) {
        final ProgressDialog progressDialog=new ProgressDialog( Create_Fp_Case.this);
        progressDialog.setTitle( "Uploading video..." );
        progressDialog.show();

        final StorageReference fileReference = storageReference2.child(System.currentTimeMillis()
                + "." + getFileExtension(vedioUri));
        StorageTask<UploadTask.TaskSnapshot> uploadTask = fileReference.putFile(vedioUri);
        uploadTask.addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        } ).addOnProgressListener( new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText( getApplicationContext(),"Progress",Toast.LENGTH_SHORT ).show();
                double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage( "Upload"+(int)progress+"%");

            }
        } ).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    vedioUrlstr = downloadUri.toString();
                    progressDialog.dismiss();
                    Toast.makeText( getApplicationContext(),"Vedio uploaded successfully",Toast.LENGTH_SHORT ).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText( Create_Fp_Case.this.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText( Create_Fp_Case.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage() {
        if (imageUri != null) {
            final Dialog dialogImg = new ProgressDialog(this);
            ((ProgressDialog) dialogImg).setMessage("Uploading ...");
            dialogImg.setCancelable(false);
            dialogImg.show();
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            StorageTask<UploadTask.TaskSnapshot> uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        dialogImg.dismiss();
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        dialogImg.dismiss();
                        downloadUri = task.getResult();
                        imgUrlStr = downloadUri.toString();
                        
                     /*   String id = auth.getCurrentUser().getUid();
                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(id).child("Profile_Info");
                        final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users_List").child(id);
                        final HashMap map = new HashMap();
                        map.put("imgUrl", imgUrlStr);

//                        ref.updateChildren(map);

                        ref2.updateChildren(map);
                        ref3.updateChildren(map);
                        dialogImg.dismiss();
                        Toast.makeText(Create_Fp_Case.this.getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
*/
                    } else {
                        dialogImg.dismiss();
                        Toast.makeText( Create_Fp_Case.this.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogImg.dismiss();
                    Toast.makeText( Create_Fp_Case.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
