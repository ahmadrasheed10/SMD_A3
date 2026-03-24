package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieList;
    private Context context;
    private boolean isComingSoon;

    public MovieAdapter(Context context, ArrayList<Movie> movieList, boolean isComingSoon) {
        this.context = context;
        this.movieList = movieList;
        this.isComingSoon = isComingSoon;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.imgPoster.setImageResource(movie.getPosterResId());
        holder.txtMovieName.setText(movie.getName());
        holder.txtGenreDuration.setText(movie.getGenre() + " / " + movie.getDuration());

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerUrl()));
            context.startActivity(intent);
        });

        holder.btnBookSeats.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).navigateToSeatSelection(movie.getName(), isComingSoon, movie.getTrailerUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtMovieName, txtGenreDuration;
        Button btnBookSeats, btnTrailer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
            txtGenreDuration = itemView.findViewById(R.id.txtGenreDuration);
            btnBookSeats = itemView.findViewById(R.id.btnBookSeats);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
        }
    }
}
