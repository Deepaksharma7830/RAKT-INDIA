package com.example.raktindia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DonorActivity extends AppCompatActivity {

    private EditText etBloodGroup, etLocation, etLastDonation, etHealth;
    private Button btnUpdateDonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        // IDs check karo aur exist karni chahiye XML me
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etLocation = findViewById(R.id.etLocation);
        etLastDonation = findViewById(R.id.etLastDonation);
        etHealth = findViewById(R.id.etHealth);
        btnUpdateDonor = findViewById(R.id.btnUpdateDonor);

        // Button click listener
        btnUpdateDonor.setOnClickListener(v ->
                Toast.makeText(DonorActivity.this, "Donor info saved (demo)", Toast.LENGTH_SHORT).show());
    }
}
