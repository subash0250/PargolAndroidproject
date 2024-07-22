package com.example.project_firstpage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DeleteBook extends AppCompatActivity {
    Button confirm_delete_book_btn, cancel_delete_book_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);
        confirm_delete_book_btn = findViewById(R.id.confirm_delete_book_btn);
        cancel_delete_book_btn = findViewById(R.id.cancel_delete_book_btn);

        confirm_delete_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete book
            }
        });

        cancel_delete_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back
            }
        });


    }
}