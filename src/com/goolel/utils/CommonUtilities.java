package com.goolel.utils;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    public static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        
    }
}
