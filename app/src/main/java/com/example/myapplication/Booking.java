package com.example.myapplication;

import java.util.ArrayList;

public class Booking {
    private String bookingId;
    private String userId;
    private String movieName;
    private ArrayList<String> seats;
    private int totalPrice;
    private String date;
    private String time;
    private long bookingTimestamp;

    public Booking() {
        // Required empty constructor for Firebase.
    }

    public Booking(String bookingId, String userId, String movieName, ArrayList<String> seats,
                   int totalPrice, String date, String time, long bookingTimestamp) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.bookingTimestamp = bookingTimestamp;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMovieName() {
        return movieName;
    }

    public ArrayList<String> getSeats() {
        return seats;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public long getBookingTimestamp() {
        return bookingTimestamp;
    }
}
