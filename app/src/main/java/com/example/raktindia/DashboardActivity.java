package com.example.raktindia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    private CardView donorPanel;
    private CardView patientPanel;
    private CardView bloodBankPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        donorPanel = findViewById(R.id.donor_panel_card);
        patientPanel = findViewById(R.id.patient_panel_card);
        bloodBankPanel = findViewById(R.id.blood_bank_panel_card);

        donorPanel.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, DonorActivity.class)));
        patientPanel.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, PatientActivity.class)));
        bloodBankPanel.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, BloodBankActivity.class)));
    }
}
