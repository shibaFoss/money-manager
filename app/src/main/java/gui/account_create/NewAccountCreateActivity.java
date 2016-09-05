package gui.account_create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import gui.home.HomeActivity;
import in.softc.aladindm.R;

public class NewAccountCreateActivity extends BaseActivity {

    private EditText accountName, accountBalance, accountNote;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_new_account;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        accountName = (EditText) findViewById(R.id.edit_account_name);
        accountBalance = (EditText) findViewById(R.id.edit_starting_balance);
        accountNote = (EditText) findViewById(R.id.edit_account_note);
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }


    private String getCurrencySymbolByTimeZone() {
        return "₹";
    }


    public void onBack(View view) {
        exitActivityOnDoublePress();
    }


    public void onSaveAccount(View view) {
        Account account = new Account();

        String enteredName = accountName.getText().toString();
        if (enteredName.length() > 1) account.name = enteredName;
        else {
            toast(getString(R.string.give_account_name));
            return;
        }

        account.note = accountNote.getText().toString();
        account.currency = getCurrencySymbolByTimeZone();
        account.monthlyBudget = 0.0;

        Transaction transaction = new Transaction();
        transaction.account = account;
        transaction.isExpense = false;

        String enteredAmount = accountBalance.getText().toString();
        if (enteredAmount.length() > 1) transaction.transactionAmount = Double.valueOf(enteredAmount);
        else transaction.transactionAmount = 0;

        transaction.transactionNote = "Initial balance.";
        transaction.transactionCategory = "Initial Balance";

        transaction.memoImagePath = null;
        transaction.colorCode = "#66bb6a";

        transaction.updateTransactionTime();

        account.addNewTransaction(transaction);

        AccountManager accountManager = getApp().getAccountManager();
        accountManager.totalAccounts.add(account);
        accountManager.write(getApp());

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isLauncherFired", false))
            startActivity(HomeActivity.class);

        finish();
    }
}
