package com.laapata.findmissingperson.Dashboard.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.laapata.findmissingperson.Helper.ImagePickerActivity;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
public class MyProfile extends AppCompatActivity {
    private static final String TAG = MyProfile.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    Button saveBtn ;
    EditText nameEt, AddresEt, phoneEt, cityEt ;
    TextView titleTv, emailTv, dateTv ;
    ImageView profileImg ;
    LinearLayout linearLayout ;
    FirebaseDatabase database;
    FirebaseAuth auth ;
    StorageReference storageReference;
    Uri imageUri ;
    Uri downloadUri ;
    String dateStampStr ;
    String nameStr ;
    String adresStr ;
    String phoneStr ;
    String cityStr ;
    String imgUrlStr ;
    String searchStr ;
    String isVerifiedStr ;
    String userTypeStr ;
    String userType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        Toolbar toolbar = findViewById(R.id.toolbar_id_my_profile);
        RelativeLayout relativeLayout = findViewById(R.id.relative_id_my_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                intent.putExtra("userType", userType);
//                startActivity(intent);
            }});
        saveBtn = findViewById(R.id.save_btn_my_profile);




        titleTv  = findViewById(R.id.title_tv_my_profile);
        emailTv  = findViewById(R.id.email_tv_my_profile);
        nameEt  = findViewById(R.id.name_et_my_profile);
        phoneEt = findViewById(R.id.phone_et_my_profile);
        cityEt = findViewById(R.id.city_et_my_profile);
        AddresEt  = findViewById(R.id.address_et_my_profile);
        dateTv = findViewById(R.id.date_stamp_my_profile);
        profileImg = findViewById(R.id.pro_img_my_profile);

        linearLayout   = findViewById(R.id.linear_root_my_profile);
        linearLayout.requestFocus();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance() ;
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Toast.makeText(getApplicationContext(), "email can't be changed", Toast.LENGTH_SHORT).show(); }});

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Button Pressed"+"pppppppppppppppppppppppppppppppppppppppppppppppp");
                String name   = nameEt.getText().toString();
                String adres  = AddresEt.getText().toString();
                String phone  = phoneEt.getText().toString();
                String city   = cityEt.getText().toString();

                if (TextUtils.isEmpty(name)){
                    nameEt.setError("field empty");
                }else {
                    updateData(name, adres, phone, city);
                }
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MyProfile.this)
                        .withPermissions(Manifest.permission.CAMERA)
                        //, Manifest.permission.WRITE_EXTERNAL_STORAGE
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
        });

    }
    //============ Onstart method
    @Override
    public void onStart() {
        super.onStart();
        // data is found
        final Dialog dialog = new ProgressDialog( this );
        ((ProgressDialog) dialog).setMessage( "Loading data..." );
        dialog.show();
        database = FirebaseDatabase.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String userid = firebaseUser.getUid();
        final String userEmail = firebaseUser.getEmail();

        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference( "AllUsersAccount" ).child( userid ).child( "Profile_Info" );
        try {
            ref2.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UsersData data = dataSnapshot.getValue( UsersData.class );
                        dateStampStr = data.getAccounnt_created();
                        imgUrlStr = data.getImgUrl();
                        emailTv.setHint( userEmail );
                        dateTv.setText( "Join on \n" + data.getAccounnt_created() );
                        titleTv.setText( data.getFull_name() );
                        nameEt.setText( data.getFull_name() );
                        phoneEt.setText( data.getPhone() );
                        cityEt.setText( data.getCity() );
                        AddresEt.setText( data.getAddress() );
                        try {
                            Glide.with( getApplicationContext() ).load( data.getImgUrl() ).placeholder( R.drawable.sample ).into( profileImg );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText( MyProfile.this, "Oops ! Some Network Issue.", Toast.LENGTH_SHORT ).show();
                        dialog.dismiss();
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
    //============ Update Data Method
    public void updateData(String name, String adres, String  phone, String  city){
            // data is found
            final Dialog getDialog = new ProgressDialog(MyProfile.this);
            ((ProgressDialog) getDialog).setMessage("Updating profile...");
            getDialog.setCancelable(false);
            getDialog.show();

            FirebaseUser firebaseUser = auth.getCurrentUser();
            String useridStr = firebaseUser.getUid();
            String emailStr = firebaseUser.getEmail();

//            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_List").child(useridStr);
            final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users_List").child(useridStr);
            final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference( "AllUsersAccount" ).child(useridStr).child( "Profile_Info");
            final UsersData data = new UsersData(
                    useridStr,
                    dateStampStr,
                    name,
                    emailStr,
                    phone,
                    city,
                    adres,
                    imgUrlStr,
                    name.toLowerCase(),
                    userTypeStr,
                    "",
                    0,0
            );


                ref2.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            ref.setValue(data);
                            ref3.setValue(data);
                            getDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            getDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed !May some Network Issue", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    //==================================================================================================
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
        Intent intent = new Intent(MyProfile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //............................... choose from Camera ...............................................
    private void launchCameraIntent() {
        Intent intent = new Intent(MyProfile.this, ImagePickerActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                MyProfile.this.openSettings();
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
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }
    //......................... On Activity Result............................
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getParcelableExtra("path");
                imgUrlStr = imageUri.toString();
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Glide.with(getApplicationContext()).load(bitmap).into(profileImg);
                    uploadImage();
                    // loading profile image from local cache
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //..................................... Upload \ save image path ............................................
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
                        String id = auth.getCurrentUser().getUid();

                        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(id).child("Profile_Info");
                        final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users_List").child(id);

                        final HashMap map = new HashMap();
                        map.put("imgUrl", imgUrlStr);

//                        ref.updateChildren(map);

                            ref2.updateChildren(map);
                            ref3.updateChildren(map);
                            dialogImg.dismiss();
                            Toast.makeText(MyProfile.this.getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                    } else {
                        dialogImg.dismiss();
                        Toast.makeText(MyProfile.this.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogImg.dismiss();
                    Toast.makeText(MyProfile.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }
    /*================================================================================================*/
}

