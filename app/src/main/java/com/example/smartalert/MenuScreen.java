package com.example.smartalert;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MenuScreen extends AppCompatActivity {
    
    Button button;
    EditText editText_email, editText_password;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        loadLocale();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getResources().getString(R.string.app_name));

        //Sign in Account
        editText_email = findViewById(R.id.text_email);
        editText_password = findViewById(R.id.text_password);

        // Check if user is already logged in
        FirebaseUser currentUser =  firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String userRole = sharedPreferences.getString("userRole", "");
            if (userRole.equals("user")) {
                Intent intent = new Intent(MenuScreen.this, LoginUser.class);
                startActivity(intent);
                finish();
            } else if (userRole.equals("employee")) {
                Intent intent = new Intent(MenuScreen.this, LoginEmployee.class);
                startActivity(intent);
                finish();
            }
        }

        button = findViewById(R.id.button_sign_in);
        button.setOnClickListener(view -> {
            String email = editText_email.getText().toString().trim();
            String pass = editText_password.getText().toString().trim();
            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(MenuScreen.this, "To log in please fill the fields",Toast.LENGTH_LONG).show();
            }else{
                signIn();
            }

        });

        //Open Registration
        button = findViewById(R.id.button_sign_up);
        Intent intent2 = new Intent(this, Register.class);

        button.setOnClickListener(view -> startActivity(intent2));

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //Load language saved in share preferences
    public void loadLocale (){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }

    //Sign in
    private void signIn() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, check if email ends with "@gov.gr"
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            String email = user.getEmail();
                            assert email != null;
                            if (email.endsWith("@gov.gr")) {
                                // Email ends with "@gov.gr", open employer activity
                                Intent intent = new Intent(MenuScreen.this, LoginEmployee.class);
                                startActivity(intent);
                            } else {
                                // Email does not end with "@gov.gr", open user activity
                                Intent intent = new Intent(MenuScreen.this, LoginUser.class);
                                startActivity(intent);
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MenuScreen.this, "Email or password is wrong.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        // Exit the app
        finishAffinity();
    }

}