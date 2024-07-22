package com.example.project_firstpage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteBook extends AppCompatActivity {
    Button confirm_delete_book_btn, cancel_delete_book_btn;
    private DatabaseReference booksDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);
        confirm_delete_book_btn = findViewById(R.id.confirm_delete_book_btn);
        cancel_delete_book_btn = findViewById(R.id.cancel_delete_book_btn);

        confirm_delete_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String  bookId = getIntent().getStringExtra("bookId");
              if(bookId != null){
                  booksDatabase = FirebaseDatabase.getInstance().getReference("books");

              }
            }
        });

        cancel_delete_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}