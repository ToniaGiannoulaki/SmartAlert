package com.example.smartalert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IncidentUser extends AppCompatActivity {
    TextView incident, address, latitude, longitude, date, time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat, simpleDateFormat1;
    String Date, Time ;
    EditText description;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    IncidentInfo incidentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_user);

        incidentInfo = new IncidentInfo();
        ImageView imageUrl = findViewById(R.id.imageView);

        //Transfer DATA from LoginUser class
        incident = findViewById(R.id.textView3);
        Intent incidentUser = getIntent();
        String text = incidentUser.getStringExtra("incident");
        incident.setText(text);

        //Receive user's timestamp
        latitude = findViewById(R.id.textView_Latitude);
        longitude = findViewById(R.id.textView_Longitude);
        address =  findViewById(R.id.textView_Address2);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();

        //Receive user's date and time
        calendar = Calendar.getInstance();
        date = findViewById(R.id.textView_Date);
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date= simpleDateFormat.format(calendar.getTime());
        date.setText(Date);

        time = findViewById(R.id.textView_Time);
        simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        Time= simpleDateFormat1.format(calendar.getTime());
        time.setText(Time);

        //Check if edit text is empty, it's a required field
        Button button = findViewById(R.id.button_Send);
        description = findViewById(R.id.editTextTextMultiLine_Description);

        button.setOnClickListener(view -> {
            String inc = incident.getText().toString().trim();
            String add = address.getText().toString().trim();
            String lat = latitude.getText().toString().trim();
            String lon = longitude.getText().toString().trim();
            String dat = date.getText().toString().trim();
            String tim = time.getText().toString().trim();
            String desc = description.getText().toString().trim();
            String ima = imageUrl.toString();

            if (description.getText().toString().trim().isEmpty()) {
                Toast.makeText(IncidentUser.this, "To send the message you must describe the incident",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(IncidentUser.this, "Message sent successfully!",Toast.LENGTH_LONG).show();
                saveMessage(inc,add,lat,lon,dat,tim,desc,ima);
            }
        });

        //Load Image from the gallery
        Button button_Gallery = findViewById(R.id.button_Image);
        button_Gallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.imageView);
            Glide.with(this).load(selectedImage).into(imageView);
            Toast.makeText(IncidentUser.this, "Image uploaded successfully!",Toast.LENGTH_LONG).show();

            // Check if the ImageView has an image set
            if (imageView.getDrawable() != null) {
                Log.d("MyApp", "Image loaded successfully into ImageView");
            } else {
                Log.d("MyApp", "Failed to load image into ImageView");
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude.setText(location.getLatitude() + "");
                        longitude.setText(location.getLongitude() + "");
                        address.setText(getUserCountry(address.getContext()));
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitude.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    //Get the user's country
    @Nullable
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getNetworkCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    //Save  user's message to Firebase Realtime DB
    public void saveMessage(String inc, String add, String lat, String lon, String dat, String ti, String desc,String img){

        incidentInfo.setIncident(inc);
        incidentInfo.setCountry(add);
        incidentInfo.setLatitude(lat);
        incidentInfo.setLongitude(lon);
        incidentInfo.setDate(dat);
        incidentInfo.setTime(ti);
        incidentInfo.setDescription(desc);
        incidentInfo.setImageUri(img);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserMessages");
        String userId = databaseReference.push().getKey();
        databaseReference.child(userId).setValue(incidentInfo);

    }
}
