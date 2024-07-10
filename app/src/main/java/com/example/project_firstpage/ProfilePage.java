package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    private TextView profileName, profileEmail, profileUsername, profileRole;
    private Button logoutButton, viewWishlistButton;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("users");

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profileRole = findViewById(R.id.profileRole);
        viewWishlistButton = findViewById(R.id.viewWishlistButton);
        logoutButton = findViewById(R.id.logoutButton);

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                userReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String firstName = userSnapshot.child("firstName").getValue(String.class);
                            String lastName = userSnapshot.child("lastName").getValue(String.class);
                            String username = firstName + " " + lastName;
                            String role = userSnapshot.child("role").getValue(String.class);
                            profileName.setText("Name: "+ username);
                            profileEmail.setText("Email: " + userEmail);
                            profileUsername.setText("Username: " + username);
                            profileRole.setText("Role: " + role);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ProfilePage.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Intent intent = new Intent(ProfilePage.this, Login.class);
            startActivity(intent);
            finish();
        }


        viewWishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(ProfilePage.this, Login.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Create a map of item IDs to actions
        Map<Integer, Runnable> navActions = new HashMap<>();
        // Add navigation actions here

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });
    }
}
