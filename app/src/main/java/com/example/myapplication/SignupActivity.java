package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

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

                        try {
                            usersRef = FirebaseDatabase.getInstance().getReference("users");
                        } catch (Exception e) {
                            Log.e(TAG, "Realtime Database is not configured", e);
                            Toast.makeText(
                                    this,
                                    "Account created, but Firebase Realtime Database is not configured yet.",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

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
                        Exception exception = task.getException();
                        Log.e(TAG, "Signup failed", exception);
                        String message = getSignupErrorMessage(exception);
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getSignupErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            if ("ERROR_INVALID_EMAIL".equals(errorCode)) {
                return "Please enter a valid email address.";
            }
            if ("ERROR_EMAIL_ALREADY_IN_USE".equals(errorCode)) {
                return "This email is already registered.";
            }
            if ("ERROR_WEAK_PASSWORD".equals(errorCode)) {
                return "Password is too weak.";
            }
            if ("ERROR_OPERATION_NOT_ALLOWED".equals(errorCode)
                    || "ERROR_INTERNAL_ERROR".equals(errorCode)) {
                return "Firebase Authentication is not configured. Enable Email/Password sign-in in Firebase Console.";
            }
        }

        if (exception instanceof FirebaseException && exception.getMessage() != null
                && exception.getMessage().contains("CONFIGURATION_NOT_FOUND")) {
            return "Firebase Authentication is not configured. Enable Email/Password sign-in in Firebase Console.";
        }

        return exception != null && exception.getMessage() != null
                ? exception.getMessage()
                : "Signup failed";
    }
}
