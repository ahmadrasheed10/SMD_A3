package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TicketSummaryActivity extends AppCompatActivity {

    String movie;
    int ticketTotal;
    int snacksTotal;
    ArrayList<String> seatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        movie = getIntent().getStringExtra("movie");
        ticketTotal = getIntent().getIntExtra("ticketTotal", 0);
        snacksTotal = getIntent().getIntExtra("snacksTotal", 0);
        seatsList = getIntent().getStringArrayListExtra("seatsList");

        ImageView imgMovie = findViewById(R.id.imgMovie);
        TextView txtMovie = findViewById(R.id.txtMovie);
        TextView txtDetails = findViewById(R.id.txtDetails);
        LinearLayout ticketContainer = findViewById(R.id.ticketContainer);
        LinearLayout snackContainer = findViewById(R.id.snackContainer);
        TextView snackTitle = findViewById(R.id.snackTitle);
        TextView txtGrandTotal = findViewById(R.id.txtGrandTotal);

        txtMovie.setText(movie);

        if (movie != null) {
            if (movie.equalsIgnoreCase("Oppenheimer")) {
                imgMovie.setImageResource(R.drawable.oppenheimer);
            } else if (movie.equalsIgnoreCase("Dr Strange")) {
                imgMovie.setImageResource(R.drawable.strange);
            } else if (movie.equalsIgnoreCase("3 Idiots")) {
                imgMovie.setImageResource(R.drawable.idiots);
            }
        }

        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        txtDetails.setText("Stars (90' Mall)  |  Hall 1 \n" + date + "  |  " + time);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TicketSummaryActivity.this, OnboardingActivity.class);

            startActivity(intent);
            finish();
        });

        if (seatsList != null && !seatsList.isEmpty()) {

            for (String s : seatsList) {

                TextView row = new TextView(this);
                row.setTextSize(15);

                row.setText("Row " + s.charAt(0) +
                        ", Seat " + s.substring(1) +
                        "                         RS 500");

                ticketContainer.addView(row);
            }
        }

        if (snacksTotal > 0) {

            snackTitle.setVisibility(View.VISIBLE);

            TextView snackRow = new TextView(this);
            snackRow.setTextSize(15);
            snackRow.setText("Snacks Total                         RS " + snacksTotal);

            snackContainer.addView(snackRow);
        }

        int grandTotal = ticketTotal + snacksTotal;
        txtGrandTotal.setText("TOTAL                         RS " + grandTotal);

        findViewById(R.id.btnSend).setOnClickListener(v -> sendTicket(grandTotal, date, time));
    }

    private void sendTicket(int grandTotal, String date, String time){

        StringBuilder seatText = new StringBuilder();
        for(String s : seatsList){
            seatText.append("Row ")
                    .append(s.charAt(0))
                    .append(", Seat ")
                    .append(s.substring(1))
                    .append("\n");
        }

        String message =
                " CineFAST Ticket\n\n" +
                        "Movie: " + movie + "\n" +
                        "Location: Stars Mall | Hall 1\n" +
                        "Date: " + date + "\n" +
                        "Time: " + time + "\n\n" +
                        seatText +
                        "\nTickets: RS " + ticketTotal +
                        "\nSnacks: RS " + snacksTotal +
                        "\n\nTOTAL: RS " + grandTotal;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your CineFAST Ticket");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(intent,"Send Ticket Via"));


    }
}
