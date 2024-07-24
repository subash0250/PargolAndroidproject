
package com.example.project_firstpage;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_firstpage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private EditText searchBookEditText;
    private Button searchBookButton;
    private ListView bookListView;
    private ArrayAdapter<String> bookAdapter;
    private ArrayList<String> bookList;

    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // toolbar
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Event Planner");
        setSupportActionBar(toolbar);

        searchBookEditText = findViewById(R.id.searchBookEditText);
        searchBookButton = findViewById(R.id.searchBookButton);
        bookListView = findViewById(R.id.bookListView);

        bookList = new ArrayList<>();
        bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookList);
        bookListView.setAdapter(bookAdapter);

        booksRef = FirebaseDatabase.getInstance().getReference("books");

        loadBooks();

        searchBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = searchBookEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(searchQuery)) {
                    searchBooks(searchQuery);
                } else {
                    Toast.makeText(Dashboard.this, "Please enter text to  search ", Toast.LENGTH_SHORT).show();
                    Intent gotoprofile = new Intent(Dashboard.this, ProfilePage.class);
                    startActivity(gotoprofile);
                    finish();
                }

            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedBook = bookList.get(position);

                showBookDetails(selectedBook);
            }
        });
    }

    private void loadBooks() {
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    String bookTitle = bookSnapshot.child("title").getValue(String.class);
                    bookList.add(bookTitle);
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchBooks(String searchQuery) {
        booksRef.orderByChild("title").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookList.clear();
                        for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                            String bookTitle = bookSnapshot.child("title").getValue(String.class);
                            bookList.add(bookTitle);
                        }
                        bookAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Dashboard.this, "Search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showBookDetails(String bookTitle) {
        booksRef.orderByChild("title").equalTo(bookTitle)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                                String bookId = bookSnapshot.getKey();
                                String author = bookSnapshot.child("author").getValue(String.class);
                                boolean isAvailable = bookSnapshot.child("available").getValue(Boolean.class);

                                // Display book details and provide options to borrow, return, add to wishlist
                                // Show a dialog or another activity with these details and options
                                showBookOptions(bookId, bookTitle, author, isAvailable);
                            }
                        } else {
                            Toast.makeText(Dashboard.this, "Book details not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Dashboard.this, "Failed to retrieve book details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showBookOptions(String bookId, String bookTitle, String author, boolean isAvailable) {
        // Show book details in a dialog or another activity and provide options to:
        // 1. Borrow the book
        // 2. Return the book
        // 3. Add the book to the wishlist

        // Example: Borrow the book
        if (isAvailable) {
            booksRef.child(bookId).child("available").setValue(false);
            Toast.makeText(Dashboard.this, "Book borrowed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Dashboard.this, "Book is currently unavailable", Toast.LENGTH_SHORT).show();
        }

        // Example: Return the book
        if (!isAvailable) {
            booksRef.child(bookId).child("available").setValue(true);
            Toast.makeText(Dashboard.this, "Book returned successfully", Toast.LENGTH_SHORT).show();
        }

        // Example: Add to wishlist
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child("userId").child("wishlist");
        userWishlistRef.child(bookId).setValue(true);
        Toast.makeText(Dashboard.this, "Book added to wishlist", Toast.LENGTH_SHORT).show();
    }
}
