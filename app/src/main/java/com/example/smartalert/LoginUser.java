package com.example.smartalert;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {

    Button button_logOut;
    ImageButton b1, b2, b3, b4, b5, b6;
    TextView one, two, three, four, five, six;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        button_logOut = findViewById(R.id.button_LogOut);
        button_logOut.setOnClickListener(view -> confirmLogOut());

        //Choose Incident
        b1 = findViewById(R.id.imageButton_fire);
        b2 = findViewById(R.id.imageButton_flood);
        b3 = findViewById(R.id.imageButton_earthquake);
        b4 = findViewById(R.id.imageButton_hurricane);
        b5 = findViewById(R.id.imageButton_weather);
        b6 = findViewById(R.id.imageButton_something_else);

        one = findViewById(R.id.textView_fire);
        two = findViewById(R.id.textView_flood);
        three = findViewById(R.id.textView_earthquake);
        four = findViewById(R.id.textView_tornado);
        five = findViewById(R.id.textView_weather);
        six = findViewById(R.id.textView_something_else);

        b1.setOnClickListener(view -> {
            b1.setSelected(!b1.isSelected());
            String incident = one.getText().toString().trim();
            if (b1.isSelected()) {
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(view -> {
            b2.setSelected(!b2.isSelected());
            String incident = two.getText().toString().trim();
            if(b2.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        b3.setOnClickListener(view -> {
            b3.setSelected(!b3.isSelected());
            String incident = three.getText().toString().trim();
            if(b3.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        b4.setOnClickListener(view -> {
            b4.setSelected(!b4.isSelected());
            String incident = four.getText().toString().trim();
            if(b4.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        b5.setOnClickListener(view -> {
            b5.setSelected(!b5.isSelected());
            String incident = five.getText().toString().trim();
            if(b5.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        b6.setOnClickListener(view -> {
            b6.setSelected(!b6.isSelected());
            String incident = six.getText().toString().trim();
            if(b6.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });
    }
    private void confirmLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    mAuth.signOut();
                    Intent intent = new Intent(LoginUser.this, MenuScreen.class);
                    intent.putExtra("userType", "user");
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

}