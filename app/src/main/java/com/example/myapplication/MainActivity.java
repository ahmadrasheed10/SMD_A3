package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Welcome to CineFAST", Toast.LENGTH_SHORT).show();
        Log.d("CineFAST", "MainActivity launched");
        sessionManager = new SessionManager(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        View headerView = navigationView.getHeaderView(0);
        TextView emailTv = headerView.findViewById(R.id.tvDrawerEmail);
        emailTv.setText(sessionManager.getUserEmail());

        setupDrawerNavigation();

        if (savedInstanceState == null) {
            showHomeFragment();
        }
    }

    private void setupDrawerNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showHomeFragment();
            } else if (id == R.id.nav_my_bookings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new MyBookingsFragment())
                        .commit();
            } else if (id == R.id.nav_logout) {
                sessionManager.clearSession();
                FirebaseAuth.getInstance().signOut();
                startActivity(new android.content.Intent(this, LoginActivity.class));
                finishAffinity();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void showHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
            return;
        }

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (!(currentFragment instanceof HomeFragment)) {
            showHomeFragment();
            return;
        }

        super.onBackPressed();
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.START);
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
        showHomeFragment();
    }
}
