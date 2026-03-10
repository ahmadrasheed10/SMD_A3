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
                R.id.seat1,R.id.seat2,R.id.seat3,R.id.seat4,R.id.seat5,R.id.seat6,
                R.id.seat7,R.id.seat8,R.id.seat9,R.id.seat10,R.id.seat11,R.id.seat12,
                R.id.seat13,R.id.seat14,R.id.seat15,R.id.seat16,R.id.seat17,R.id.seat18
        };

        for (int id : seats) {

            Button seat = findViewById(id);

            String label = getSeatLabel(id);
            seat.setText(label);

            if (bookedSeats.contains(label)) {

                seat.setEnabled(false);

                seat.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.seat_booked));

            } else {

                seat.setOnClickListener(v -> toggleSeat(seat, label));
            }
        }

        btnBook.setOnClickListener(v -> {

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

    private String getSeatLabel(int seatId) {

        if (seatId == R.id.seat1) return "A1";
        else if (seatId == R.id.seat2) return "A2";
        else if (seatId == R.id.seat3) return "A3";
        else if (seatId == R.id.seat4) return "A4";
        else if (seatId == R.id.seat5) return "A5";
        else if (seatId == R.id.seat6) return "B1";

        else if (seatId == R.id.seat7) return "B2";
        else if (seatId == R.id.seat8) return "B3";
        else if (seatId == R.id.seat9) return "B4";
        else if (seatId == R.id.seat10) return "B5";
        else if (seatId == R.id.seat11) return "C1";
        else if (seatId == R.id.seat12) return "C2";

        else if (seatId == R.id.seat13) return "C3";
        else if (seatId == R.id.seat14) return "C4";
        else if (seatId == R.id.seat15) return "C5";
        else if (seatId == R.id.seat16) return "D1";
        else if (seatId == R.id.seat17) return "C2";
        else if (seatId == R.id.seat18) return "D3";

        return "";
    }

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
