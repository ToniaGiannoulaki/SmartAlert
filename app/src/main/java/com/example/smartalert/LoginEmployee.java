package com.example.smartalert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginEmployee extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employer);


        Button button_Report = findViewById(R.id.button_Report);
        button_Report.setOnClickListener(view -> {
            Intent intent = new Intent(LoginEmployee.this, IncidentEmployee.class);
            startActivity(intent);
        });

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
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}