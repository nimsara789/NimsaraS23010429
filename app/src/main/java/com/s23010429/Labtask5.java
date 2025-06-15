package com.example.s23010429;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteException;

public class Labtask5 extends AppCompatActivity {
    // UI elements
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    // Database helper
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to use the existing layout
        setContentView(R.layout.activity_multimedia_app);

        // Initialize database helper
        dbHelper = new MyDatabaseHelper(this);

        try {
            // Open database to ensure it's created
            dbHelper.open();
            Log.d("Labtask5", "Database opened successfully");
        } catch (SQLiteException e) {
            Log.e("Labtask5", "Database error on open: " + e.getMessage());
        }

        // Initialize UI elements
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    // Method to save data to database
    private void saveUserData() {
        // Get text from input fields
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_LONG).show();
            return;
        }

        // Try to save the data to database
        try {
            // Ensure database is open
            dbHelper.open();

            // Insert data and get the result ID
            long id = dbHelper.insertUserData(username, password);

            if (id != -1) {
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_LONG).show();

                // Clear input fields after successful save
                usernameInput.setText("");
                passwordInput.setText("");
            } else {
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Labtask5", "Error saving data: " + e.getMessage());
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Close database connection when activity is destroyed
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}