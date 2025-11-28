package com.example.raktindia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private CardView donorPanel;
    private CardView patientPanel;
    private CardView bloodBankPanel;
    private Button logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        donorPanel = findViewById(R.id.donor_panel_card);
        patientPanel = findViewById(R.id.patient_panel_card);
        bloodBankPanel = findViewById(R.id.blood_bank_panel_card);
        logoutBtn = findViewById(R.id.logout_btn);

        donorPanel.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, DonorActivity.class)));
        patientPanel.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, PatientActivity.class)));
        bloodBankPanel
                .setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, BloodBankActivity.class)));

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
