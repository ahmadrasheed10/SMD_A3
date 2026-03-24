package com.example.myapplication;

public class Movie {
    private String name;
    private String genre;
    private String duration;
    private int posterResId;
    private String trailerUrl;

    public Movie(String name, String genre, String duration, int posterResId, String trailerUrl) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.posterResId = posterResId;
        this.trailerUrl = trailerUrl;
    }

    public String getName() { return name; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getPosterResId() { return posterResId; }
    public String getTrailerUrl() { return trailerUrl; }
}
