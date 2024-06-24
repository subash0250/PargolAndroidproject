package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Accountcreation extends AppCompatActivity {

    EditText edt1, edt2, edt3, edt4, edt5;

    Button button;
    TextView txtvw;
    FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accountcreation);

        edt1 = findViewById(R.id.createAccountemail);
        edt2 = findViewById(R.id.FirstName);
        edt3 = findViewById(R.id.lastname);
        edt4 = findViewById(R.id.passwordcreatin);
        edt5 = findViewById(R.id.Confirmpassword);
        button = findViewById(R.id.accountsubmitbutton);
        txtvw = findViewById(R.id.backtoLogin);
        mAuth = FirebaseAuth.getInstance();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRegistration();

            }
        });


        txtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Accountcreation.this, page2.class);

                startActivity(j);
                finish();
            }
        });

    }

    private void UserRegistration() {

        String emailaddress = edt1.getText().toString();
        String Fname = edt2.getText().toString();
        String lName = edt3.getText().toString();
        String pwdcode = edt4.getText().toString();
        String cpwd = edt5.getText().toString();

        if (emailaddress.isEmpty() || Fname.isEmpty() || lName.isEmpty() || pwdcode.isEmpty() || cpwd.isEmpty()) {

            Toast.makeText(Accountcreation.this, "Enter all the feilds", Toast.LENGTH_SHORT).show();

//                    beacause of text already  exist, if is not running ,need to ask

        }

        if (!pwdcode.equals(cpwd)) {
            edt5.setError("password does not Match");
            edt5.requestFocus();
            return;
        }
        if (pwdcode.length() < 8) {

            edt5.setError("Password should contain more than 8 characters");
            edt5.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            edt1.setError("email should be in format");
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(emailaddress, pwdcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(Accountcreation.this, "Account created successfully", Toast.LENGTH_SHORT).show();
//                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent i = new Intent(Accountcreation.this, page2.class);
                        startActivity(i);
                        finish();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Accountcreation.this, "Sign Up failed", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

}