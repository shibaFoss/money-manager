package gui.transaction;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import in.mobi_space.money_manager.R;
import utils.ViewUtility;

import static utils.ViewUtility.getFormattedNumber;
import static utils.ViewUtility.makeRoundedValue;

class TransactionMainCashManager implements View.OnClickListener, TransactionAccountSwitcherPopupMenu.OnAccountSelection {

    public TransactionActivity activity;
    public Transaction transaction;
    private TransactionAccountSwitcherPopupMenu accountSwitcher;
    private EditText transactionNoteEdit;
    private TextView transactionAmountPreview, transactionDatePreview, transactionAccountPreview;

    TransactionMainCashManager(TransactionActivity activity, Transaction transaction) {
        this.activity = activity;
        this.transaction = transaction;

        transactionAmountPreview = (TextView) activity.findViewById(R.id.txt_transaction_amount);
        transactionAccountPreview = (TextView) activity.findViewById(R.id.txt_account);
        transactionDatePreview = (TextView) activity.findViewById(R.id.txt_transaction_date);
        transactionNoteEdit = (EditText) activity.findViewById(R.id.edit_transaction_note);

        activity.findViewById(R.id.bnt_date_selector).setOnClickListener(this);
        activity.findViewById(R.id.bnt_amount_selector).setOnClickListener(this);
        activity.findViewById(R.id.txt_account).setOnClickListener(this);


        transactionAmountPreview.requestFocus();
        onTransactionAmountSelect();

        updateViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnt_date_selector:
                onTransactionDateSelect();
                break;

            case R.id.bnt_amount_selector:
                onTransactionAmountSelect();
                break;

            case R.id.txt_account:
                onTransactionAccountSelect();
                break;
        }
    }


    @Override
    public void onSelect(String accountName) {
        accountSwitcher.close();
        if (accountName != null) {
            AccountManager am = activity.getApp().getAccountManager();
            Account account = am.getAccountByName(accountName);
            if (account != null) {
                transaction.accountName = account.accountName;
                transactionAccountPreview.setText(accountName);
            }
        }
    }

    private void onTransactionAccountSelect() {
        if (accountSwitcher == null) {
            accountSwitcher = new TransactionAccountSwitcherPopupMenu(activity);
            accountSwitcher.setAccountSelectionListener(this);
            accountSwitcher.addItems(AccountManager.getAllAccountNames(activity.getApp().getAccountManager()));
            accountSwitcher.setAnchorView(activity.findViewById(R.id.txt_transaction_account));
        }

        if (accountSwitcher != null)
            accountSwitcher.show();
    }

    private void onTransactionAmountSelect() {
        CalculatorDialog calculatorDialog = new CalculatorDialog(activity,
                (transaction.transactionAmount < 1 ? "" : makeRoundedValue(transaction.transactionAmount)));
        calculatorDialog.setOnSubmitResultListener(new CalculatorDialog.OnSubmitResult() {
            @Override
            public void onSubmitResult(double result, Dialog dialog) {
                result = Double.valueOf(ViewUtility.makeRoundedValue(result));
                transaction.transactionAmount = result;
                updateViews();
            }
        });

        calculatorDialog.show();
    }

    private void onTransactionDateSelect() {
        int year = transaction.yearCode;
        int month = transaction.monthCode;
        int day = transaction.dayCode;
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                transaction.transactionDate = day + "/" + (month + 1) + "/" + year;
                transaction.dayCode = day;
                transaction.monthCode = month;
                transaction.yearCode = year;
                updateViews();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    String getTransactionNote() {
        String input = transactionNoteEdit.getText().toString();
        if (input.length() > 1) return input;
        else return "Untitled transaction";
    }

    private void updateViews() {
        String date = transaction.dayCode + "/" + (transaction.monthCode + 1) + "/" + transaction.yearCode;
        transactionDatePreview.setText(date);

        AccountManager am = activity.getApp().getAccountManager();
        String currency = am.getAccountByName(transaction.accountName).currencySymbol;
        transactionAmountPreview.setText(currency + " " + getFormattedNumber(transaction.transactionAmount));
        if (transaction.isExpense) {
            transactionAmountPreview.setTextColor(activity.getColorFrom(R.color.md_red_600));
        } else {
            transactionAmountPreview.setTextColor(activity.getColorFrom(R.color.md_green_500));
        }


        String transactionAccount = transaction.accountName;
        transactionAccountPreview.setText(transactionAccount);
    }


}
