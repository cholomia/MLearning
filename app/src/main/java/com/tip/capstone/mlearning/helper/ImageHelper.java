package com.tip.capstone.mlearning.helper;

import android.content.Context;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class ImageHelper {

    public static int getResourceId(Context context, String pVariableName) {
        try {
            return context.getResources().getIdentifier(pVariableName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
