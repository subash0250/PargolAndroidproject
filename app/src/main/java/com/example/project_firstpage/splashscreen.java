package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splashscreen extends AppCompatActivity {


    int spashscreentimeout =2000;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);

        int splashscreentimer = 5000;

        imageView = findViewById(R.id.splashscreen);

new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
Intent i = new Intent(splashscreen.this, MainActivity.class);
startActivity(i);
    }
},splashscreentimer);

    }
}