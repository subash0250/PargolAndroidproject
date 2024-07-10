package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splashscreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    FirebaseDatabase.getInstance().getReference("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String role = dataSnapshot.child("role").getValue(String.class);
                            if (role != null && role.equals("admin")) {
                                Intent intent = new Intent(splashscreen.this, AdminDashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(splashscreen.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        }


                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                } else {
                    Intent i = new Intent(splashscreen.this, Login.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
