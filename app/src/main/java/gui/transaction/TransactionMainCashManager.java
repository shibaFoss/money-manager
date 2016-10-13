package gui.transaction;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import accounts.Transaction;
import in.mobi_space.money_manager.R;

import static utils.ViewUtility.makeRoundedValue;

public class TransactionMainCashManager implements View.OnClickListener {
    public TransactionActivity activity;
    public Transaction transaction;
    private EditText transactionNoteEdit;
    private TextView transactionAmountPreview, transactionDatePreview;

    public TransactionMainCashManager(TransactionActivity activity, Transaction transaction) {
        this.activity = activity;
        this.transaction = transaction;

        transactionAmountPreview = (TextView) activity.findViewById(R.id.txt_transaction_amount);
        transactionDatePreview = (TextView) activity.findViewById(R.id.txt_transaction_date);
        transactionNoteEdit = (EditText) activity.findViewById(R.id.edit_transaction_note);

        activity.findViewById(R.id.bnt_date_selector).setOnClickListener(this);
        activity.findViewById(R.id.bnt_amount_selector).setOnClickListener(this);

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
        }
    }

    private void onTransactionAmountSelect() {
        CalculatorDialog calculatorDialog = new CalculatorDialog(activity,
                (transaction.transactionAmount < 1 ? "" : makeRoundedValue(transaction.transactionAmount)));
        calculatorDialog.setOnSubmitResultListener(new CalculatorDialog.OnSubmitResult() {
            @Override
            public void onSubmitResult(double result, Dialog dialog) {
                result = Math.floor(result * 100) / 100;
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

    public String getTransactionNote() {
        String input = transactionNoteEdit.getText().toString();
        if (input.length() > 1) return input;
        else return "Untitled transaction";
    }

    private void updateViews() {
        transactionDatePreview.setText(String.valueOf(transaction.dayCode + "/" + (transaction.monthCode + 1) + "/" +
                transaction.yearCode));
        String currency = activity.getApp().getAccountManager().getAccountByName(transaction.accountName).currencySymbol;
        transactionAmountPreview.setText(String.valueOf(currency + " " + makeRoundedValue(transaction.transactionAmount)));

        if (transaction.isExpense)
            transactionAmountPreview.setTextColor(activity.getColorFrom(R.color.md_red_500));
        else
            transactionAmountPreview.setTextColor(activity.getColorFrom(R.color.md_green_500));
    }

}
