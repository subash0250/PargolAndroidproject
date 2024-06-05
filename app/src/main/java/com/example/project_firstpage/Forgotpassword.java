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

public class Forgotpassword extends AppCompatActivity {

    EditText newpwd,confirmationpwd;

    Button passwordconfirmbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);

        newpwd = findViewById(R.id.newpwd);
        confirmationpwd = findViewById(R.id.confirmpwd);
        passwordconfirmbutton = findViewById(R.id.resetpwdbtn);

        passwordconfirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpasswordvariable = newpwd.getText().toString();
                String confirmpasswordvariable = confirmationpwd.getText().toString();

                if(newpasswordvariable.isEmpty() || confirmpasswordvariable.isEmpty()){

                    Toast.makeText(Forgotpassword.this, "Both feilds are mandatory", Toast.LENGTH_SHORT).show();

                }

                else if (newpasswordvariable.equals(confirmpasswordvariable)) {

                    newpwd.setText("");
                    confirmationpwd.setText("");

                    Toast.makeText(Forgotpassword.this, "Password has been updated", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Forgotpassword.this, page2.class);
                    startActivity(i);
//                   but the password in main page should also update(pwdcode in page2.class) how ??
                }

                else {

                    Toast.makeText(Forgotpassword.this, "Password didnot match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}