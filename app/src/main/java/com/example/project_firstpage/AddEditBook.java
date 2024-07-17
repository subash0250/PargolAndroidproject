package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEditBook extends AppCompatActivity {

    private EditText edtTitle, edtAuthor,edtlanguage,edtgener, edtimage;
    private CheckBox checkboxAvailable;
    private Button btnAdd, btnBack;
    private DatabaseReference booksDatabase;
    private String bookId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtlanguage = findViewById(R.id.languages);
        edtgener = findViewById(R.id.genere);
        edtimage = findViewById(R.id.image);
        checkboxAvailable = findViewById(R.id.checkboxAvailable);

        btnAdd = findViewById(R.id.btnAdd);

        booksDatabase = FirebaseDatabase.getInstance().getReference("books");

        bookId = getIntent().getStringExtra("bookId");
        if (bookId != null) {
            setTitle("Edit Book");
            // Load book details and set to EditTexts (implementation not shown)
        } else {
            setTitle("Add Book");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String author = edtAuthor.getText().toString();
                String language = edtlanguage.getText().toString();
                String gener = edtgener.getText().toString();
                String image = edtimage.getText().toString();
                boolean isAvailable = checkboxAvailable.isChecked();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author) || TextUtils.isEmpty(language) || TextUtils.isEmpty(gener) ) {
                    Toast.makeText(AddEditBook.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (bookId != null) {
                    updateBook(title, author,language,gener, image, isAvailable);
                } else {
                    addBook(title, author,language,gener,image, isAvailable);
                }
            }
        });
    }

    private void addBook(String title, String author,String language, String gener, String image, Boolean isAvailable) {
        String id = booksDatabase.push().getKey();
        Book book = new Book(id, title, author,language,gener,image, isAvailable);
        booksDatabase.child(id).setValue(book).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddEditBook.this, "Book added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddEditBook.this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBook(String title, String author, String language, String gener, String image, boolean isAvailable) {
        Book book = new Book(bookId, title, author,language,gener, image, isAvailable);
        booksDatabase.child(bookId).setValue(book).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddEditBook.this, "Book updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddEditBook.this, "Failed to update book", Toast.LENGTH_SHORT).show();
            }
        });
    }
}