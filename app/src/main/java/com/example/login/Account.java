package com.example.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Account extends Fragment {

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private TextView welcome, mail, phone1;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initializing SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, getActivity().MODE_PRIVATE);

        // Setting up the welcome text and other TextViews
        welcome = view.findViewById(R.id.welcome);
        mail = view.findViewById(R.id.email);
        phone1 = view.findViewById(R.id.phone);

        // Initializing DBHelper
        dbHelper = new DBHelper(getContext());

        // Fetching username from SharedPreferences
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        if (!username.isEmpty()) {
            // Fetching user details from database
            Cursor cursor = dbHelper.getUserData(username);
            if (cursor != null && cursor.moveToFirst()) {
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String username1 = cursor.getString(cursor.getColumnIndex("username"));
                String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String lastname = cursor.getString(cursor.getColumnIndex("lastname"));

                // Updating UI with user data
                welcome.setText("" + firstname + " " + lastname);
                mail.setText(email);
                phone1.setText(phone);
                cursor.close();
            }
        } else {
            welcome.setText("Welcome");
        }

        // Setting up the logout button
        Button btnLogout = view.findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear login state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_IS_LOGGED_IN, false);
                editor.putString(KEY_USERNAME, null);
                editor.apply();

                // Redirect to MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish(); // Close the current activity
            }
        });

        return view;
    }
}
