package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button m1_seats = findViewById(R.id.movie1_book);
        Button m1_trailer = findViewById(R.id.movie1_trailer);
        Button tomorrow = findViewById(R.id.btntomorrow);
        Button today = findViewById(R.id.btnToday);


        today.setOnClickListener(v -> {

            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_booked));

            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_available));
        });


        tomorrow.setOnClickListener(v -> {

            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_booked));

            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_available));
        });

        m1_seats.setOnClickListener(v -> openseats("The Oppenheimer"));
        m1_trailer.setOnClickListener(v -> opentrailer("https://www.youtube.com/watch?v=uYPbbksJxIg"));

        Button m2_seats = findViewById(R.id.movie2_book);
        Button m2_trailer = findViewById(R.id.movie2_trailer);

        m2_seats.setOnClickListener(v -> openseats("3 Idiots"));
        m2_trailer.setOnClickListener(v -> opentrailer("https://www.youtube.com/watch?v=K0eDlFX9GMc"));

        Button m3_seats = findViewById(R.id.movie3_book);
        Button m3_trailer = findViewById(R.id.movie3_trailer);

        m3_seats.setOnClickListener(v -> openseats("Dr Strange"));
        m3_trailer.setOnClickListener(v -> opentrailer("https://www.youtube.com/watch?v=aWzlQ2N6qqg"));

    }

    private void openseats(String movieName){
        Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
        intent.putExtra("movie", movieName);
        startActivity(intent);
    }

    private void opentrailer(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
