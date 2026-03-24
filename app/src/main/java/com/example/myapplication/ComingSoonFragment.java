package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Movie> comingSoonMovies = new ArrayList<>();
        comingSoonMovies.add(new Movie("Oppenheimer 2", "Drama", "160 min",
                R.drawable.oppenheimer, "https://www.youtube.com/watch?v=uYPbbksJxIg"));
        comingSoonMovies.add(new Movie("3 Idiots Returns", "Comedy", "145 min",
                R.drawable.idiots, "https://www.youtube.com/watch?v=K0eDlFX9GMc"));
        comingSoonMovies.add(new Movie("Dr Strange 3", "Action", "140 min",
                R.drawable.strange, "https://www.youtube.com/watch?v=aWzlQ2N6qqg"));

        MovieAdapter adapter = new MovieAdapter(getActivity(), comingSoonMovies);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
