package gui.home.overview;

import android.os.Bundle;
import android.view.View;

import gui.BaseFragment;
import gui.home.HomeActivity;
import in.softc.aladindm.R;

public class OverviewFragment extends BaseFragment {
    private HomeActivity activity;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_overflow;
    }


    @Override
    protected void onAfterLayoutLoad(View layoutView, Bundle savedInstanceState) {
        activity = (HomeActivity) getBaseActivity();
        activity.changeToolbarTitle("Overview");
    }
}
