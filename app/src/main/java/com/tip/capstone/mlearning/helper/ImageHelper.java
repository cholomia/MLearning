package com.tip.capstone.mlearning.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * @author pocholomia
 * @since 18/11/2016
 */

public class ImageHelper {

    private static final String TAG = ImageHelper.class.getSimpleName();

    public static int getResourceId(Context context, String pVariableName) {
        try {
            Resources resources = context.getResources();
            String[] imageName = pVariableName.split("\\.");
            Log.d(TAG, "getResourceId: imagename " + imageName[0]);
            final int resourceId = resources.getIdentifier(imageName[0], "drawable", context.getApplicationContext().getPackageName());
            //return ContextCompat.getDrawable(context, resourceId);
            return context.getResources().getIdentifier(imageName[0], "drawable", context.getPackageName());
        } catch (Exception e) {
            Log.e(TAG, "getResourceId: " + pVariableName, e);
            return -1;
        }
    }

}
