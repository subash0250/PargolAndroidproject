package com.example.project_firstpage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class AdminViewBorrowBook extends AppCompatActivity {
    // admin
    private TextView tvTitle, tvAuthor,tvlanguage,tvgener, tvavailability, tvBorrowDate;
    private ImageView ivimage;
    private Button  btnBack, btnReturn;
    private String bookId, title, author, language, gener, image, borrowDate ;
    private Boolean isAvailable;
    private DatabaseReference booksRef, books;
    FirebaseAuth mAuth;
    private  String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_borrow_book);
        mAuth = FirebaseAuth.getInstance();

    }
}