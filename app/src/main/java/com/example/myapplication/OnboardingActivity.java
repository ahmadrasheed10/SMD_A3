package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button getStartedBtn = findViewById(R.id.get_started_btn);

        getStartedBtn.setOnClickListener(v -> {

            Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
            startActivity(intent);

        });
    }
}
