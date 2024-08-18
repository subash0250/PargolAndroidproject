package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
      //  tvBorrowDate = findViewById(R.id.BorrowDate);



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Central Library");
        setSupportActionBar(toolbar);


        booksRef = FirebaseDatabase.getInstance().getReference("borrow").child(userId);
        bookId = getIntent().getStringExtra("bookId");
        Log.e("bookId", bookId);
        Toast.makeText(this, "Book id"+ bookId, Toast.LENGTH_SHORT).show();
        books = FirebaseDatabase.getInstance().getReference("books").child(bookId);
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
                        borrowDate = snapshot.child("borrowDate").getValue(String.class);
                        tvTitle.setText("Title: " + title);
                        tvAuthor.setText("Author: " + author);
                        tvlanguage.setText("Language: "+ language);
                        tvgener.setText("Gener: " + gener);
                        ivimage.setImageURI(Uri.parse(image));
                        Picasso.get()
                                .load(image)
                                .into(ivimage);
                        // tvavailability.setText(Boolean.TRUE.equals(isAvailable) ? "Availability: Yes" : "Availability: No");
                        tvBorrowDate.setText("Borrowed Date: " + borrowDate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        } else {
            Toast.makeText(this, "Can not find the book", Toast.LENGTH_SHORT).show();
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAvailable){
                    showBookOptions(bookId, title, author, language, gener, image, isAvailable);
                    Toast.makeText(AdminViewBorrowBook.this, "This book return success.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AdminViewBorrowBook.this, "This book can't return now, Try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showBookOptions(String bookId, String title, String author, String language, String gener, String image, Boolean isAvailable) {
        books.child("isAvailable").setValue(true);
        booksRef.child(bookId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminViewBorrowBook.this, "Book returns success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminViewBorrowBook.this, "Book returns failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_profile) {
            fun_profile();
            return true;
        } else if (itemId == R.id.menu_about) {
            fun_about();
            return true;
        }else if (itemId == R.id.menu_sign_out) {
            fun_sign_out();
            return true;
        } else if (itemId == R.id.menu_borrow) {
            fun_borrow();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void fun_sign_out() {
        Intent intent = new Intent(AdminViewBorrowBook.this, SignOut.class);
        startActivity(intent);
    }

    private void fun_about() {
        Intent intent = new Intent(AdminViewBorrowBook.this, About.class);
        startActivity(intent);
    }

    private void fun_profile() {
        Intent intent = new Intent(AdminViewBorrowBook.this, Profile.class);
        startActivity(intent);
    }
    private void fun_borrow() {
        Intent intent = new Intent(AdminViewBorrowBook.this, BorrowActivity.class);
        startActivity(intent);
    }
}
