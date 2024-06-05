package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Accountcreation extends AppCompatActivity {

    EditText edt1,edt2,edt3,edt4;

   Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accountcreation);

        edt1 = findViewById(R.id.createAccountemail);
        edt2 = findViewById(R.id.FirstName);
        edt3 = findViewById(R.id.lastname);
        edt4 = findViewById(R.id.passwordcreatin);
        button = findViewById(R.id.accountsubmitbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Accountemail = edt1.getText().toString();
                String Fname = edt2.getText().toString();
                String lName = edt3.getText().toString();
                String Password = edt4.getText().toString();

                if (Accountemail.isEmpty() || Fname.isEmpty() || lName.isEmpty() || Password.isEmpty()) {

                    Toast.makeText(Accountcreation.this, "Enter all the feilds", Toast.LENGTH_SHORT).show();

//                    beacause of text already  exist, if is not running ,need to ask

                }
                else {

                    Toast.makeText(Accountcreation.this, "Account created successfully, Login now", Toast.LENGTH_SHORT).show();

                    Intent backtologinpage = new Intent(Accountcreation.this, page2.class);

                    startActivity(backtologinpage);

                }
            }
        });


    }
}