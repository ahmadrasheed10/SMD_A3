package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SnackAdapter extends ArrayAdapter<Snack> {

    public SnackAdapter(Context context, ArrayList<Snack> snacks) {
        super(context, 0, snacks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Snack snack = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_snack, parent, false);
        }

        ImageView imgSnack = convertView.findViewById(R.id.imgSnack);
        TextView txtName = convertView.findViewById(R.id.txtSnackName);
        TextView txtPrice = convertView.findViewById(R.id.txtSnackPrice);
        TextView txtQty = convertView.findViewById(R.id.txtQty);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);
        Button btnMinus = convertView.findViewById(R.id.btnMinus);

        imgSnack.setImageResource(snack.getImageResId());
        txtName.setText(snack.getName());
        txtPrice.setText(snack.getPrice() + " RS");
        txtQty.setText(String.valueOf(snack.getQuantity()));

        btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            notifyDataSetChanged();
        });

        btnMinus.setOnClickListener(v -> {
            if (snack.getQuantity() > 0) {
                snack.setQuantity(snack.getQuantity() - 1);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
