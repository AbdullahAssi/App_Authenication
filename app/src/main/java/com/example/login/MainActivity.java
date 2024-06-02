package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup;
    TextView signin;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signup = findViewById(R.id.btnsignup);
        signin = findViewById(R.id.btnsignin);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                // Defining the password pattern
                String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$";

                // Checking if the password matches the pattern
                Pattern pattern = Pattern.compile(passwordPattern);
                Matcher matcher = pattern.matcher(pass);

                // Password validation
                if (!matcher.matches()) {
                    Toast.makeText(MainActivity.this, "Password must be at least 8 characters long and include a mix of uppercase letters, lowercase letters, and special characters", Toast.LENGTH_SHORT).show();
                } else if(user.equals("") || pass.equals("") || repass.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if(pass.equals(repass)) {
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser) {
                            Boolean insert = DB.insertData(user, pass);
                            if(insert) {
                                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("username", user); // Passing the username
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
