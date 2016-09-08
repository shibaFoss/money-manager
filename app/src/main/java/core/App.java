package core;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import accounts.AccountManager;
import libs.Remember;
import utils.Font;

public class App extends Application {

    public static final String appDirectory = Environment.getExternalStorageDirectory() + "/.Money Manager";
    public static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private AccountManager accountManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(this, "App");
        Font.init(this);
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
