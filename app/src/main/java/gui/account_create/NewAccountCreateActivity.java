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
import utils.Font;

public class NewAccountCreateActivity extends BaseActivity {

    private EditText accountName, accountBalance;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_new_account;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        initFonts();
        accountName = (EditText) findViewById(R.id.edit_account_name);
        accountBalance = (EditText) findViewById(R.id.edit_starting_balance);
    }

    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }

    public void onBack(View view) {
        exitActivityOnDoublePress();
    }

    public void onSaveAccount(View view) {
        Account account = new Account();
        account.uniqueId = getApp().getAccountManager().getUniqueAccountId();

        String enteredName = accountName.getText().toString();
        if (enteredName.length() > 1) account.accountName = enteredName;
        else {
            toast(getString(R.string.give_account_name));
            return;
        }
        account.currencySymbol = getCurrencySymbolByTimeZone();
        account.monthlyBudget = 0;

        //---------------------------------------------------------------//
        Transaction transaction = new Transaction();
        transaction.uniqueId = account.getNewTransactionUniqueId();
        transaction.accountName = account.accountName;
        transaction.associateAccountId = account.uniqueId;
        transaction.isExpense = false;

        String enteredAmount = accountBalance.getText().toString();
        if (enteredAmount.length() > 1) {
            double result = Double.valueOf(enteredAmount);
            result = Math.floor(result * 100) / 100;
            transaction.transactionAmount = result;
        } else {
            transaction.transactionAmount = 0;
        }

        transaction.transactionNote = getString(R.string.initial_balance);
        transaction.transactionCategory = getString(R.string.starting_balance);
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

    private void initFonts() {
        int[] ids = new int[]{R.id.txt_account_name, R.id.edit_account_name, R.id.txt_currency, R.id
                .currency_selector_spinner, R.id.txt_current_balance, R.id.edit_starting_balance};
        Font.setFont(Font.LatoRegular, this, ids);
        Font.setFont(Font.LatoRegular, this, R.id.txt_toolbar);
    }

    private String getCurrencySymbolByTimeZone() {
        return "₹";
    }
}
