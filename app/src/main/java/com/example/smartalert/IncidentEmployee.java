package com.example.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IncidentEmployee extends AppCompatActivity {

    EditText title, latitude, longitude, maxDistance, date, time, description;
    Button send;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    IncidentEmployeeInfo incidentEmployeeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_employee);

        title = findViewById(R.id.editText_Title);
        latitude = findViewById(R.id.editText_Latitude);
        longitude = findViewById(R.id.editText_Longitude);
        maxDistance = findViewById(R.id.editText_MaxDistance);
        date = findViewById(R.id.editTextDate);
        time = findViewById(R.id.editTextTime);
        description = findViewById(R.id.editTextTextMultiLine_Description2);
        send = findViewById(R.id.button_Send2);

        incidentEmployeeInfo = new IncidentEmployeeInfo();

        send.setOnClickListener(view -> {
            String tit = title.getText().toString().trim();
            String lat = latitude.getText().toString().trim();
            String lon = longitude.getText().toString().trim();
            String max = maxDistance.getText().toString().trim();
            String dat = date.getText().toString().trim();
            String tim = time.getText().toString().trim();
            String desc = description.getText().toString().trim();

            //Check if the fields are empty
            if (tit.isEmpty() || lat.isEmpty() || lon.isEmpty() || max.isEmpty() || dat.isEmpty() || tim.isEmpty() || desc.isEmpty()){
                Toast.makeText(this, "You must fill all the fields to send the message!", Toast.LENGTH_SHORT).show();
            }else{
                //call the saveMessage() method;
                saveMessage(tit,lat,lon,max,dat,tim,desc);
                Toast.makeText(this, "Your message sent successfully!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //saveMessage Method to save the employee's message to the realtime DB
    public void saveMessage(String tit, String lat, String lon, String max, String dat, String tim, String desc){
        incidentEmployeeInfo.setTitle(tit);
        incidentEmployeeInfo.setLatitude(lat);
        incidentEmployeeInfo.setLongitude(lon);
        incidentEmployeeInfo.setMaxDistance(max);
        incidentEmployeeInfo.setDate(dat);
        incidentEmployeeInfo.setTime(tim);
        incidentEmployeeInfo.setDescription(desc);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EmployeeMessages");
        String userId = databaseReference.push().getKey();
        databaseReference.child(userId).setValue(incidentEmployeeInfo);
    }
}