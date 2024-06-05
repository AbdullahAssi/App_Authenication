package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword, firstname, lastname, email, phone, dob;
    Button signup;
    TextView signin;
    DBHelper DB;
    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            String user = sharedPreferences.getString(KEY_USERNAME, null);
            if (user != null) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("username", user); // Passing the username
                startActivity(intent);
                finish();
                return; // Return here to prevent running the rest of the code in onCreate
            }
        }

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        dob = findViewById(R.id.dob);
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
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String mail = email.getText().toString();
                String ph = phone.getText().toString();
                String date = dob.getText().toString();
                String repass = repassword.getText().toString();

                // Defining the password pattern
                String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$";

                // Checking if the password matches the pattern
                Pattern pattern = Pattern.compile(passwordPattern);
                Matcher matcher = pattern.matcher(pass);

                // Password validation
                if (!matcher.matches()) {
                    Toast.makeText(MainActivity.this, "Password must be at least 8 characters long and include a mix of uppercase letters, lowercase letters, and special characters", Toast.LENGTH_SHORT).show();
                } else if (user.equals("") || pass.equals("") || repass.equals("") || fname.equals("") || lname.equals("") || mail.equals("") || ph.equals("") || date.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = DB.checkusername(user);
                        if (!checkuser) {
                            Boolean insert = DB.insertData(fname, lname, user, mail, date, ph, pass);
                            if (insert) {
                                Log.i(TAG, "User registered successfully: " + user);
                                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                                // Save login state in SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                editor.putString(KEY_USERNAME, user);
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("username", user); // Passing the username
                                startActivity(intent);
                                finish(); // Close the current activity
                            } else {
                                Log.e(TAG, "Registration failed: insertData returned false");
                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "Username already taken: " + user);
                            Toast.makeText(MainActivity.this, "User Name is taken, please try another.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Passwords do not match for user: " + user);
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
