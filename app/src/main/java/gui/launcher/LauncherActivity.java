package gui.launcher;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

import accounts.AccountManager;
import core.App;
import gui.BaseActivity;
import gui.account_create.NewAccountCreateActivity;
import gui.static_dialogs.YesNoDialog;
import in.softc.aladindm.R;
import libs.AsyncJob;
import utils.FileUtils;

public class LauncherActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_launcher;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        if (App.isPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (getApp().getAccountManager() == null) {
                AsyncJob.doInBackground(new AsyncJob.BackgroundJob() {
                    @Override
                    public void doInBackground() {
                        AccountManager am = AccountManager.readData(getApp());
                        getApp().setAccountManager(am);
                        startNextActivity();
                    }
                });
            } else {
                startNextActivity();
            }
        } else {
            askForSdcardWritingPermission();
        }
    }


    @Override
    public void onClosed() {
        finish();
    }


    private void startNextActivity() {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                AccountManager accountManager = getApp().getAccountManager();
                if (accountManager.totalAccounts.size() == 0) {
                    startActivity(NewAccountCreateActivity.class);

                } else {
                    toast("Test complete! Account is created. " + accountManager.totalAccounts.get(0).accountName);
                }
            }
        });
    }


    private void askForSdcardWritingPermission() {
        YesNoDialog yesNoDialog = new YesNoDialog(this) {

            @Override
            public void onYes(Dialog dialog) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(LauncherActivity.this,
                        App.REQUIRED_PERMISSIONS,
                        USES_PERMISSIONS_REQUEST_CODE);
            }


            @Override
            public void onNo(Dialog dialog) {
                dialog.dismiss();
                finish();
            }
        };

        yesNoDialog.show(
                R.string.grant_permission,
                R.string.cancel,
                R.string.app_require_write_permissions);
    }

}
