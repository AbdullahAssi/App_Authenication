package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the TextView after setContentView
        TextView welcome = findViewById(R.id.welcome);

        // Get the username from the Intent
        String username = getIntent().getStringExtra("username");

        // Set the welcome message with the username
        if (username != null && !username.isEmpty()) {
            welcome.setText("Welcome " + username);
        } else {
            welcome.setText("Welcome");
        }
    }
}
