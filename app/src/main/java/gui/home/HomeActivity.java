package gui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.TextView;

import gui.BaseActivity;
import gui.home.overview.OverviewFragment;
import in.softc.aladindm.R;

public class HomeActivity extends BaseActivity {

    private OverviewFragment overviewFragment;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragment_container, getOverviewFragment()).commit();
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }


    public OverviewFragment getOverviewFragment() {
        return (overviewFragment == null) ? new OverviewFragment() : overviewFragment;
    }


    public void changeToolbarTitle(String titleName) {
        TextView txtToolbar = (TextView) findViewById(R.id.txt_toolbar);
        if (txtToolbar != null)
            txtToolbar.setText(titleName);
    }
}
