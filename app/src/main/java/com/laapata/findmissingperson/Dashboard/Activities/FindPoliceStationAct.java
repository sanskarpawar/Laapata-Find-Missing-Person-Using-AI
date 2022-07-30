package com.laapata.findmissingperson.Dashboard.Activities;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laapata.findmissingperson.Dashboard.Adapters.CustomInfoWindowAdapter;
import com.laapata.findmissingperson.Dashboard.Adapters.RecyclerAdapter_Fp_Reports;
import com.laapata.findmissingperson.Dashboard.Fragments.CasesViewActivity;
import com.laapata.findmissingperson.ModelClasses.FoundPersonModel;
import com.laapata.findmissingperson.ModelClasses.UsersData;
import com.laapata.findmissingperson.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class FindPoliceStationAct extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "UpheavMapActivity";
    private GoogleMap mMap;
    double final_latitude;
    double final_longitude;
    AutocompleteSupportFragment autocompleteFragment;
    String addresso;
    String apiKey;

    String locationbtn;
    private Marker marker;
    Geocoder geocoder;
    Button button;
    ImageView myLocBtn;
    String pickupadress;
    String dropupadress;
    /////////////////////////////////////////////////////////
    private static final int REQUEST_ACCESS_FINE_LOCATION = 13112;
    private static final int REQUEST_CHECK_SETTINGS = 13119;

    private boolean mLocationPermissionGranted;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    ////////////////
    private FirebaseAuth auth;
    private List<UsersData> mUsersDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_find_police_stations);

        apiKey = getString(R.string.api_key);
        button = findViewById(R.id.add_btn_map);
        myLocBtn = findViewById(R.id.my_loc_btn);

        auth = FirebaseAuth.getInstance();
        mUsersDataList = new ArrayList<>();

        locationbtn = getIntent().getStringExtra("locationbtn");
        pickupadress = getIntent().getStringExtra("pickupadress");
        dropupadress = getIntent().getStringExtra("dropupadress");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

//        placesApiSearch();
////////////////////////////////////////////////
        // usercureentlocation();
        myLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usercureentlocation();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        checkLocationAvailability();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(51.5072, 0.1276))
                .bearing(10)
                .zoom(10)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
        ////////////////////////////////////////////
        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder = new Geocoder(FindPoliceStationAct.this);
                List<Address> addressi = new ArrayList<>();
                if (marker != null) {
                    marker.remove();
                }
                try {
                    addressi = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    final_latitude = point.latitude;
                    final_longitude = point.longitude;
                } catch (IOException e) {
                    Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                }
                final Address address = addressi.get(0);
                if (addressi.size() > 0) {
                    Log.d(TAG, "geoLocate: found a location: " + address.toString());
                    final ProgressDialog dialg = new ProgressDialog(FindPoliceStationAct.this);
                    ((ProgressDialog) dialg).setMessage("Loading...");
                    dialg.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            addresso = address.getAddressLine(0);
//                            autocompleteFragment.setText(addresso);
                            dialg.dismiss();

                            CameraPosition googlePlex = CameraPosition.builder()
                                    .target(new LatLng(address.getLatitude(), address.getLongitude()))
                                    .bearing(10)
                                    .zoom(14)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
                            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 13f, address.getAddressLine(0));
                            Toast.makeText(getApplicationContext(), addresso, Toast.LENGTH_SHORT).show();
                        }
                    }, 1500);

                }
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(point).title(addresso)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

            }
        });*/

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.police_marker);
        Bitmap mMarkerBitmap = Bitmap.createScaledBitmap(bitmap, 120, 146, false);

        final ProgressDialog dialog = new ProgressDialog(FindPoliceStationAct.this);
        dialog.setMessage("Loading Data...");
        dialog.show();
