package com.example.raktindia;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView homeTitle;
    private EditText searchBox;
    private EditText emailBox;
    private EditText passwordBox;
    private EditText nameBox;
    private Button submitBtn;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        homeTitle = findViewById(R.id.homeTitle);
        searchBox = findViewById(R.id.searchBox);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        nameBox = findViewById(R.id.nameBox);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });
    }

    /**

     */
    private void handleFormSubmission() {

        String searchText = searchBox.getText().toString().trim();
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();
        String name = nameBox.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(HomeActivity.this, "Please fill all required fields (Email, Password, Name)", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Search Query: " + searchText);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Password: " + password);
        Log.d(TAG, "Name: " + name);

        Toast.makeText(HomeActivity.this, "Information Submitted Successfully!", Toast.LENGTH_LONG).show();

    }
}
