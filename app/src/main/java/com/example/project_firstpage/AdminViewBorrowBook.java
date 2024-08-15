package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_borrow_book);
        mAuth = FirebaseAuth.getInstance();
        //        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            userId = user.getUid();
//        }
        userId = getIntent().getStringExtra("userId");
        Log.e("userId", userId);
        btnBack = findViewById(R.id.btnBack);
        btnReturn = findViewById(R.id.btnReturn);
        tvTitle = findViewById(R.id.Title);
        tvAuthor = findViewById(R.id.Author);
        tvlanguage = findViewById(R.id.Language);
        tvgener = findViewById(R.id.Genere);
        ivimage = findViewById(R.id.Image);
        //tvavailability = findViewById(R.id.Availability);
        tvBorrowDate = findViewById(R.id.BorrowDate);

    }
}