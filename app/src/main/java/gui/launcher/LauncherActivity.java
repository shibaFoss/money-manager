package gui.launcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import accounts.AccountManager;
import gui.BaseActivity;
import gui.home.HomeActivity;
import in.mobi_space.money_manager.R;
import utils.FileUtils;
import utils.Font;
import utils.WritableObject;

import static gui.launcher.InitialDialog.FILE_SELECT_CODE;

public class LauncherActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_launcher;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        Font.setFont(Font.RobotoRegular, this, R.id.txt_company_name);

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        String path = FileUtils.getPath(this, uri);
                        if (path != null) {
                            File database = new File(path);
                            if (database.exists()) {
                                WritableObject wo = AccountManager.readObjectFromSdcard(database);
                                if (wo != null) {
                                    AccountManager am = (AccountManager) wo;
                                    getApp().setAccountManager(am);
                                    am.write(getApp());
                                    showSimpleHtmlMessageBox(getString(R.string.restart_app_for_effect),
                                            new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    finish();
                                                }
                                            });
                                } else {
                                    vibrate(20);
                                    toast(R.string.item_is_not_valid_database_file);
                                    startNextActivity();
                                }
                            }
                        }
                    } catch (Throwable error) {
                        error.printStackTrace();
                        toast(R.string.item_is_not_valid_database_file);
                        startNextActivity();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startNextActivity() {
        AccountManager am = getApp().getAccountManager();
        if (am != null) {
            if (am.accounts.size() == 0) {
                new InitialDialog(this);
            } else {
                startActivity(HomeActivity.class);
                finish();
            }
        }
    }

}
