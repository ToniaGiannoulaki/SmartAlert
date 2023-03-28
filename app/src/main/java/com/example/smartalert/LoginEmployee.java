package com.example.smartalert;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginEmployee extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employer);


        //Employee goes to IncidentEmployee class
        Button button_Report = findViewById(R.id.button_Report);
        button_Report.setOnClickListener(view -> {
            Intent intent = new Intent(LoginEmployee.this, IncidentEmployee.class);
            startActivity(intent);
        });

        //Employee goes to MessagesEmployee class
        Button button_Message = findViewById(R.id.button_Messages);
        button_Message.setOnClickListener(view -> {
            Intent intent = new Intent(LoginEmployee.this, MessagesEmployee.class);
            startActivity(intent);
        });

        //Employee goes to MenuScreen class
        Button button_LogOut = findViewById(R.id.button_LogOut_Employer);
        button_LogOut.setOnClickListener(view -> {
            confirmLogOut();
        });
    }
    private void confirmLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    mAuth.signOut();
                    Intent intent = new Intent(LoginEmployee.this, MenuScreen.class);
                    intent.putExtra("userType", "Employee");
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}