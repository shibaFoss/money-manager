package gui.account_create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import accounts.Account;
import accounts.AccountManager;
import gui.BaseActivity;
import gui.home.HomeActivity;
import in.softc.aladindm.R;

public class NewAccountCreateActivity extends BaseActivity {

    private Account account;
    private EditText accountName, accountBalance, accountNote;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_new_account;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        generateEmptyAccount();
        setupViews();
    }


    private void setupViews() {
        accountName = (EditText) findViewById(R.id.edit_account_name);
        accountBalance = (EditText) findViewById(R.id.edit_starting_balance);
        accountNote = (EditText) findViewById(R.id.edit_account_note);
    }


    private void generateEmptyAccount() {
        account = new Account();
        account.availableBalance = 0.0;
        account.currency = getCurrencyByTimeZone();
        account.note = "";
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }


    private String getCurrencyByTimeZone() {
        return "";
    }


    public void onBack(View view) {
        exitActivityOnDoublePress();
    }


    public void onSaveAccount(View view) {
        String balance = accountBalance.getText().toString();
        if (balance.length() > 1)
            account.availableBalance = Double.valueOf(balance);

        account.accountName = accountName.getText().toString();
        account.note = accountNote.getText().toString();

        if (account.accountName.length() < 1) {
            toast(getString(R.string.give_account_name));
            return;
        }

        AccountManager accountManager = getApp().getAccountManager();
        accountManager.totalAccounts.add(account);
        accountManager.write(getApp());

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isLauncherFired", false))
            startActivity(HomeActivity.class);

        finish();
    }
}
