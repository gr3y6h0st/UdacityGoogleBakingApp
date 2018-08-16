package com.nanodegree.android.bakingapp.BakingData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BakingDataDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "recipe_ingredients.db";

    public BakingDataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                BakingDataContract.TABLE_NAME + "(" +
                BakingDataContract.COLUMN_INGREDIENT_QUANTITY + " TEXT NOT NULL, " +
                BakingDataContract.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                BakingDataContract.COLUMN_INGREDIENT_DESCRIPTION + " TEXT NOT NULL" +
                ")";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BakingDataContract.TABLE_NAME);
        onCreate(db);

    }
}
