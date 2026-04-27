package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEt;
    private EditText passwordEt;
    private CheckBox rememberMeCb;
    private FirebaseAuth firebaseAuth;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        emailEt = findViewById(R.id.et_email);
        passwordEt = findViewById(R.id.et_password);
        rememberMeCb = findViewById(R.id.cb_remember_me);
        Button loginBtn = findViewById(R.id.btn_login);
        TextView registerNowTv = findViewById(R.id.tv_register_now);
        ImageView backBtn = findViewById(R.id.iv_back);

        backBtn.setOnClickListener(v -> finish());
        registerNowTv.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (rememberMeCb.isChecked()) {
                            sessionManager.saveLoginSession(email);
                        } else {
                            sessionManager.clearSession();
                        }

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Log.d("CineFAST", "User logged in successfully");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        String message = task.getException() != null
                                ? task.getException().getMessage()
                                : "Login failed";
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
