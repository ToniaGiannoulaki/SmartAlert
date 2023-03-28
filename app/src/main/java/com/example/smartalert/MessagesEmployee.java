package com.example.smartalert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MessagesEmployee extends AppCompatActivity {

    Button accept, delete;
    TextView message;
    ImageView imageView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_employee);

        message = findViewById(R.id.textView_Messages);
        imageView = findViewById(R.id.imageView2);
        accept = findViewById(R.id.button_Accept);
        delete = findViewById(R.id.button_Delete);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserMessages");

        databaseReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the last message
                    IncidentInfo lastMessage = messageSnapshot.getValue(IncidentInfo.class);
                    // Update the text view with all the fields
                    message.setText("Incident: " + lastMessage.getIncident() +
                            "\nCountry: " + lastMessage.getCountry() +
                            "\nLatitude: " + lastMessage.getLatitude() +
                            "\nLongitude: " + lastMessage.getLongitude() +
                            "\nDate: " + lastMessage.getDate() +
                            "\nTime: " + lastMessage.getTime() +
                            "\nDescription: " + lastMessage.getDescription());


                    // Load the image from the URL and display it in the image view using Glide
                    Glide.with(imageView)
                            .load(lastMessage.getImageUri())
                            .into(imageView);
                }

                accept.setOnClickListener(v -> {
                    // Get the reference to the last message
                    Query lastMessageQuery = databaseReference.orderByKey().limitToLast(1);
                    lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for (DataSnapshot messageSnapshot : dataSnapshot1.getChildren()) {
                                // Retrieve the last message
                                IncidentInfo lastMessage = messageSnapshot.getValue(IncidentInfo.class);
                                // Update the message with the new status
                                  //lastMessage.setStatus("accepted");
                                // Save the updated message back to the database
                                messageSnapshot.getRef().setValue(lastMessage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
                });

                delete.setOnClickListener(v -> {
                    // Show a confirmation dialog to the employee
                    new AlertDialog.Builder(MessagesEmployee.this)
                            .setTitle("Delete message")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Get the reference to the last message
                                Query lastMessageQuery = databaseReference.orderByKey().limitToLast(1);
                                lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot12) {
                                        for (DataSnapshot messageSnapshot : dataSnapshot12.getChildren()) {
                                            // Delete the message from the database
                                            messageSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle errors here
                                    }
                                });
                            })
                            .setNegativeButton("No", null)
                            .show();
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

}