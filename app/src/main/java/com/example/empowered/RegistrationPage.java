package com.example.empowered;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationPage extends AppCompatActivity {
    Button btn_signup;
    EditText user_name, pass_word;
    FirebaseAuth mAuth;
    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1A759F")));

        user_name=findViewById(R.id.username);
        pass_word=findViewById(R.id.password1);
        btn_signup=findViewById(R.id.sign);
        btn_signin=findViewById(R.id.accountexist);
        mAuth=FirebaseAuth.getInstance();

        //simple regex in order to check if password has
        //an uppercase,lowercase, special character and a number
        String regex = "^(?=.*[a-z])(?=."
                + "*[A-Z])(?=.*\\d)"
                + "(?=.*[-+_!@#$%^&*., ?]).+$";

        //compiling the regex in a pattern
        Pattern passwordPattern = Pattern.compile(regex);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_name.getText().toString().trim();
                String password= pass_word.getText().toString().trim();

                //creating a matcher to find match between given password
                //and the regex expression
                Matcher matcherP = passwordPattern.matcher(password);

                if(email.isEmpty())
                {
                    user_name.setError("Email is empty");
                    user_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    user_name.setError("Enter a valid email address");
                    user_name.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    pass_word.setError("Enter a password");
                    pass_word.requestFocus();
                    return;
                }
                if(password.length()<9)
                {
                    pass_word.setError("Length of the password should be at least 10 characters long");
                    pass_word.requestFocus();
                    return;
                }
                if(!matcherP.matches()){

                    pass_word.setError("The password has to have the following: Uppercase letter, number and special character");
                    pass_word.requestFocus();
                    return;

                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegistrationPage.this,"You are Registered !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationPage.this, LoginPage.class));
                    }
                    else
                    {
                        Toast.makeText(RegistrationPage.this,"There has been a mistake, try again !",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_signin.setOnClickListener(v -> startActivity(new Intent(RegistrationPage.this,LoginPage.class )));

    }
}