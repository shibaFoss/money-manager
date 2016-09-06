package gui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import gui.home.overview.OverviewFragment;
import gui.transaction.TransactionActivity;
import in.softc.aladindm.R;
import utils.DialogUtility;

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
    public void onResume() {
        super.onResume();
        if (overviewFragment != null)
            overviewFragment.updateTransactions();
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


    public void onAddExpense(View view) {
        final AccountManager am = getApp().getAccountManager();
        String accountNameArray[] = new String[am.totalAccounts.size()];
        for (int i = 0; i < accountNameArray.length; i++)
            accountNameArray[i] = am.totalAccounts.get(i).name;

        DialogUtility.getDefaultBuilder(this)
                .title(R.string.select_account)
                .items(accountNameArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = new Intent(HomeActivity.this, TransactionActivity.class);
                        intent.putExtra(TransactionActivity.ACCOUNT_ARRAY_POSITION, which);
                        intent.putExtra(TransactionActivity.IS_EXPENSE, true);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .show();

    }

}
