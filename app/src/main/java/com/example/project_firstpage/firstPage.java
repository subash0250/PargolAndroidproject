package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class firstPage extends AppCompatActivity {

    Button beginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
beginbtn = findViewById(R.id.Beginpage);

beginbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(firstPage.this, Login.class);
        startActivity(i);
        finish();
    }
});


    }
}