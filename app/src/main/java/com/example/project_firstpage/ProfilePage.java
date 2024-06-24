package com.example.project_firstpage;

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


    private TextView profileName,pemail,pusername;

    Button logoutbtn,viewWishlistbtn;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("users");

        profileName = findViewById(R.id.profileName);
        pemail = findViewById(R.id.profileEmail);
        pusername = findViewById(R.id.profileUsername);
        viewWishlistbtn = findViewById(R.id.viewWishlistButton);
        logoutbtn = findViewById(R.id.logoutButton);


        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                userReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String username = userSnapshot.child("username").getValue(String.class);
                            profileName.setText("Name: " + name);
                            pemail.setText("Email: " + userEmail);
                            pusername.setText("Username: " + username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ProfilePage.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            // No user is signed in, redirect to login activity
            Intent intent = new Intent(ProfilePage.this, page2.class);
            startActivity(intent);
            finish();
        }

        // Set button click listeners
       viewWishlistbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               Intent wishlistIntent = new Intent(ProfilePage.this, WishlistActivity.class);
//               startActivity(wishlistIntent);
           }
       });

       logoutbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mAuth.signOut();
               Intent logoutIntent = new Intent(ProfilePage.this, page2.class);
               logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(logoutIntent);
               finish();
           }
       });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Create a map of item IDs to actions
        Map<Integer, Runnable> navActions = new HashMap<>();
//        navActions.put(R.id.navigation_home, this::navigateToHome);
//        navActions.put(R.id.navigation_dashboard, this::navigateToWishlist);
//        navActions.put(R.id.navigation_notifications, this::navigateToNotifications);

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