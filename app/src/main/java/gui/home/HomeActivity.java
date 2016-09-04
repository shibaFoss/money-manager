package gui.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import gui.BaseActivity;
import in.softc.aladindm.R;

public class HomeActivity extends BaseActivity {
    private ViewPager viewPager;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }

}
