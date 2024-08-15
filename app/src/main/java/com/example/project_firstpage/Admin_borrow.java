package com.example.project_firstpage;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_borrow extends AppCompatActivity {

    private ListView userListView;

    private List<User> users;
    private AdminborrowbookAdapter adapter;
    private TextView noUserMsg;
    private Button btn_back;

    private DatabaseReference borroeBooksRef, usersRef;
    FirebaseAuth mAuth;
    private  String userId;
    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_borrow);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Book borrowed users");
        setSupportActionBar(toolbar);

        noUserMsg = findViewById(R.id.noUserMsg);
        btn_back = findViewById(R.id.btn_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userListView = findViewById(R.id.userListView);




        users = new ArrayList<>();
        adapter = new AdminborrowbookAdapter(this, users);
        userListView.setAdapter(adapter);

        borroeBooksRef = FirebaseDatabase.getInstance().getReference("borrow");

        loadBorrowUsers();

    }

    private void loadBorrowUsers() {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        borroeBooksRef = FirebaseDatabase.getInstance().getReference("borrow");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                if (usersSnapshot.exists()) {
                    Map<String, User> usersMap = new HashMap<>();
                    for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                        String userKey = userSnapshot.getKey();
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            user.setUserId(userKey);
                            usersMap.put(userKey, user);
                        }
                    }

                    borroeBooksRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot borrowSnapshot) {
                            List<User> userList = new ArrayList<>();
                            if (borrowSnapshot.exists()) {
                                for (DataSnapshot snapshot : borrowSnapshot.getChildren()) {
                                    try {
                                        String userKey = snapshot.getKey();
                                        Log.e(TAG, "Books borrowed by user key: " + userKey);
                                        User user = usersMap.get(userKey);
                                        if (user != null) {
                                            Log.e(TAG, "User details: " + user.getEmail());
                                            userList.add(user);  // Add user to the list
                                        } else {
                                            Log.e(TAG, "User not found in pre-loaded data for key: " + userKey);
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error retrieving user details from snapshot: " + snapshot, e);
                                    }
                                }
                            } else {
                                // noUsersMsg.setVisibility(View.VISIBLE);
                            }

                            adapter.updateData(userList);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Admin_borrow.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Database error: " + databaseError.getMessage(), databaseError.toException());
                        }
                    });

                } else {
                    Log.e(TAG, "No users found in the database.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error loading users: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }
}