//        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("FoundedPersonsCases");
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("PoliceStations");
        try {
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsersDataList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UsersData usersData = snapshot.getValue(UsersData.class);
                        mUsersDataList.add(usersData);
                        if (usersData!=null && usersData.getUserType().equals("police")) {
                            if (usersData.getLatitude() != 0 && usersData.getLongitude() != 0) {
                                LatLng point0 = new LatLng(usersData.getLatitude(), usersData.getLongitude());
                                Marker marker = mMap.addMarker(new MarkerOptions().position(point0)
                                        .title(usersData.getFull_name()+"\nClick for details"));
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(mMarkerBitmap));
                                marker.showInfoWindow();
                                marker.setTag(usersData);
                            }
                            mUsersDataList.add(usersData);

                        }
                    }
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            UsersData usersData = (UsersData) marker.getTag();
                            if (usersData!=null) showOptionDialog(usersData);
                        }
                    });
//                    UsersData model = new UsersData();
//                    CustomInfoWindowAdapter customInfoWindowAdapter =
//                            new CustomInfoWindowAdapter(FindPoliceStationAct.this, model);
//                    mMap.setInfoWindowAdapter(customInfoWindowAdapter);
//                    LatLng point1 = new LatLng(31.526183129904638, 74.31587465107441);
//                    LatLng point2 = new LatLng(31.526343743638304, 74.3167855963111);
//                    LatLng point3 = new LatLng(31.528894664236606, 74.30775359272957);
//                    LatLng point4 = new LatLng(31.5368436244034, 74.31503009051086);
//                    Marker marker1 = mMap.addMarker(new MarkerOptions().position(point1).title("Police Station 1"));
//                    marker1.setIcon(BitmapDescriptorFactory.fromBitmap(mMarkerBitmap));
//                    mMap.addMarker(new MarkerOptions().position(point2).title("Police Station 2")
//                            .icon(BitmapDescriptorFactory.fromBitmap(mMarkerBitmap)));
//                    mMap.addMarker(new MarkerOptions().position(point3).title("Police Station 3")
//                            .icon(BitmapDescriptorFactory.fromBitmap(mMarkerBitmap)));
//                    mMap.addMarker(new MarkerOptions().position(point4).title("Police Station 4")
//                            .icon(BitmapDescriptorFactory.fromBitmap(mMarkerBitmap)));
                    dialog.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showOptionDialog(UsersData usersData){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_custom_info_window_police);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setGravity(Gravity.RIGHT);

        ImageView userImage = dialog.findViewById(R.id.iv_image);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvAddress = dialog.findViewById(R.id.tvAddress);
        TextView tvEmail = dialog.findViewById(R.id.tvEmail);
        Button phoneBtn = dialog.findViewById(R.id.phoneBtn);
        Button chatBtn = dialog.findViewById(R.id.chatBtn);

        tvTitle.setText(usersData.getFull_name());
        tvAddress.setText(usersData.getAddress());
        tvEmail.setText(usersData.getEmail());

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Make call", "");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+usersData.getPhone()));
                try {
                    startActivity(intent);
                    Log.i("Finished making a call", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent( getApplicationContext(), MessageActivity.class);
                intent.putExtra( "cruserid",usersData.getIdPush());
                startActivity(intent);
            }
        });


        dialog.show();

    }

    public void usercureentlocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FindPoliceStationAct.this);
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                if (marker != null) {
                    marker.remove();
                }
                for (final Location location : locationResult.getLocations()) {
                    final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Geocoder geocoder = new Geocoder(FindPoliceStationAct.this);
                    List<Address> addressi = new ArrayList<>();
                    if (marker != null) {
                        marker.remove();
                    }
                    try {
                        addressi = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        final_latitude = location.getLatitude();
                        final_longitude = location.getLongitude();
                        updateCurrentUserLatLong(final_latitude,final_longitude);

                    } catch (IOException e) {
                        Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                    }
                    try {
                        final Address address = addressi.get(0);
                        if (addressi.size() > 0) {
                            Log.d(TAG, "geoLocate: found a location: " + address.toString());
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    addresso = address.getAddressLine(0);
//                                    autocompleteFragment.setText(addresso);
                                    Toast.makeText(getApplicationContext(), addresso, Toast.LENGTH_SHORT).show();
                                CameraPosition googlePlex = CameraPosition.builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .bearing(10)
                                        .zoom(10)
                                        .build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 3000, null);
                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(addresso));
                            }
                            }, 1500);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mFusedLocationProviderClient.removeLocationUpdates(this);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        getPermissions();

    }

    public void updateCurrentUserLatLong(double latitude, double longitude){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser!=null) {
            String userUid = firebaseUser.getUid();
            String emailStr = firebaseUser.getEmail();
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users_List").child(userUid);
             DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("AllUsersAccount").child(userUid).child("Profile_Info");
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("latitude", latitude);
            hashMap.put("longitude", longitude);

            ref1.updateChildren(hashMap);
            ref2.updateChildren(hashMap);
        }
    }

    //-----------------------------------------------------------------------------------------------/
