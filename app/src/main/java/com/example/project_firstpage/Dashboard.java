
package com.example.project_firstpage;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.List;

public class Dashboard extends AppCompatActivity {


    private ListView bookListView;

    private List<Book> books;
    private BookAdapter adapter;


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


        //search box
        SearchView searchView = findViewById(R.id.search_view);


        bookListView = findViewById(R.id.bookListView);




        books = new ArrayList<>();
        adapter = new BookAdapter(this, books);
        bookListView.setAdapter(adapter);

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
                Toast.makeText(Dashboard.this, "Failed to load books", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + databaseError.getMessage(), databaseError.toException());
            }
        });
    }

    private void searchBooks(String searchQuery) {
        booksRef.orderByChild("title").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        Toast.makeText(Dashboard.this, "Search failed", Toast.LENGTH_SHORT).show();
                    }
                });
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
        } else if (itemId == R.id.menu_borrow) {
            fun_borrow();
            return true;
        } else {
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
    private void fun_borrow() {
        Intent intent = new Intent(Dashboard.this, BorrowActivity.class);
        startActivity(intent);
    }
}
