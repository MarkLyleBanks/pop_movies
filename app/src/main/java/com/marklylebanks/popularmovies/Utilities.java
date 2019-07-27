package com.marklylebanks.popularmovies;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utilities {

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
        Log.i("test", "size is: " + size);
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
        Log.i("test", "display width is: " + (int) displayMetrics.widthPixels);
        return (int) displayMetrics.widthPixels;
    }


}