//////////////////////////////////////////////////////////////////////////////////////////////////
    public void placesApiSearch() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_id);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                final double lati = place.getLatLng().latitude;
                final double longi = place.getLatLng().longitude;
                addresso = place.getAddress();
                final_latitude = place.getLatLng().latitude;
                final_longitude = place.getLatLng().longitude;

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(lati, longi))
                        .bearing(10)
                        .tilt(25)
                        .zoom(14)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
                moveCamera(new LatLng(lati, longi), 14f, place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///////////////////////////////////////////
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        CameraPosition googlePlex = CameraPosition.builder()
                .target(latLng)
                .bearing(10)
                .tilt(25)
                .zoom(14)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 2000, null);
        if (!title.equals("My Location")) {
            if (marker != null) {
                marker.remove();
            }
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

    }
///////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    if (mLocationPermissionGranted) {
                        if (ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(this,
                                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                        }
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    new AlertDialog.Builder(FindPoliceStationAct.this)
                            .setTitle("Need Location")
                            .setMessage("Hi there! We can't show your current location without the" +
                                    " location service, could you please enable it?")
                            .setPositiveButton("Yep", (dialogInterface, i) -> checkLocationAvailability())
                            .setNegativeButton("No thanks", (dialogInterface, i) -> {
                                Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
                                finish();
                            }).show();
                    break;
                default:
                    break;
            }
        }
    }

    //////////////////////////////////////////
    private void checkLocationAvailability() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnCompleteListener(task1 -> {
            try {
                LocationSettingsResponse response = task1.getResult(ApiException.class);
                // All location settings are satisfied.
                if (mLocationPermissionGranted) {
                    if (ActivityCompat.checkSelfPermission(FindPoliceStationAct.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(FindPoliceStationAct.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, null);
                    }
                }

            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    FindPoliceStationAct.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
    ///////////////////////////
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do what you want . . .
                mLocationPermissionGranted = true;
                mSupportMapFragment.getMapAsync(this);
            } else {
                // permission denied, boo! :(
                // Show an explanation to the user why this permission is required
                displayDialog();
            }
        }
    }
    /////////////
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user why this permission is required
                    displayDialog();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_ACCESS_FINE_LOCATION);
                }
            } else {
                // Permission has already been granted
                mLocationPermissionGranted = true;
                mSupportMapFragment.getMapAsync(this);
            }
        } else {
            // Android SDK Version is below Marshmallow.
            // You don't need runtime permission, Do what you want . . .
            mLocationPermissionGranted = true;
            mSupportMapFragment.getMapAsync(this);
        }
    }
    ////////////////////////
    private void displayDialog() {
        new AlertDialog.Builder(FindPoliceStationAct.this)
                .setTitle("Location Permission")
                .setMessage("Hi there! We can't show your current location without the" +
                        " location permission, could you please grant it?")
                .setPositiveButton("Yep", (dialogInterface, i) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("No thanks", (dialogInterface, i) -> {
                    Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
                    finish();
                }).show();
    }
/////////////////////////////////


}


