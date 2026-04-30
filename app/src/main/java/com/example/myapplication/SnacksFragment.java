package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private ArrayList<Snack> snackList;
    private SnackAdapter adapter;

    private int seats;
    private int ticketTotal;
    private String movie;
    private ArrayList<String> seatsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        if (getArguments() != null) {
            movie = getArguments().getString("movie");
            seats = getArguments().getInt("seats", 0);
            ticketTotal = getArguments().getInt("ticketTotal", 0);
            seatsList = getArguments().getStringArrayList("seatsList");
        }

        ListView listViewSnacks = view.findViewById(R.id.listViewSnacks);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        SnackDatabaseHelper databaseHelper = new SnackDatabaseHelper(requireContext());
        snackList = databaseHelper.getAllSnacks();

        adapter = new SnackAdapter(getContext(), snackList);
        listViewSnacks.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> openSummary());

        return view;
    }

    private void openSummary() {
        int snacksTotal = 0;
        for (Snack snack : snackList) {
            snacksTotal += snack.getPrice() * snack.getQuantity();
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToTicketSummary(
                    movie, seatsList, ticketTotal, snacksTotal);
        }
    }
}
