package com.ahnaser.myfirstapp;

/**
 * Created by root on 23/05/15.
 */
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class L {
    public static void m(String message) {
        Log.d("AHMAD", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }
    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}