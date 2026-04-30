package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MyBookingsFragment extends Fragment {

    private final ArrayList<Booking> allBookings = new ArrayList<>();
    private final ArrayList<Booking> filteredBookings = new ArrayList<>();
    private BookingAdapter adapter;
    private TextView emptyStateTv;
    private DatabaseReference userBookingsRef;
    private ValueEventListener bookingsListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        RecyclerView bookingsRv = view.findViewById(R.id.rvBookings);
        EditText searchEt = view.findViewById(R.id.etSearchBookings);
        emptyStateTv = view.findViewById(R.id.tvNoBookings);

        view.findViewById(R.id.btnBackHome).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToHome();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(requireContext(), "Please login again", Toast.LENGTH_SHORT).show();
            emptyStateTv.setVisibility(View.VISIBLE);
            return view;
        }

        userBookingsRef = FirebaseDatabase.getInstance()
                .getReference("bookings")
                .child(user.getUid());

        adapter = new BookingAdapter(requireContext(), filteredBookings, userBookingsRef);
        bookingsRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        bookingsRv.setAdapter(adapter);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        attachBookingsListener();
        return view;
    }

    private void attachBookingsListener() {
        bookingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allBookings.clear();
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    Booking booking = bookingSnapshot.getValue(Booking.class);
                    if (booking != null) {
                        allBookings.add(new Booking(
                                bookingSnapshot.getKey(),
                                booking.getUserId(),
                                booking.getMovieName(),
                                booking.getSeats(),
                                booking.getTotalPrice(),
                                booking.getDate(),
                                booking.getTime(),
                                booking.getBookingTimestamp()
                        ));
                    }
                }
                applyFilter("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        userBookingsRef.addValueEventListener(bookingsListener);
    }

    private void applyFilter(String query) {
        String lowerQuery = query.toLowerCase(Locale.getDefault()).trim();
        filteredBookings.clear();
        for (Booking booking : allBookings) {
            String movieName = booking.getMovieName() == null ? "" : booking.getMovieName();
            String date = booking.getDate() == null ? "" : booking.getDate();
            if (lowerQuery.isEmpty()
                    || movieName.toLowerCase(Locale.getDefault()).contains(lowerQuery)
                    || date.toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
                filteredBookings.add(booking);
            }
        }
        adapter.notifyDataSetChanged();
        emptyStateTv.setVisibility(filteredBookings.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userBookingsRef != null && bookingsListener != null) {
            userBookingsRef.removeEventListener(bookingsListener);
        }
    }
}
