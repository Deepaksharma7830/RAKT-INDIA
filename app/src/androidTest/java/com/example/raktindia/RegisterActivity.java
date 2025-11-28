package com.example.raktindia;

// सभी जरूरी क्लास को इम्पोर्ट करें
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {

    // लेआउट के अनुसार सभी व्यूज को डिक्लेयर करें
    private EditText nameEditText, emailEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button registerBtn;
    private ProgressBar progressBar; // लोडिंग दिखाने के लिए

    // Firebase की जरूरी सर्विसेज़ को डिक्लेयर करें
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // लेआउट फ़ाइल को सेट करें (यह activity_register.xml को लोड करेगा)
        setContentView(R.layout.activity_register);

        // Firebase को इनिशियलाइज़ करें
        mAuth = FirebaseAuth.getInstance();

        // सभी व्यूज को उनकी ID से लिंक करें
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerBtn = findViewById(R.id.registerBtn);
        // अगर आपने लेआउट में ProgressBar नहीं बनाया है, तो इस लाइन को हटा दें
        // progressBar = findViewById(R.id.progressBar);

        // Spinner को सेट करें
        setupRoleSpinner();

        // रजिस्टर बटन का क्लिक लिस्नर
        registerBtn.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void setupRoleSpinner() {
        // Spinner के लिए विकल्प (Options)
        String[] roles = {"Donor", "Patient", "Hospital"};
        // अडैप्टर बनाएं
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // अडैप्टर को Spinner से जोड़ें
        roleSpinner.setAdapter(adapter);
    }

    private void registerUser() {
        // यूजर से इनपुट लें और खाली स्पेस हटा दें
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        // --- इनपुट की जांच ---
        if (name.isEmpty()) {
            nameEditText.setError("नाम डालना अनिवार्य है!");
            nameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("ईमेल डालना अनिवार्य है!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("कृपया सही ईमेल डालें!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("पासवर्ड डालना अनिवार्य है!");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("पासवर्ड कम से कम 6 अक्षरों का होना चाहिए!");
            passwordEditText.requestFocus();
            return;
        }

        // लोडिंग दिखाना और बटन को डिसेबल करना
        if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);

        // Firebase Authentication का उपयोग करके नया यूजर बनाएं
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // यूजर की जानकारी को एक ऑब्जेक्ट में डालें
                        User user = new User(name, email, role);

                        // यूजर की जानकारी को Realtime Database में सेव करें
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    // लोडिंग बंद करें और बटन को फिर से एनेबल करें
                                    if(progressBar != null) progressBar.setVisibility(View.GONE);
                                    registerBtn.setEnabled(true);

                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "यूजर सफलतापूर्वक रजिस्टर हो गया है!", Toast.LENGTH_LONG).show();

                                        // यूजर को लॉगिन स्क्रीन पर भेजें
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        // पुरानी सभी एक्टिविटी को हटा दें
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "यूजर डेटा सेव करने में विफल! " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // अगर रजिस्ट्रेशन फेल हो जाए
                        if(progressBar != null) progressBar.setVisibility(View.GONE);
                        registerBtn.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "रजिस्टर करने में विफल! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
