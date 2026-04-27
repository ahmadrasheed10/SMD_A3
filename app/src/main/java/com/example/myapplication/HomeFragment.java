package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        android.widget.ImageView btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> showPopupMenu(v));

        Button today = view.findViewById(R.id.btnToday);
        Button tomorrow = view.findViewById(R.id.btntomorrow);

        today.setOnClickListener(v -> {
            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));
            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_available));
        });

        tomorrow.setOnClickListener(v -> {
            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));
            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_available));
        });

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Now Showing");
            } else {
                tab.setText("Coming Soon");
            }
        }).attach();

        return view;
    }

    private void showPopupMenu(View view) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(requireContext(), view);
        popup.getMenu().add("View Last Booking");
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("View Last Booking")) {
                showLastBooking();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showLastBooking() {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("BookingPrefs", android.content.Context.MODE_PRIVATE);
        String movie = prefs.getString("LastMovie", null);

        if (movie == null) {
            new android.app.AlertDialog.Builder(requireContext())
                    .setMessage("No previous booking found.")
                    .setPositiveButton("OK", null)
                    .show();
        }

        else {
            int seats = prefs.getInt("LastSeats", 0);
            int totalPrice = prefs.getInt("LastTotalPrice", 0);

            String message = "Movie: " + movie + "\n" +
                    "Seats: " + seats + "\n" +
                    "Total Price: RS " + totalPrice;

            new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Last Booking")
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
