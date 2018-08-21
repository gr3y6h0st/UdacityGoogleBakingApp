package com.nanodegree.android.bakingapp.BakingData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BakingDataContentProvider extends ContentProvider {

    private BakingDataDbHelper mDbHelper;

    public static final int RECIPES_CODE = 319;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(BakingDataContract.CONTENT_AUTHORITY, BakingDataContract.PATH_RECIPES, RECIPES_CODE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new BakingDataDbHelper(context);

        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor = null;

        switch(match){
            case RECIPES_CODE:

                cursor = db.query(BakingDataContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri ingredientsDataUri = null;

        switch (match){

            case RECIPES_CODE:

                long id = db.insert(BakingDataContract.TABLE_NAME, null, values);

                if(id > 0){
                    ingredientsDataUri = ContentUris.withAppendedId(BakingDataContract.CONTENT_URI, id);
                }

                break;

            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ingredientsDataUri;
    }

    //Once AsyncTask Loader finishes, call delete to
    // erase the data in the table prior to writing the selected recipe.
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {

            case RECIPES_CODE:
                final SQLiteDatabase db = mDbHelper.getWritableDatabase();

                String id = uri.getLastPathSegment();
                numRowsDeleted = db.delete(
                        BakingDataContract.TABLE_NAME,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
