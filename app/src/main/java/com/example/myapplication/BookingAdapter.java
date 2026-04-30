package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final ArrayList<Booking> bookings;
    private final DatabaseReference userBookingsRef;
    private final Context context;

    public BookingAdapter(Context context, ArrayList<Booking> bookings, DatabaseReference userBookingsRef) {
        this.context = context;
        this.bookings = bookings;
        this.userBookingsRef = userBookingsRef;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        holder.txtMovieName.setText(booking.getMovieName());
        holder.txtDateTime.setText(booking.getDate() + " | " + booking.getTime());
        int ticketsCount = booking.getSeats() != null ? booking.getSeats().size() : 0;
        holder.txtTickets.setText("Tickets: " + ticketsCount + " | Total: RS " + booking.getTotalPrice());
        holder.imgPoster.setImageResource(resolvePosterByMovieName(booking.getMovieName()));

        holder.btnCancel.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> cancelBookingIfAllowed(holder.getBindingAdapterPosition()))
                .setNegativeButton("No", null)
                .show());
    }

    private void cancelBookingIfAllowed(int adapterPosition) {
        if (adapterPosition == RecyclerView.NO_POSITION || adapterPosition >= bookings.size()) {
            return;
        }

        Booking booking = bookings.get(adapterPosition);
        if (!isFutureBooking(booking)) {
            Toast.makeText(context, "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookingId = booking.getBookingId();
        if (bookingId == null || bookingId.isEmpty()) {
            return;
        }

        userBookingsRef.child(bookingId).removeValue().addOnSuccessListener(unused ->
                Toast.makeText(context, "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show()
        );
    }

    private boolean isFutureBooking(Booking booking) {
        long now = System.currentTimeMillis();
        if (booking.getBookingTimestamp() > 0) {
            return booking.getBookingTimestamp() > now;
        }

        String combined = booking.getDate() + " " + booking.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault());
        try {
            Date date = sdf.parse(combined);
            return date != null && date.getTime() > now;
        } catch (ParseException e) {
            return false;
        }
    }

    private int resolvePosterByMovieName(String movieName) {
        if (movieName == null) {
            return R.drawable.oppenheimer;
        }
        if (movieName.equalsIgnoreCase("Oppenheimer")
                || movieName.equalsIgnoreCase("The Oppenheimer")
                || movieName.equalsIgnoreCase("Oppenheimer 2")) {
            return R.drawable.oppenheimer;
        }
        if (movieName.equalsIgnoreCase("Dr Strange") || movieName.equalsIgnoreCase("Dr Strange 3")) {
            return R.drawable.strange;
        }
        if (movieName.equalsIgnoreCase("3 Idiots") || movieName.equalsIgnoreCase("3 Idiots Returns")) {
            return R.drawable.idiots;
        }
        return R.drawable.oppenheimer;
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtMovieName;
        TextView txtDateTime;
        TextView txtTickets;
        ImageButton btnCancel;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgBookingPoster);
            txtMovieName = itemView.findViewById(R.id.txtBookingMovieName);
            txtDateTime = itemView.findViewById(R.id.txtBookingDateTime);
            txtTickets = itemView.findViewById(R.id.txtBookingTickets);
            btnCancel = itemView.findViewById(R.id.btnCancelBooking);
        }
    }
}
