package core;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import accounts.AccountManager;
import libs.Remember;

public class App extends Application {

    private AccountManager accountManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(this, "App");
    }


    public static boolean isPermissionGranted(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }


    public AccountManager getAccountManager() {
        return accountManager;
    }
}
