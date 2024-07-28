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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewBook extends AppCompatActivity {
    private TextView tvTitle, tvAuthor,tvlanguage,tvgener, tvavailability;
    private ImageView ivimage;
    private Button  btnBack, btnBorrow;
    private String bookId, title, author, language, gener, image ;
    private Boolean isAvailable;
    private DatabaseReference booksRef;
    FirebaseAuth mAuth;
    private  String userId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        btnBack = findViewById(R.id.btnBack);
        btnBorrow = findViewById(R.id.btnBorrow);
        tvTitle = findViewById(R.id.Title);
        tvAuthor = findViewById(R.id.Author);
        tvlanguage = findViewById(R.id.Language);
        tvgener = findViewById(R.id.Genere);
        ivimage = findViewById(R.id.Image);
        tvavailability = findViewById(R.id.Availability);
        //booksDatabase = FirebaseDatabase.getInstance().getReference("books");
        booksRef = FirebaseDatabase.getInstance().getReference("books");
        bookId = getIntent().getStringExtra("bookId");
        if (bookId != null) {
            booksRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        title = snapshot.child("title").getValue(String.class);
                        author = snapshot.child("author").getValue(String.class);
                        language = snapshot.child("language").getValue(String.class);
                        gener = snapshot.child("gener").getValue(String.class);
                        image = snapshot.child("image").getValue(String.class);
                        isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
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
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAvailable){
                    showBookOptions(bookId, title, author, language, gener, image, isAvailable);
                }else {
                    Toast.makeText(ViewBook.this, "This book not available, Try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBookOptions(String bookId, String title, String author, String language, String gener, String image, Boolean isAvailable) {
        booksRef.child(bookId).child("isAvailable").setValue(false);
        Book book = new Book(bookId, title, author, language, gener, image, false);
        DatabaseReference borrowRef = FirebaseDatabase.getInstance().getReference("borrow").child(userId).child(bookId);
        borrowRef.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ViewBook.this, "Book borrowed successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewBook.this, "Book borrow failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}