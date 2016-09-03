package utils;

import android.os.Build;

public class OsUtility {

    public static int getBuildSdk() {
        return Build.VERSION.SDK_INT;
    }

}
