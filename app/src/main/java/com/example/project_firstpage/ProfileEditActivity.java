package com.example.project_firstpage;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileEditActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference userReference;
    TextView firstname, lastname;
    Button button_edit;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        firstname = findViewById(R.id.et_firstname);
        lastname = findViewById(R.id.et_lastname);
        button_edit = findViewById(R.id.btn_edit);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("users");

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                userReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String firstName = userSnapshot.child("firstName").getValue(String.class);
                            String lastName = userSnapshot.child("lastName").getValue(String.class);
                            firstname.setText(firstName);
                            lastname.setText(lastName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ProfileEditActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String f_name = firstname.getText().toString().trim();
                String l_name = lastname.getText().toString().trim();

                if(f_name.isEmpty()){
                    firstname.setError("First name can't be empty");
                    return;
                }
                if(l_name.isEmpty()){
                    lastname.setError("Last name can't be empty");
                    return;
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String email = currentUser.getEmail();
                    Query query = userReference.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                userSnapshot.getRef().child("firstName").setValue(f_name);
                                userSnapshot.getRef().child("lastName").setValue(l_name);
                            }
                            Toast.makeText(ProfileEditActivity.this, "User name updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
                }
            }
        });

    }
}