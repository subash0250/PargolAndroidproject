package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignOut extends AppCompatActivity {
    Button button_yes, button_no;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);

        mAuth = FirebaseAuth.getInstance();
        button_yes = findViewById(R.id.btn_yes);
        button_no = findViewById(R.id.btn_no);
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mAuth.signOut();
                Toast.makeText(SignOut.this, "Successfully Logout.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignOut.this, Login.class);
                startActivity(intent);
            }
        });
    }
}