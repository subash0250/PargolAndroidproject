package com.example.project_firstpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminBookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;
    private DatabaseReference mBooksDatabase;

    public AdminBookAdapter(Context context, List<Book> books){
        this.context = context;
        this.books = books;
        mBooksDatabase = FirebaseDatabase.getInstance().getReference("books");
    }
    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_book_item, parent, false);
        }
        final Book book = books.get(position);
        TextView bookTitle = convertView.findViewById(R.id.bookTitle);
        TextView bookAuthor = convertView.findViewById(R.id.bookAuthor);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        return convertView;
    }
}
