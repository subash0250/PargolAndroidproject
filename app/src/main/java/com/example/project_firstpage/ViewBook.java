package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewBook extends AppCompatActivity {
    private TextView tvTitle, tvAuthor,tvlanguage,tvgener, tvavailability;
    private ImageView ivimage;
    private Button  btnBack;
    private DatabaseReference booksDatabase;
    private String bookId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.Title);
        tvAuthor = findViewById(R.id.Author);
        tvlanguage = findViewById(R.id.Language);
        tvgener = findViewById(R.id.Genere);
        ivimage = findViewById(R.id.Image);
        tvavailability = findViewById(R.id.Availability);
        booksDatabase = FirebaseDatabase.getInstance().getReference("books");
        bookId = getIntent().getStringExtra("bookId");
        if (bookId != null) {
            booksDatabase.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String title = snapshot.child("title").getValue(String.class);
                        String author = snapshot.child("author").getValue(String.class);
                        String language = snapshot.child("language").getValue(String.class);
                        String gener = snapshot.child("gener").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        Boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                        tvTitle.setText("Title: " + title);
                        tvAuthor.setText("Author: " + author);
                        tvlanguage.setText("Language: "+ language);
                        tvgener.setText("Gener: " + gener);
                        ivimage.setImageURI(Uri.parse(image));
                        Picasso.get()
                                .load(image)
                                .into(ivimage);
                        tvavailability.setText(Boolean.TRUE.equals(isAvailable) ? "Availability: Yes" : "Availability: No");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // can't find book
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}