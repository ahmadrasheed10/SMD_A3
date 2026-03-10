package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    int pop=0, nachos=0, drink=0, candy=0;
    final int POP_PRICE = 500;
    final int NACHOS_PRICE = 250;
    final int DRINK_PRICE = 150;
    final int CANDY_PRICE = 100;

    TextView txtPop, txtNachos, txtDrink, txtCandy;
    int seats, ticketTotal;
    String movie;
    ArrayList<String> seatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        movie = getIntent().getStringExtra("movie");
        seats = getIntent().getIntExtra("seats",0);
        ticketTotal = getIntent().getIntExtra("ticketTotal",0);
        seatsList = getIntent().getStringArrayListExtra("seatsList");

        txtPop = findViewById(R.id.txtQtyPop);
        txtDrink = findViewById(R.id.txtQtyDrink);
        txtCandy = findViewById(R.id.txtQtyCandy);
        txtNachos = findViewById(R.id.txtQtyNachos);

        setupButtons();
    }

    private void setupButtons() {

        findViewById(R.id.btnPlusPop).setOnClickListener(v -> { pop++; txtPop.setText(""+pop); });
        findViewById(R.id.btnMinusPop).setOnClickListener(v -> { if(pop>0) pop--; txtPop.setText(""+pop); });

        findViewById(R.id.btnPlusNachos).setOnClickListener(v -> { nachos++; txtNachos.setText(""+nachos); });
        findViewById(R.id.btnMinusNachos).setOnClickListener(v -> { if(nachos>0) nachos--; txtNachos.setText(""+nachos); });


        findViewById(R.id.btnPlusDrink).setOnClickListener(v -> { drink++; txtDrink.setText(""+drink); });
        findViewById(R.id.btnMinusDrink).setOnClickListener(v -> { if(drink>0) drink--; txtDrink.setText(""+drink); });

        findViewById(R.id.btnPlusCandy).setOnClickListener(v -> { candy++; txtCandy.setText(""+candy); });
        findViewById(R.id.btnMinusCandy).setOnClickListener(v -> { if(candy>0) candy--; txtCandy.setText(""+candy); });

        findViewById(R.id.btnConfirm).setOnClickListener(v -> openSummary());

    }

    private void openSummary() {

        int snacksTotal =
                pop*POP_PRICE +
                        nachos*NACHOS_PRICE +
                        drink*DRINK_PRICE +
                        candy*CANDY_PRICE;

        Intent intent = new Intent(this, TicketSummaryActivity.class);
        intent.putExtra("movie", movie);
        intent.putExtra("seats", seats);
        intent.putExtra("ticketTotal", ticketTotal);
        intent.putExtra("snacksTotal", snacksTotal);
        intent.putStringArrayListExtra("seatsList", seatsList);
        startActivity(intent);
    }
}
