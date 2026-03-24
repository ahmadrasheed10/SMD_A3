package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SeatSelectionActivity extends AppCompatActivity {

    private final int PRICE = 500;

    private ArrayList<String> selectedSeats = new ArrayList<>();

    private ArrayList<String> bookedSeats = new ArrayList<>();

    TextView txtSelected;
    Button btnSnacks, btnBook;

    String movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        movie = getIntent().getStringExtra("movie");

        bookedSeats.add("A2");
        bookedSeats.add("B3");
        bookedSeats.add("C4");
        bookedSeats.add("D1");

        txtSelected = findViewById(R.id.txtSelected);
        btnSnacks = findViewById(R.id.btnSnacks);
        btnBook = findViewById(R.id.btnBook);
        TextView txtMovieName = findViewById(R.id.txtMovieName);
        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnBook.setEnabled(false);
        btnSnacks.setEnabled(false);

        movie = getIntent().getStringExtra("movie");
        if (movie != null)
            txtMovieName.setText(movie);

        int[] seats = {
                R.id.seat1, R.id.seat2, R.id.seat3, R.id.seat4, R.id.seat5, R.id.seat6,
                R.id.seat7, R.id.seat8, R.id.seat9, R.id.seat10, R.id.seat11, R.id.seat12,
                R.id.seat13, R.id.seat14, R.id.seat15, R.id.seat16, R.id.seat17, R.id.seat18,
                R.id.seat19, R.id.seat20, R.id.seat21, R.id.seat22, R.id.seat23, R.id.seat24,
                R.id.seat25, R.id.seat26, R.id.seat27, R.id.seat28, R.id.seat29, R.id.seat30,
                R.id.seat31, R.id.seat32, R.id.seat33, R.id.seat34, R.id.seat35, R.id.seat36
        };

        boolean isComingSoon = getIntent().getBooleanExtra("isComingSoon", false);
        String trailerUrl = getIntent().getStringExtra("trailerUrl");

        if (isComingSoon) {
            btnBook.setText("Coming Soon");
            btnBook.setEnabled(false);
            btnBook.setClickable(false);
            
            btnSnacks.setText("Watch Trailer");
            btnSnacks.setEnabled(true);
            btnSnacks.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.seat_selected));
            btnSnacks.setOnClickListener(v -> {
                if (trailerUrl != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(trailerUrl));
                    startActivity(intent);
                }
            });
        } else {
            btnBook.setOnClickListener(v -> {
                android.widget.Toast.makeText(this, "Booking Confirmed!", android.widget.Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TicketSummaryActivity.class);
                intent.putExtra("movie", movie);
                intent.putExtra("ticketTotal", selectedSeats.size() * PRICE);
                intent.putExtra("snacksTotal", 0);
                intent.putStringArrayListExtra("seatsList", selectedSeats);
                startActivity(intent);
            });

            btnSnacks.setOnClickListener(v -> {
                Intent intent = new Intent(this, SnacksActivity.class);
                intent.putExtra("movie", movie);
                intent.putStringArrayListExtra("seatsList", selectedSeats);
                intent.putExtra("ticketTotal", selectedSeats.size() * PRICE);
                intent.putStringArrayListExtra("seatsList", selectedSeats);
                startActivity(intent);
            });
        }

        for (int i = 0; i < seats.length; i++) {
            Button seat = findViewById(seats[i]);
            String label = "" + (char)('A' + (i / 6)) + ((i % 6) + 1);
            seat.setText(label);

            if (isComingSoon) {
                seat.setEnabled(false);
                seat.setClickable(false);
            } else {
                if (bookedSeats.contains(label)) {
                    seat.setEnabled(false);
                    seat.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.seat_booked));
                } else {
                    seat.setOnClickListener(v -> toggleSeat(seat, label));
                }
            }
        }
    }

        // Removed getSeatLabel since it is no longer needed

    private void toggleSeat(Button seat, String label) {

        if (seat.isSelected()) {

            seat.setSelected(false);
            seat.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_available));

            selectedSeats.remove(label);

        } else {

            seat.setSelected(true);
            seat.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_selected));

            selectedSeats.add(label);
        }

        txtSelected.setText("Selected Seats: " + selectedSeats.size());

        updateButtons();
    }

    private void updateButtons() {

        if (!selectedSeats.isEmpty()) {

            btnBook.setEnabled(true);
            btnSnacks.setEnabled(true);

            btnBook.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_booked));

            btnSnacks.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.seat_selected));

        }
        else {

            btnBook.setEnabled(false);
            btnSnacks.setEnabled(false);

            btnBook.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray));

            btnSnacks.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray));
        }
    }
}
