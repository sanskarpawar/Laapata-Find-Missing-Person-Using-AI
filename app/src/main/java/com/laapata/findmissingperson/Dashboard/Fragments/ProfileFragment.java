package com.laapata.findmissingperson.Dashboard.Fragments;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.laapata.findmissingperson.Authentication.AuthActivity;
import com.laapata.findmissingperson.Authentication.LoginFragment;
import com.laapata.findmissingperson.Dashboard.Activities.MyProfile;
import com.laapata.findmissingperson.Helper.ImagePickerActivity;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static FragmentManager fragmentManager ;

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
    String imgUrlStr ;
    String isVerifiedStr ;
    String userTypeStr ;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile_fragment, null);
        fragmentManager = getActivity().getSupportFragmentManager();
        saveBtn = rootView.findViewById(R.id.save_btn_my_profile);
        titleTv  = rootView.findViewById(R.id.title_tv_my_profile);
        emailTv  = rootView.findViewById(R.id.email_tv_my_profile);
        nameEt  = rootView.findViewById(R.id.name_et_my_profile);
        phoneEt = rootView.findViewById(R.id.phone_et_my_profile);
        cityEt = rootView.findViewById(R.id.city_et_my_profile);
        AddresEt  = rootView.findViewById(R.id.address_et_my_profile);
        dateTv = rootView.findViewById(R.id.date_stamp_my_profile);
        profileImg = rootView.findViewById(R.id.pro_img_my_profile);
        linearLayout   = rootView.findViewById(R.id.linear_root_my_profile);
        linearLayout.requestFocus();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance() ;
        storageReference = FirebaseStorage.getInstance().getReference("uploads");


        emailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Toast.makeText(getContext(), "email can't be changed", Toast.LENGTH_SHORT).show(); }});

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
                Dexter.withActivity(getActivity())
                        .withPermissions( Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        return rootView ;
    }
    //============ Onstart method
    @Override
    public void onStart() {
        super.onStart();
        // data is found
        final Dialog dialog = new ProgressDialog( getActivity() );
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
                            Glide.with( getContext() ).load( data.getImgUrl() ).placeholder( R.drawable.sample ).into( profileImg );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText( getContext(), "Oops ! Some Network Issue.", Toast.LENGTH_SHORT ).show();
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
        final Dialog getDialog = new ProgressDialog(getActivity());
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
                    Toast.makeText( getContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                } else {
                    getDialog.dismiss();
                    Toast.makeText( getContext(), "Failed !May some Network Issue", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    //==================================================================================================
//.................................... img dialog ..................................................
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
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
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    //............................... choose from Camera ...............................................
    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
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
        ContentResolver contentResolver =  getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
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
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);

    }
    //......................... On Activity Result............................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imageUri = data.getParcelableExtra("path");
                imgUrlStr = imageUri.toString();
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    Glide.with( getContext()).load(bitmap).into(profileImg);
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
            final Dialog dialogImg = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity() , "Uploaded", Toast.LENGTH_SHORT).show();

                    } else {
                        dialogImg.dismiss();
                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogImg.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText( getContext(), "No Image selected", Toast.LENGTH_SHORT).show();
        }
    }
    /*================================================================================================*/
}

