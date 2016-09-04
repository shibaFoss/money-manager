package gui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import accounts.AccountManager;
import gui.BaseActivity;
import gui.account_create.NewAccountCreateActivity;
import gui.home.HomeActivity;
import in.softc.aladindm.R;

public class LauncherActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_launcher;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        //the account manager is not set yet. so initialize it first.
        if (getApp().getAccountManager() == null) {
            AccountManager am = AccountManager.readData(getApp());
            getApp().setAccountManager(am);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        }, 1500);
    }


    @Override
    public void onClosed() {
        finish();
    }


    private void startNextActivity() {
        AccountManager am = getApp().getAccountManager();
        if (am != null) {
            if (am.totalAccounts.size() == 0) {
                Intent intent = new Intent(LauncherActivity.this, NewAccountCreateActivity.class);
                intent.putExtra("isLauncherFired", true);
                startActivity(intent);

            } else {
                startActivity(HomeActivity.class);
            }
        }

        finish();
    }

}
