package com.example.mido.moviesapp;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Ahmed
 */

public class Utility {

    public static int calculateNoOfColumns(Context context,int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / width);
    }

    public static int calculateScreenWidthDividedBy2(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.widthPixels / 2);
    }
}
