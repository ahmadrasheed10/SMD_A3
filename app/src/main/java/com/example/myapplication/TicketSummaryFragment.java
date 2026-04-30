package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TicketSummaryFragment extends Fragment {

    String movie;
    int ticketTotal;
    int snacksTotal;
    ArrayList<String> seatsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        if (getArguments() != null) {

            movie = getArguments().getString("movie");
            ticketTotal = getArguments().getInt("ticketTotal", 0);
            snacksTotal = getArguments().getInt("snacksTotal", 0);
            seatsList = getArguments().getStringArrayList("seatsList");
        
        }

        ImageView imgMovie = view.findViewById(R.id.imgMovie);
        TextView txtMovie = view.findViewById(R.id.txtMovie);
        TextView txtDetails = view.findViewById(R.id.txtDetails);
        LinearLayout ticketContainer = view.findViewById(R.id.ticketContainer);
        LinearLayout snackContainer = view.findViewById(R.id.snackContainer);
        TextView snackTitle = view.findViewById(R.id.snackTitle);
        TextView txtGrandTotal = view.findViewById(R.id.txtGrandTotal);

        txtMovie.setText(movie);

        if (movie != null) {
            if (movie.equalsIgnoreCase("Oppenheimer") || movie.equalsIgnoreCase("The Oppenheimer") || movie.equalsIgnoreCase("Oppenheimer 2")) {
                imgMovie.setImageResource(R.drawable.oppenheimer);
            } else if (movie.equalsIgnoreCase("Dr Strange") || movie.equalsIgnoreCase("Dr Strange 3")) {
                imgMovie.setImageResource(R.drawable.strange);
            } else if (movie.equalsIgnoreCase("3 Idiots") || movie.equalsIgnoreCase("3 Idiots Returns")) {
                imgMovie.setImageResource(R.drawable.idiots);
            }
        }

        long bookingTimestamp = System.currentTimeMillis() + (60L * 60L * 1000L);
        Date bookingDate = new Date(bookingTimestamp);
        String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(bookingDate);
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(bookingDate);

        txtDetails.setText("Stars (90' Mall)  |  Hall 1 \n" + date + "  |  " + time);

        ImageButton btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });

        if (seatsList != null && !seatsList.isEmpty()) {

            for (String s : seatsList) {

                TextView row = new TextView(requireContext());
                row.setTextSize(15);

                row.setText("Row " + s.charAt(0) +
                        ", Seat " + s.substring(1) +
                        "                         RS 500");

                ticketContainer.addView(row);
            }
        }

        if (snacksTotal > 0) {

            snackTitle.setVisibility(View.VISIBLE);

            TextView snackRow = new TextView(requireContext());
            snackRow.setTextSize(15);
            snackRow.setText("Snacks Total                         RS " + snacksTotal);

            snackContainer.addView(snackRow);
        }

        int grandTotal = ticketTotal + snacksTotal;
        txtGrandTotal.setText("TOTAL                         RS " + grandTotal);

        android.content.SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("BookingPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        if (movie != null) {
            editor.putString("LastMovie", movie);
            editor.putInt("LastSeats", seatsList != null ? seatsList.size() : 0);
            editor.putInt("LastTotalPrice", grandTotal);
            editor.apply();
        }

        saveBookingToFirebase(grandTotal, date, time, bookingTimestamp);
        view.findViewById(R.id.btnSend).setOnClickListener(v -> sendTicket(grandTotal, date, time));

        return view;
    }

    private void saveBookingToFirebase(int grandTotal, String date, String time, long bookingTimestamp) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || movie == null) {
            return;
        }

        String userId = user.getUid();
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(userId);
        String bookingId = bookingsRef.push().getKey();
        if (bookingId == null) {
            return;
        }

        ArrayList<String> safeSeats = seatsList != null ? seatsList : new ArrayList<>();
        Booking booking = new Booking(
                bookingId,
                userId,
                movie,
                safeSeats,
                grandTotal,
                date,
                time,
                bookingTimestamp
        );
        bookingsRef.child(bookingId).setValue(booking);
    }

    private void sendTicket(int grandTotal, String date, String time) {

        StringBuilder seatText = new StringBuilder();
        if (seatsList != null) {
            for (String s : seatsList) {
                seatText.append("Row ")
                        .append(s.charAt(0))
                        .append(", Seat ")
                        .append(s.substring(1))
                        .append("\n");
            }
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

        startActivity(Intent.createChooser(intent, "Send Ticket Via"));
    }
}
