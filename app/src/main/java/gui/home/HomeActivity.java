package gui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import gui.transaction.TransactionActivity;
import in.softc.aladindm.R;
import utils.DialogUtility;
import utils.OsUtility;
import utils.ViewUtility;

import static accounts.AccountManager.getTotalAvailableBalance;
import static accounts.AccountManager.getTotalBudget;
import static accounts.AccountManager.getTotalExpensesOfTheMonth;
import static accounts.AccountManager.getTotalIncomeOfTheMonth;
import static utils.ViewUtility.makeRoundedValue;

public class HomeActivity extends BaseActivity implements TransactionListAdapter.OnTransactionClick {

    private ListView transactionList;
    private View overviewHeaderLayout;
    private AccountManager accountManager;
    private int month, year;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        accountManager = getApp().getAccountManager();
        transactionList = (ListView) findViewById(R.id.list_transaction);

        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);

        updateTransactions(month, year);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTransactions(month, year);
    }

    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }

    public void onAddExpense(View view) {
        addNewTransaction(true);
    }

    public void onAddIncome(View view) {
        addNewTransaction(false);
    }

    private void addNewTransaction(final boolean isExpense) {
        String accountNameArray[] = new String[accountManager.totalAccounts.size()];
        for (int i = 0; i < accountNameArray.length; i++)
            accountNameArray[i] = accountManager.totalAccounts.get(i).name;

        DialogUtility.getDefaultBuilder(this)
                .title(R.string.select_account)
                .items(accountNameArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = new Intent(HomeActivity.this, TransactionActivity.class);
                        intent.putExtra(TransactionActivity.ACCOUNT_ARRAY_POSITION, which);
                        intent.putExtra(TransactionActivity.IS_EXPENSE, isExpense);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).show();
    }

    public void updateTransactions(int month, int year) {
        updateOverviewInformation(accountManager.totalAccounts, month, year);
    }

    public void updateOverviewInformation(ArrayList<Account> accounts, int month, int year) {
        updateOverviewHeader(accounts, month, year);
        TransactionListAdapter adapter = new TransactionListAdapter(this, accounts, month, year);
        transactionList.setAdapter(adapter);
    }

    private void updateOverviewHeader(ArrayList<Account> accounts, int month, int year) {
        View accountOverview;
        if (overviewHeaderLayout != null) {
            accountOverview = overviewHeaderLayout;
        } else {
            overviewHeaderLayout = View.inflate(this, R.layout.activity_home_overview, null);
            accountOverview = overviewHeaderLayout;
        }

        TextView overviewDate = (TextView) accountOverview.findViewById(R.id.txt_overview_month);
        TextView overviewTotalBalance = (TextView) accountOverview.findViewById(R.id.txt_total_balance);

        TextView overviewSavingsAmount = (TextView) accountOverview.findViewById(R.id.txt_overview_savings);
        TextView overviewTotalIncome = (TextView) accountOverview.findViewById(R.id.txt_total_income);

        TextView overviewBudget = (TextView) accountOverview.findViewById(R.id.txt_overview_budget);
        TextView overviewTotalExpense = (TextView) accountOverview.findViewById(R.id.txt_total_expenses);

        if (transactionList != null) {
            if (transactionList.getHeaderViewsCount() < 1) {
                transactionList.addHeaderView(accountOverview);
            }
        }

        //----------------------------------------------------------------------------------------//
        String currency = accounts.get(0).currency;
        String monthName = OsUtility.getFullMonthName(month) + " " + year;
        double totalAvailableBalance = getTotalAvailableBalance(accounts, month, year);

        double totalIncomeOfTheMonth = getTotalIncomeOfTheMonth(accounts, month, year);
        double totalExpensesOfTheMonth = getTotalExpensesOfTheMonth(accounts, month, year);

        double totalBudget = getTotalBudget(accounts);
        double totalSaving = totalIncomeOfTheMonth - totalExpensesOfTheMonth;

        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (currentMonth != month && currentYear != year)
            totalBudget = 0;

        overviewDate.setText(monthName);
        if (totalAvailableBalance < 1)
            overviewTotalBalance.setTextColor(getColorFrom(R.color.red_500));
        else
            overviewTotalBalance.setTextColor(getColorFrom(R.color.blue_500));

        overviewTotalBalance.setText(String.valueOf(currency + " " + makeRoundedValue(totalAvailableBalance)));
        if (totalSaving < 1)
            overviewSavingsAmount.setText(R.string.none);
        else
            overviewSavingsAmount.setText(String.valueOf(currency + " " + makeRoundedValue(totalSaving)));

        overviewTotalIncome.setText(String.valueOf(currency + " " + makeRoundedValue(totalIncomeOfTheMonth)));
        overviewBudget.setText(totalBudget < 1 ?
                getString(R.string.cant_show) : String.valueOf(currency + " " + totalBudget));
        overviewTotalExpense.setText(String.valueOf(currency + " " + makeRoundedValue(totalExpensesOfTheMonth)));
    }

    @Override
    public void onTransactionClick(Transaction transaction, int listPosition) {

    }

}
