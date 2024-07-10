package com.example.project_firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    EditText newpwd,confirmationpwd;

    Button passwordconfirmbutton,cancelbutton;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);

        newpwd = findViewById(R.id.newpwd);
        confirmationpwd = findViewById(R.id.confirmpwd);
        passwordconfirmbutton = findViewById(R.id.resetpwdbtn);
        cancelbutton = findViewById(R.id.cancelbtn);
        mAuth = FirebaseAuth.getInstance();


        passwordconfirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Resetpassword();

            }
        });
       cancelbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent cancelactivity = new Intent(Forgotpassword.this, Login.class);
               startActivity(cancelactivity);
               finish();
           }
       });
    }



    private void Resetpassword()
    {
        String newpassword = newpwd.getText().toString();
        String confirmpassword= confirmationpwd.getText().toString();

        if(newpassword.isEmpty() || confirmpassword.isEmpty()){

            Toast.makeText(Forgotpassword.this, "Both feilds are mandatory", Toast.LENGTH_SHORT).show();

        }

        else if (!newpassword.equals(confirmpassword)) {

            confirmationpwd.setError("Password did not match");
            confirmationpwd.requestFocus();
            return;

//                   but the password in main page should also update(pwdcode in page2.class) how ??
        } else if (newpassword.length()<8) {

            newpwd.setError("password length is 8 characters");
            newpwd.requestFocus();

        }
        else {

            mAuth.confirmPasswordReset(newpassword,confirmpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(Forgotpassword.this, " Congratulations..!,Password changed", Toast.LENGTH_SHORT).show();

                        newpwd.setText("");
                        confirmationpwd.setText("");
                        Intent i = new Intent(Forgotpassword.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            });

        }


    }
}