package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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


}

