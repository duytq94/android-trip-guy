package com.dfa.vinatrip.utils;

import android.content.Context;

/**
 * Created by duonghd on 9/27/2017.
 */

public class AppUtil {
    private static Context context;
    
    public static void init(Context context) {
        AppUtil.context = context;
    }
    
    public Context getContext() {
        return context;
    }
}
