package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Subscriptions extends Fragment {

    EditText editTextAmount;
    Button btnFood, btnRent, btnShopping, btnUtilities, btnSubmit;
    String selectedCategory;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriptions, container, false);

        dbHelper = new DBHelper(getContext());

        editTextAmount = view.findViewById(R.id.username1);
        btnFood = view.findViewById(R.id.btnFood);
        btnRent = view.findViewById(R.id.btnRent);
        btnShopping = view.findViewById(R.id.btnShopping);
        btnUtilities = view.findViewById(R.id.btnUtilities);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnFood.setOnClickListener(v -> {
            selectedCategory = "Food";
            btnFood.setSelected(true);
            btnRent.setSelected(false);
            btnShopping.setSelected(false);
            btnUtilities.setSelected(false);
        });

        btnRent.setOnClickListener(v -> {
            selectedCategory = "Rent";
            btnFood.setSelected(false);
            btnRent.setSelected(true);
            btnShopping.setSelected(false);
            btnUtilities.setSelected(false);
        });

        btnShopping.setOnClickListener(v -> {
            selectedCategory = "Shopping";
            btnFood.setSelected(false);
            btnRent.setSelected(false);
            btnShopping.setSelected(true);
            btnUtilities.setSelected(false);
        });

        btnUtilities.setOnClickListener(v -> {
            selectedCategory = "Utilities";
            btnFood.setSelected(false);
            btnRent.setSelected(false);
            btnShopping.setSelected(false);
            btnUtilities.setSelected(true);
        });

        btnSubmit.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString().trim();

            if (amount.isEmpty() || selectedCategory == null) {
                Toast.makeText(getContext(), "Please enter amount and select a category", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = dbHelper.insertSubscription(amount, selectedCategory);
                if (inserted) {
                    Toast.makeText(getContext(), "Subscription added successfully", Toast.LENGTH_SHORT).show();
                    editTextAmount.setText("");
                    selectedCategory = null;
                } else {
                    Toast.makeText(getContext(), "Failed to add subscription", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
