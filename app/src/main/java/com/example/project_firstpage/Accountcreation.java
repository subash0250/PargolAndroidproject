package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Accountcreation extends AppCompatActivity {

    EditText edt1, edt2, edt3, edt4, edt5;
    Spinner roleSpinner;
    Button button;
    TextView txtvw;
    FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountcreation);

        edt1 = findViewById(R.id.createAccountemail);
        edt2 = findViewById(R.id.FirstName);
        edt3 = findViewById(R.id.lastname);
        edt4 = findViewById(R.id.passwordcreatin);
        edt5 = findViewById(R.id.Confirmpassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        button = findViewById(R.id.accountsubmitbutton);
        txtvw = findViewById(R.id.backtoLogin);

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistration();
            }
        });

        txtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(Accountcreation.this, Login.class);
                startActivity(j);
                finish();
            }
        });
    }

    private void UserRegistration() {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        String emailaddress = edt1.getText().toString();
        String Fname = edt2.getText().toString();
        String lName = edt3.getText().toString();
        String pwdcode = edt4.getText().toString();
        String cpwd = edt5.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        if (emailaddress.isEmpty() || Fname.isEmpty() || lName.isEmpty() || pwdcode.isEmpty() || cpwd.isEmpty()) {
            Toast.makeText(Accountcreation.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwdcode.equals(cpwd)) {
            edt5.setError("Password does not match");
            edt5.requestFocus();
            return;
        }

        if (pwdcode.length() < 8) {
            edt5.setError("Password should contain more than 8 characters");
            edt5.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            edt1.setError("Email should be in correct format");
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailaddress, pwdcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    User user = new User(Fname, lName, emailaddress, role);
                    mUsersDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Accountcreation.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Accountcreation.this, Login.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Accountcreation.this, "Failed to store user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Accountcreation.this, "Authentication failed", Toast.LENGTH_SHORT).show();
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
