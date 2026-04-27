package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEt;
    private EditText emailEt;
    private EditText passwordEt;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        nameEt = findViewById(R.id.et_name);
        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        Button signupBtn = findViewById(R.id.btn_signup);
        ImageView backBtn = findViewById(R.id.iv_back);

        backBtn.setOnClickListener(v -> finish());
        signupBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser == null) {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = firebaseUser.getUid();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("uid", uid);

                        usersRef.child(uid).setValue(userData).addOnCompleteListener(saveTask -> {
                            if (saveTask.isSuccessful()) {
                                new SessionManager(this).saveLoginSession(email);
                                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                String message = saveTask.getException() != null
                                        ? saveTask.getException().getMessage()
                                        : "Failed to store user data";
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        String message = task.getException() != null
                                ? task.getException().getMessage()
                                : "Signup failed";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
