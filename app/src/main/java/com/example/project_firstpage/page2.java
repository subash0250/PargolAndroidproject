package com.example.project_firstpage;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class page2 extends AppCompatActivity {

    EditText Uname,pwd;
    TextView tvforgotpwd,tvnewtolibrary;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        Uname = findViewById(R.id.Email);
        pwd = findViewById(R.id.password);
        button = findViewById(R.id.submitbutton);
        tvforgotpwd = findViewById(R.id.forgotpwd);
        tvnewtolibrary =findViewById(R.id.newtolibrary);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailaddress = Uname.getText().toString();
                String pwdcode = pwd.getText().toString();


                if(emailaddress.isEmpty() || pwdcode.isEmpty()){

                    Toast.makeText(page2.this, "Uname or password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (emailaddress.equals("gaddamsubashreddy@gmail.com") && pwdcode.equals( "1234")) {

                    Toast.makeText(page2.this, "Login successful", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(page2.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvforgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resetpage = new Intent(page2.this, Forgotpassword.class);
                startActivity(resetpage);
            }
        });

        tvnewtolibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Accountcreationpage = new Intent(page2.this, Accountcreation.class);
                startActivity(Accountcreationpage);
            }
        });
    }
}