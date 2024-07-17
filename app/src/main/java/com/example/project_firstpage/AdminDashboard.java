package com.example.project_firstpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {
    private static final String TAG = "AdminDashboard";
    Button add_book_btn;
    private ListView bookListView;
    private AdminBookAdapter adapter;
    private List<Book> books;

    private DatabaseReference booksRef;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        add_book_btn = findViewById(R.id.add_book_btn);


        booksRef = FirebaseDatabase.getInstance().getReference("books");
        bookListView = findViewById(R.id.bookListView);

        books = new ArrayList<>();
        adapter = new AdminBookAdapter(this, books);
        bookListView.setAdapter(adapter);

        loadBooks();

        add_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashboard.this, AddEditBook.class);
                startActivity(intent);
            }
        });
    }

    private void loadBooks() {
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null) {
                                book.setId(snapshot.getKey());
                                books.add(book);
                            } else {
                                Log.e(TAG, "Book is null for snapshot: " + snapshot);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error retrieving book from snapshot: " + snapshot, e);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDashboard.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }
}
