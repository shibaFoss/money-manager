package core;

import android.app.Application;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import accounts.WalletManager;
import libs.Remember;

public class App extends Application {

    public static final String[] REQUIRED_PERMISSIONS = new String[]{};

    public static boolean isPermissionGranted(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(this, "App");
        setUpWalletManager();

    }

    private void setUpWalletManager() {

    }

    private WalletManager walletManager;

    public WalletManager getWalletManager() {
        return walletManager;
    }

}
