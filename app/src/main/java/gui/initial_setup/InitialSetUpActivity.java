package gui.initial_setup;

import android.os.Bundle;

import gui.BaseActivity;
import in.softc.aladindm.R;

public class InitialSetUpActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_initial_setup;
    }

    @Override
    public void onInitialize(Bundle bundle) {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onResumed() {

    }

    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }

    @Override
    public void onDestroyed() {

    }

}
