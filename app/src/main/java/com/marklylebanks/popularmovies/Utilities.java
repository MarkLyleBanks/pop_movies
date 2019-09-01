package com.marklylebanks.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Utilities {

    public static final String KEY_AUTHORITY = "api.themoviedb.org";
    public static final String KEY_ACCESS_KEY = "c78be6308882fe1a55161ed04273afe1"; // enter your key here

    /*
     * returns the largest usable image size based
     * on the screen size of the device being used
     */
    public static String getImageSize(int columnSize) {

        String size = "";
        if (columnSize < 154) {
            size = "w92";
        } else if (columnSize < 185) {
            size = "w154";
        } else if (columnSize < 342) {
            size = "w185";
        } else if (columnSize < 500) {
            size = "w342";
        } else if (columnSize < 780) {
            size = "w500";
        } else {
            size = "w780";
        }
        return size;
    }

    /*
     * returns the width of the device's screen
     *
     * The idea to use displayMetrics came from a StackOverflow answer on how to set the number of
     * columns in a gridLayout based on screen size.
     */
    public static int getDisplayWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) displayMetrics.widthPixels;
    }

    /*
     *method to discover if device is connected to network
     * credit StackOverflow
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * This method accepts a string that determines the type of search
     * and builds the correct URL
     */
    public static URL getUrl(String endPoint) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("https")
                .authority(KEY_AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(endPoint)
                .appendQueryParameter("api_key", KEY_ACCESS_KEY);

        String builtUri = builder.build().toString();

        URL url = null;
        try {
            url = new URL(builtUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This overloaded method accepts a string that determines the type of search,
     * a string for a movie id, and builds the correct URL
     */
    public static URL getUrl(String endPoint, String id) {
        Uri.Builder builder = new Uri.Builder();
        builder
                .scheme("https")
                .authority(KEY_AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendPath(endPoint)
                .appendQueryParameter("api_key", KEY_ACCESS_KEY);

        String builtUri = builder.build().toString();

        URL url = null;
        try {
            url = new URL(builtUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method accepts a URL and retrieves a JASON String
     * code source is Udacity's lesson T02.06
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
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
    }// end of getResponseFromHttpUrl function

}
