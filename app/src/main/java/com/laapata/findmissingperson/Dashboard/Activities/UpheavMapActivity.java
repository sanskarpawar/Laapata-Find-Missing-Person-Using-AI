package com.laapata.findmissingperson.Dashboard.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.laapata.findmissingperson.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class UpheavMapActivity extends FragmentActivity implements OnMapReadyCallback {
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
    String fmpname;
    String fmpfathername;
    String fmpheight;
    String fmpage;
    String fmpplace;
    String fmppermanentadre;
    String fmpcontactnumber;
    String imgUrlStr;
    String vedioUrlstr;
    String permanenetadress;
    boolean fromPolice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upheav_map);

        apiKey = getString(R.string.api_key);
        button = findViewById(R.id.add_btn_map);
        myLocBtn = findViewById(R.id.my_loc_btn);

        fromPolice = getIntent().getBooleanExtra("fromPolice", false);

        locationbtn = getIntent().getStringExtra("locationbtn");
        pickupadress = getIntent().getStringExtra("pickupadress");
        dropupadress = getIntent().getStringExtra("dropupadress");

        fmpname = getIntent().getStringExtra("fpname");
        fmpfathername = getIntent().getStringExtra("fmpfathername");
        fmpheight = getIntent().getStringExtra("fmpheight");
        fmpage = getIntent().getStringExtra("fmpage");
        fmpplace = getIntent().getStringExtra("fmpplace");
        fmpcontactnumber = getIntent().getStringExtra("fmpcontactnumber");
        imgUrlStr = getIntent().getStringExtra("imgUrlStr");
        vedioUrlstr = getIntent().getStringExtra("vedioUrlstr");
        permanenetadress = getIntent().getStringExtra("getlocation");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromPolice) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("address", addresso);
                    resultIntent.putExtra("latitude", final_latitude);
                    resultIntent.putExtra("longitude", final_longitude);
                    setResult(RESULT_OK, resultIntent);
                    finish();

                } else {
                    Intent intent = new Intent(getApplicationContext(), Create_Fp_Case.class);
                    intent.putExtra("address", addresso);
                    intent.putExtra("latitude", final_latitude);
                    intent.putExtra("longitude", final_longitude);
                    intent.putExtra("fpname", fmpname);
                    intent.putExtra("fmpfathername", fmpfathername);
                    intent.putExtra("fmpheight", fmpheight);
                    intent.putExtra("fmpage", fmpage);
                    intent.putExtra("fmpplace", fmpplace);
                    intent.putExtra("fmpcontactnumber", fmpcontactnumber);
                    intent.putExtra("imageuri", imgUrlStr);
                    intent.putExtra("vedioUrlstr", vedioUrlstr);
                    intent.putExtra("getlocation", permanenetadress);
                    startActivity(intent);
                    finish();
                }
                //   Toast.makeText(UpheavMapActivity.this, Pi, Toast.LENGTH_SHORT).show();


            }
        });
        placesApiSearch();
////////////////////////////////////////////////
        // usercureentlocation();
        myLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usercureentlocation();
            }
        });

    }

    public void usercureentlocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UpheavMapActivity.this);
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
                    Geocoder geocoder = new Geocoder(UpheavMapActivity.this);
                    List<Address> addressi = new ArrayList<>();
                    if (marker != null) {
                        marker.remove();
                    }
                    try {
                        addressi = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        final_latitude = location.getLatitude();
                        final_longitude = location.getLongitude();
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
                                    autocompleteFragment.setText(addresso);
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder = new Geocoder(UpheavMapActivity.this);
                List<Address> addressi = new ArrayList<>();
                if (marker != null) {
                    marker.remove();
                }
                try {
                    addressi = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    final_latitude = point.latitude;
                    final_longitude = point.longitude;

                    Log.e("FinalLongLat001-"," "+final_latitude+"::"+final_longitude);

                } catch (IOException e) {
                    Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                }
                final Address address = addressi.get(0);
                if (addressi.size() > 0) {
                    Log.d(TAG, "geoLocate: found a location: " + address.toString());
                    final android.app.ProgressDialog dialg = new android.app.ProgressDialog(UpheavMapActivity.this);
                    ((ProgressDialog) dialg).setMessage("Loading...");
                    dialg.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            addresso = address.getAddressLine(0);
                            autocompleteFragment.setText(addresso);
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
        });
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
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
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
                        new AlertDialog.Builder(UpheavMapActivity.this)
                                .setTitle("Need Location")
                                .setMessage("Hi there! We can't show your current location without the" +
                                        " location service, could you please enable it?")
                                .setPositiveButton("Yep", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        checkLocationAvailability();
                                    }
                                })
                                .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    //////////////////////////////////////////
    private void checkLocationAvailability() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {

            @SuppressLint("MissingPermission")
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult( ApiException.class);
                    // All location settings are satisfied.
                    if (mLocationPermissionGranted) {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, null);
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
                                        UpheavMapActivity.this,
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
            }
        });
    }
    ///////////////////////////
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
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

                return;
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
        new AlertDialog.Builder(UpheavMapActivity.this)
                .setTitle("Location Permission")
                .setMessage("Hi there! We can't show your current location without the" +
                        " location permission, could you please grant it?")
                .setPositiveButton("Yep", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).show();
    }
/////////////////////////////////


}


