package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome to CineFAST", Toast.LENGTH_SHORT).show();
        Log.d("CineFAST", "MainActivity launched");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }
    }

    public void navigateToSeatSelection(String movieName, boolean isComingSoon, String trailerUrl) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("movie", movieName);
        bundle.putBoolean("isComingSoon", isComingSoon);
        bundle.putString("trailerUrl", trailerUrl);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToSnacks(String movie, ArrayList<String> seatsList, int ticketTotal, int seats) {
        SnacksFragment fragment = new SnacksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("movie", movie);
        bundle.putStringArrayList("seatsList", seatsList);
        bundle.putInt("ticketTotal", ticketTotal);
        bundle.putInt("seats", seats);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToTicketSummary(String movie, ArrayList<String> seatsList, int ticketTotal, int snacksTotal) {
        TicketSummaryFragment fragment = new TicketSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("movie", movie);
        bundle.putStringArrayList("seatsList", seatsList);
        bundle.putInt("ticketTotal", ticketTotal);
        bundle.putInt("snacksTotal", snacksTotal);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToHome() {
        getSupportFragmentManager().popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
