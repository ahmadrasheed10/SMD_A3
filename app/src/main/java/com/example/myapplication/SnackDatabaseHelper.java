package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast_snacks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SNACKS = "snacks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image";

    public SnackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_SNACKS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PRICE + " INTEGER NOT NULL, "
                + COL_IMAGE + " INTEGER NOT NULL"
                + ")";
        db.execSQL(createTableQuery);
        insertInitialSnacks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialSnacks(SQLiteDatabase db) {
        insertSnack(db, "Popcorn", 500, R.drawable.popcorn);
        insertSnack(db, "Cold Drink", 150, R.drawable.drink);
        insertSnack(db, "Candy", 100, R.drawable.candy);
        insertSnack(db, "Nachos", 250, R.drawable.nachos);
    }

    private void insertSnack(SQLiteDatabase db, String name, int price, int imageResId) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, imageResId);
        db.insert(TABLE_SNACKS, null, values);
    }

    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor c = db.query(
                TABLE_SNACKS,
                new String[]{COL_NAME, COL_PRICE, COL_IMAGE},
                null,
                null,
                null,
                null,
                COL_ID + " ASC"
        )) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndexOrThrow(COL_NAME));
                int price = c.getInt(c.getColumnIndexOrThrow(COL_PRICE));
                int imageResId = c.getInt(c.getColumnIndexOrThrow(COL_IMAGE));
                snacks.add(new Snack(name, price, imageResId));
            }
        }

        db.close();
        return snacks;
    }
}
