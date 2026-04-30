package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyBookingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        TextView bookingDetails = view.findViewById(R.id.tvBookingDetails);
        view.findViewById(R.id.btnBackHome).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });

        SharedPreferences prefs = requireActivity().getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        String movie = prefs.getString("LastMovie", null);
        int seats = prefs.getInt("LastSeats", 0);
        int totalPrice = prefs.getInt("LastTotalPrice", 0);

        if (movie == null) {
            bookingDetails.setText("No bookings found yet.\nBook a movie from Home screen.");
        } else {
            String text = "Last Booking\n\n"
                    + "Movie: " + movie + "\n"
                    + "Seats: " + seats + "\n"
                    + "Total: RS " + totalPrice;
            bookingDetails.setText(text);
        }

        return view;
    }
}
