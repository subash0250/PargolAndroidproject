package com.example.project_firstpage;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class page2 extends AppCompatActivity {

    EditText Uname,pwd;
    TextView tvforgotpwd,tvnewtolibrary;
    Button button;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        Uname = findViewById(R.id.Email);
        pwd = findViewById(R.id.password);
        button = findViewById(R.id.submitbutton);
        tvforgotpwd = findViewById(R.id.forgotpwd);
        tvnewtolibrary =findViewById(R.id.newtolibrary);
        mAuth= FirebaseAuth.getInstance();


button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        userlogin();

    }
});


        tvforgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent forgotpassword = new Intent(page2.this, Forgotpassword.class);
              startActivity(forgotpassword);
              finish();
            }
        });


        tvnewtolibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Accountcreationpage = new Intent(page2.this, Accountcreation.class);
                startActivity(Accountcreationpage);
                finish();
            }
        });


    }

//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentuser = mAuth.getCurrentUser();
//        if(currentuser != null)
//        {
//            Intent intent = new Intent(page2.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    public void userlogin(){

        String emailadd = Uname.getText().toString();
        String passcode = pwd.getText().toString();


        if(emailadd.isEmpty() || passcode.isEmpty())
        {
            Toast.makeText(page2.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailadd,passcode).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(page2.this, "login successfull", Toast.LENGTH_SHORT).show();

                    Intent k = new Intent(page2.this, Dashboard.class);
                    startActivity(k);
                    finish();

                    Uname.setText( " ");
                    pwd.setText(" ");

                }
            }
        });
    }
}