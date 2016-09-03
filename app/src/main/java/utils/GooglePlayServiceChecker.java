package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import libs.localization.LocalizationActivity;

import static com.google.android.gms.common.GooglePlayServicesUtil.getErrorDialog;

/**
 * The class is useful to check if the current user have the up-to-date google play service
 * apk installed in his/her device or not.
 */
public class GooglePlayServiceChecker {

    public static void checkGooglePlayServices(final Context context) {
        int connectionResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        analyzeConnectionResult(context, connectionResult);
    }


    private static void analyzeConnectionResult(final Context context, int connectionResult) {
        if (connectionResult == ConnectionResult.API_UNAVAILABLE
                || connectionResult == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
                || connectionResult == ConnectionResult.SERVICE_DISABLED
                || connectionResult == ConnectionResult.SERVICE_MISSING) {
            if (context instanceof LocalizationActivity) {
                Dialog dialog = getErrorDialog(connectionResult, (Activity) context, 2226);
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

}
