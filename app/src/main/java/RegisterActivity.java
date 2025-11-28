package com.example.raktindia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

// ध्यान दें: com.google.firebase.firestore.auth.User का गलत इम्पोर्ट हटा दिया गया है।
// अब यह आपकी खुद की बनाई हुई com.example.raktindia.User क्लास का उपयोग करेगा।

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button registerBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        roleSpinner = findViewById(R.id.roleSpinner);
        registerBtn = findViewById(R.id.registerBtn);

        setupRoleSpinner();

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void setupRoleSpinner() {
        String[] roles = {"Donor", "Patient", "Hospital"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        // इनपुट वैलिडेशन
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

        registerBtn.setEnabled(false); // प्रोसेस शुरू होने पर बटन को डिसेबल करें

        // Firebase Authentication के साथ यूजर बनाएं
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        // ऑथेंटिकेशन सफल होने पर, यूजर ऑब्जेक्ट बनाएं
                        User user = new User(name, email, role); // <- AtomicReference हटा दिया गया है

                        // Firebase Realtime Database में यूजर डेटा सेव करें
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(mAuth.getCurrentUser().getUid())
                                .setValue(user) // <- सीधे 'user' ऑब्जेक्ट को सेव करें
                                .addOnCompleteListener(dbTask -> {
                                    registerBtn.setEnabled(true); // प्रोसेस खत्म होने पर बटन को फिर से एनेबल करें
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "यूजर सफलतापूर्वक रजिस्टर हो गया है!", Toast.LENGTH_LONG).show();

                                        // लॉगिन स्क्रीन पर वापस जाएं
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // अगर डेटाबेस में सेव नहीं हुआ
                                        String errorMessage = dbTask.getException() != null ? dbTask.getException().getMessage() : "Unknown error";
                                        Toast.makeText(RegisterActivity.this, "यूजर डेटा सेव करने में विफल! " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        // अगर यूजर बनाने में ही कोई समस्या आई
                        registerBtn.setEnabled(true);
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(RegisterActivity.this, "रजिस्टर करने में विफल! " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
