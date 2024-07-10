package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class AdminDashboardActivity extends AppCompatActivity {

    private SearchView searchView;
    private Button addBookButton;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseReference booksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        searchView = findViewById(R.id.searchView);
        addBookButton = findViewById(R.id.addBookButton);
        recyclerView = findViewById(R.id.recyclerView);

        booksDatabase = FirebaseDatabase.getInstance().getReference("books");
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);

        loadBooks();

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, AddEditBook.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return false;
            }
        });
    }

    private void loadBooks() {
        booksDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    bookList.add(book);
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterBooks(String query) {
        List<Book> filteredBooks = new ArrayList<>();
        for (Book book : bookList) {
            if (!TextUtils.isEmpty(book.getTitle()) && book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        bookAdapter.updateList(filteredBooks);
    }
}
