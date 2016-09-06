package gui.transaction;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import in.softc.aladindm.R;

public class TransactionActivity extends BaseActivity implements OnDateSetListener {
    public static final String ACCOUNT_ARRAY_POSITION = "ACCOUNT_ARRAY_POSITION";
    public static final String IS_EXPENSE = "IS_EXPENSE";

    private EditText note;
    private TextView date, transactionAmount;
    private Transaction transaction;
    private ArrayList<String> transactionCategories;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_transaction;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        note = (EditText) findViewById(R.id.edit_transaction_note);
        date = (TextView) findViewById(R.id.txt_transaction_date);
        transactionAmount = (TextView) findViewById(R.id.txt_transaction_amount);
        RadioGroup categoryRadioGroup = (RadioGroup) findViewById(R.id.radio_group_category);

        //---------------------------------------------------------------------------//
        AccountManager accountManager = getApp().getAccountManager();
        Intent intent = getIntent();
        int accountPosition = intent.getIntExtra(ACCOUNT_ARRAY_POSITION, -1);
        boolean isExpense = intent.getBooleanExtra(IS_EXPENSE, true);

        if (accountPosition == -1) {
            vibrate(10);
            toast(getString(R.string.something_went_wrong));
            finish();
            return;
        }

        Account account = accountManager.totalAccounts.get(accountPosition);
        transaction = new Transaction();
        transaction.account = account;
        transaction.isExpense = isExpense;
        transaction.transactionAmount = 500;
        transaction.transactionCategory = "";

        transaction.updateTransactionTime();

        if (transaction.isExpense) transactionCategories = accountManager.expenseCategories;
        else transactionCategories = accountManager.incomeCategories;

        //---------------------------------------------------------------------------//
        date.setText(transaction.date);

        changeMoneyColor();
        setTransactionAmount(transaction.transactionAmount);
        createCategoryRadioButtons(categoryRadioGroup, transactionCategories);
    }


    private void setTransactionAmount(double money) {
        transactionAmount.setText(String.valueOf(transaction.account.currency + " " + money));
    }


    @Override
    public void onClosed() {
        finish();
    }


    public void onBack(View view) {
        finish();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        transaction.date = day + "/" + (month + 1) + "/" + year;
        transaction.day = day;
        transaction.month = month;
        transaction.year = year;
        date.setText(transaction.date);
    }


    public void onSaveTransaction(View view) {
        if (transaction.transactionAmount == 0) {
            vibrate(10);
            toast(getString(R.string.give_transaction_amount));
            return;
        }

        transaction.transactionNote = note.getText().toString();
        if (transaction.transactionCategory.length() < 1) {
            vibrate(10);
            toast(getString(R.string.select_category));
            return;
        }

        Account account = transaction.account;
        account.addNewTransaction(transaction);
        AccountManager accountManager = getApp().getAccountManager();
        accountManager.write(getApp());
        toast("Available Money = " + AccountManager.getTotalAvailableBalance(accountManager.totalAccounts,
                transaction.month, transaction.year));
        finish();
    }


    private void changeMoneyColor() {
        if (transaction.isExpense) transactionAmount.setTextColor(getColorFrom(R.color.red_500));
        else transactionAmount.setTextColor(getColorFrom(R.color.green_500));
    }


    private void createCategoryRadioButtons(RadioGroup categoryRadioGroup, final ArrayList<String> categories) {
        final RadioButton[] category = new RadioButton[categories.size()];
        categoryRadioGroup.setOrientation(RadioGroup.VERTICAL);

        for (int index = 0; index < categories.size(); index++) {
            category[index] = (RadioButton) View.inflate(this, R.layout.layout_transaction_category_radio_button, null);
            category[index].setText(categories.get(index));
            categoryRadioGroup.addView(category[index]);
        }

        categoryRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                transaction.transactionCategory = category[position - 1].toString();
            }
        });
    }


    public void onAmountEnter(View view) {

    }


    public void onDateChanger(View view) {
        int year = transaction.year;
        int month = transaction.month;
        int day = transaction.day;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }
}
