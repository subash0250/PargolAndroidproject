
package com.example.project_firstpage;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_firstpage.R;
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

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {


    private ListView bookListView;
    private ArrayAdapter<String> bookAdapter;
    private ArrayList<String> bookList;

    private DatabaseReference booksRef;
    FirebaseAuth mAuth;
    private  String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }



        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Central Library");
        setSupportActionBar(toolbar);
        // finished

        //search box
        SearchView searchView = findViewById(R.id.search_view);


        bookListView = findViewById(R.id.bookListView);


        bookList = new ArrayList<>();
        bookAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookList);
        bookListView.setAdapter(bookAdapter);

        booksRef = FirebaseDatabase.getInstance().getReference("books");

        loadBooks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchBooks(query);
                return true;
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
                                // String bookId = bookSnapshot.getKey();
                                String author = bookSnapshot.child("author").getValue(String.class);
                                String gener = bookSnapshot.child("gener").getValue(String.class);
                                String id = bookSnapshot.child("id").getValue(String.class);
                                String image = bookSnapshot.child("image").getValue(String.class);
                                boolean isAvailable = bookSnapshot.child("isAvailable").getValue(Boolean.class);
                                String language = bookSnapshot.child("language").getValue(String.class);
                                String title = bookSnapshot.child("title").getValue(String.class);
                                showBookOptions(id, title, author, language, gener, image, isAvailable);
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
    private void showBookOptions(String id, String title, String author, String language, String gener, String image, Boolean isAvailable) {
        // Show book details in a dialog or another activity and provide options to:
        // 1. Borrow the book
        // 2. Return the book
        // 3. Add the book to the wishlist
        if (isAvailable) {
            booksRef.child(id).child("isAvailable").setValue(false);
            Book book = new Book(id, title, author, language, gener, image, false);
            DatabaseReference userBorrowRef = FirebaseDatabase.getInstance().getReference("borrow").child(userId).child(id);
            userBorrowRef.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Dashboard.this, "Book borrowed successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Dashboard.this, "Book borrow failed", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(Dashboard.this, "Book is currently unavailable", Toast.LENGTH_SHORT).show();
        }

        // Example: Return the book
        if (!isAvailable) {
//            booksRef.child(bookId).child("isAvailable").setValue(true);
//            Toast.makeText(Dashboard.this, "Book returned successfully", Toast.LENGTH_SHORT).show();
        }

        // Example: Add to wishlist
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child("userId").child("wishlist");
//        userWishlistRef.child(bookId).setValue(true);
//        Toast.makeText(Dashboard.this, "Book added to wishlist", Toast.LENGTH_SHORT).show();
    }

    @Override
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
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void fun_sign_out() {
        Intent intent = new Intent(Dashboard.this, SignOut.class);
        startActivity(intent);
    }

    private void fun_about() {
        Intent intent = new Intent(Dashboard.this, About.class);
        startActivity(intent);
    }

    private void fun_profile() {
        Intent intent = new Intent(Dashboard.this, Profile.class);
        startActivity(intent);
    }
}
