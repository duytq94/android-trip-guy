package com.dfa.vinatrip;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNetwork {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
