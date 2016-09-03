package gui.launcher;

import android.os.Bundle;
import android.os.Handler;

import gui.BaseActivity;
import gui.initial_setup.InitialSetUpActivity;
import in.softc.aladindm.R;

public class LauncherActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_launcher;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(InitialSetUpActivity.class);
            }
        }, 1200);
    }


    @Override
    public void onResumed() {

    }


    @Override
    public void onPaused() {

    }


    @Override
    public void onClosed() {

    }


    @Override
    public void onDestroyed() {

    }
}
