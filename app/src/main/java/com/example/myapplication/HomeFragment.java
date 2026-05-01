package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        android.widget.ImageView btnMenu = view.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                ((MainActivity) requireActivity()).openDrawer();
            }
        });

        Button today = view.findViewById(R.id.btnToday);
        Button tomorrow = view.findViewById(R.id.btntomorrow);

        today.setOnClickListener(v -> {
            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));
            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_available));
        });

        tomorrow.setOnClickListener(v -> {
            tomorrow.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_booked));
            today.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.seat_available));
        });

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        return view;
    }
}
