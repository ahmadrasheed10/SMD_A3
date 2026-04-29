package com.example.myapplication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MovieRepository {

    public static ArrayList<Movie> loadMovies(Context context, boolean comingSoon) {
        ArrayList<Movie> movies = new ArrayList<>();
        if (context == null) {
            return movies;
        }

        try {
            InputStream inputStream = context.getAssets().open("movies.json");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            inputStream.close();

            JSONObject rootObject = new JSONObject(builder.toString());
            JSONArray movieArray = rootObject.getJSONArray("movies");

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject object = movieArray.getJSONObject(i);
                boolean isComingSoon = object.optBoolean("comingSoon", false);
                if (isComingSoon != comingSoon) {
                    continue;
                }

                String name = object.optString("name");
                String genre = object.optString("genre");
                String duration = object.optString("duration");
                String posterName = object.optString("poster");
                String trailerUrl = object.optString("trailerUrl");

                int posterResId = context.getResources()
                        .getIdentifier(posterName, "drawable", context.getPackageName());

                if (posterResId == 0) {
                    posterResId = R.drawable.oppenheimer;
                }

                movies.add(new Movie(name, genre, duration, posterResId, trailerUrl));
            }
        } catch (Exception ignored) {
            // Return whatever was parsed successfully; empty list if parsing failed.
        }

        return movies;
    }
}
