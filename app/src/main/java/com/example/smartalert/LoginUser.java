package com.example.smartalert;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUser extends AppCompatActivity {

    Button logOut,showMessages;
    ImageButton b1, b2, b3, b4, b5, b6;
    TextView one, two, three, four, five, six;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        //When the user clicks the logOut button, we call the logOut method
        logOut = findViewById(R.id.button_LogOut);
        logOut.setOnClickListener(view -> confirmLogOut());

        //When the user clicks the messages button, we call the showMessages method
        showMessages = findViewById(R.id.button_MessagesUser);
        showMessages.setOnClickListener(view -> showMessages());

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

        //Fire incident
        b1.setOnClickListener(view -> {
            b1.setSelected(!b1.isSelected());
            String incident = one.getText().toString().trim();
            if (b1.isSelected()) {
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        //Flood incident
        b2.setOnClickListener(view -> {
            b2.setSelected(!b2.isSelected());
            String incident = two.getText().toString().trim();
            if(b2.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        //Earthquake incident
        b3.setOnClickListener(view -> {
            b3.setSelected(!b3.isSelected());
            String incident = three.getText().toString().trim();
            if(b3.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        //Tornado incident
        b4.setOnClickListener(view -> {
            b4.setSelected(!b4.isSelected());
            String incident = four.getText().toString().trim();
            if(b4.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        //Extreme Weather conditions incident
        b5.setOnClickListener(view -> {
            b5.setSelected(!b5.isSelected());
            String incident = five.getText().toString().trim();
            if(b5.isSelected()){
                Intent intent = new Intent(LoginUser.this, IncidentUser.class);
                intent.putExtra("incident", incident);
                startActivity(intent);
            }
        });

        //Something else incident
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

    //The confirmLogOut method is when the user clicks the LogOut button, asks the user if he/she wants to log out
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

    //The showMessages method is to show if the user has any new messages for him/her
    private void showMessages(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EmployeeMessages");

        databaseReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if there are any messages available
                if (dataSnapshot.exists()) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        // Retrieve the last message
                        IncidentEmployeeInfo lastMessage = messageSnapshot.getValue(IncidentEmployeeInfo.class);
                        // Create the message string
                        String message = "Incident: " + lastMessage.getTitle() +
                                "\nLatitude: " + lastMessage.getLatitude() +
                                "\nLongitude: " + lastMessage.getLongitude() +
                                "\nMax Distance:" + lastMessage.getMaxDistance() +
                                "\nDate: " + lastMessage.getDate() +
                                "\nTime: " + lastMessage.getTime() +
                                "\nDescription: " + lastMessage.getDescription();
                        // Create the AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginUser.this);
                        builder.setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // do nothing
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    // If no messages available, show a toast message
                    Toast.makeText(LoginUser.this, "No messages available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }}