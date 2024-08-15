package com.example.project_firstpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminborrowbookAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;
    private DatabaseReference mBooksDatabase;

    public AdminborrowbookAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        mBooksDatabase = FirebaseDatabase.getInstance().getReference("books");
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
