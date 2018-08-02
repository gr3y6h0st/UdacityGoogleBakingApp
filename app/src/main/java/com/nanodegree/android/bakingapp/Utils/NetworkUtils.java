package com.nanodegree.android.bakingapp.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BAKING_DATA_PATH = "/topher/2017/May/59121517_baking/baking.json";

    private static final String STATIC_BAKING_APP_DATABASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final String BAKING_APP_DATA_BASE_URL = STATIC_BAKING_APP_DATABASE_URL;

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BAKING_APP_DATA_BASE_URL);

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI: " + url);

        return url;
    }

    /** Method returns entire result from HTTP response.
     *
     * @param url URL to fetch the HTTP response from.
     * @return THe contents of HTTP response.
     * @throws IOException Related to network and stream reading
     * Handles Character encoding. Allocates/deallocates buffers as needed.
     */
    public static String getResponseFromHttpUrl (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();

        try{
            InputStream input = urlConnection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
