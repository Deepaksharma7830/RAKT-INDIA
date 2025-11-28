package com.example.raktindia;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PatientActivity extends AppCompatActivity {

    private EditText etBloodGroup, etLocation;
    private Button btnRequestBlood;

    private DatabaseReference requestDatabase;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        // Views को उनकी ID से लिंक करें
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etLocation = findViewById(R.id.etLocation);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);

        // Firebase का इंस्टैंस लें
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // अगर कोई यूजर लॉग इन नहीं है, तो उसे वापस भेज दें
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Firebase Realtime Database में "BloodRequests" नाम का एक reference बनाएं
        requestDatabase = FirebaseDatabase.getInstance().getReference("BloodRequests");

        // रिक्वेस्ट बटन का क्लिक लिस्नर
        btnRequestBlood.setOnClickListener(v -> {
            createBloodRequest();
        });
    }

    private void createBloodRequest() {
        String requiredBloodGroup = etBloodGroup.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        // इनपुट की जांच
        if (TextUtils.isEmpty(requiredBloodGroup)) {
            etBloodGroup.setError("Required Blood Group is mandatory");
            etBloodGroup.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            etLocation.setError("Location is mandatory");
            etLocation.requestFocus();
            return;
        }

        btnRequestBlood.setEnabled(false); // बटन को डिसेबल करें

        // एक यूनिक ID के साथ रिक्वेस्ट बनाएं
        String requestId = requestDatabase.push().getKey();

        // डेटा को HashMap में डालें
        HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("requiredBloodGroup", requiredBloodGroup);
        requestInfo.put("location", location);
        requestInfo.put("requesterId", currentUser.getUid()); // रिक्वेस्ट करने वाले की ID
        requestInfo.put("status", "Pending"); // रिक्वेस्ट का स्टेटस

        // डेटा को Firebase में सेव करें
        if (requestId != null) {
            requestDatabase.child(requestId).setValue(requestInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PatientActivity.this, "Blood request sent successfully!", Toast.LENGTH_LONG).show();
                        finish(); // काम होने के बाद इस एक्टिविटी को बंद कर दें
                    } else {
                        Toast.makeText(PatientActivity.this, "Failed to send request. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    btnRequestBlood.setEnabled(true); // बटन को फिर से एनेबल करें
                }
            });
        }
    }
}
