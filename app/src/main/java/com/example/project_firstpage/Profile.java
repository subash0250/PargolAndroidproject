package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView textView_username, textView_email, textView_role;
    Button button_back, button_edit;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference userReference;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("users");

        button_back = findViewById(R.id.btn_back);
        button_edit = findViewById(R.id.btn_edit);
        textView_username = findViewById(R.id.profileName);
        textView_email = findViewById(R.id.profileEmail);
        textView_role = findViewById(R.id.profileRole);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                userReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String firstName = userSnapshot.child("firstName").getValue(String.class);
                            String lastName = userSnapshot.child("lastName").getValue(String.class);
                            String email = userSnapshot.child("email").getValue(String.class);
                            String role = userSnapshot.child("role").getValue(String.class);
                            textView_username.setText("Name: "+ firstName + " " + lastName);
                            textView_email.setText("Email: " + email);
                            textView_role.setText("Account Type: " + role);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Profile.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Intent intent = new Intent(Profile.this, Login.class);
            startActivity(intent);
            finish();
        }



    }
}