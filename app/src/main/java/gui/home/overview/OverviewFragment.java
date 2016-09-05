package gui.home.overview;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseFragment;
import gui.home.HomeActivity;
import in.softc.aladindm.R;
import utils.OsUtility;

import static accounts.AccountManager.*;

public class OverviewFragment extends BaseFragment implements ExpenseListAdapter.OnTransactionClick {

    private ListView expensesList;
    private ExpenseListAdapter expenseListAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_overflow;
    }


    @Override
    protected void onAfterLayoutLoad(View layoutView, Bundle savedInstanceState) {
        HomeActivity activity = (HomeActivity) getBaseActivity();
        activity.changeToolbarTitle(getString(R.string.all_accounts));

        expensesList = (ListView) layoutView.findViewById(R.id.list_transaction);
        expenseListAdapter = new ExpenseListAdapter(this);
        expensesList.setAdapter(expenseListAdapter);

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        AccountManager accountManager = getApp().getAccountManager();

        updateOverviewInformation(accountManager.totalAccounts, month, year);
    }


    public void updateOverviewInformation(ArrayList<Account> accounts, int month, int year) {
        if (expensesList != null && expenseListAdapter != null) {
            updateOverviewHeader(accounts, month, year);

            ArrayList<Transaction> transactionOfTheMonth = AccountManager.findTransactions(accounts, month, year);
            Collections.sort(transactionOfTheMonth, Collections.reverseOrder());
            expenseListAdapter.setTransactions(transactionOfTheMonth);

            expenseListAdapter.notifyDataSetChanged();
        }
    }


    private void updateOverviewHeader(ArrayList<Account> accounts, int month, int year) {
        View accountOverview = View.inflate(getBaseActivity(), R.layout.layout_overview, null);
        TextView overviewDate = (TextView) accountOverview.findViewById(R.id.txt_overview_month);
        TextView overviewTotalBalance = (TextView) accountOverview.findViewById(R.id.txt_total_balance);

        TextView overviewSavingsAmount = (TextView) accountOverview.findViewById(R.id.txt_overview_savings);
        TextView overviewTotalIncome = (TextView) accountOverview.findViewById(R.id.txt_total_income);

        TextView overviewBudget = (TextView) accountOverview.findViewById(R.id.txt_overview_budget);
        TextView overviewTotalExpense = (TextView) accountOverview.findViewById(R.id.txt_total_expenses);
        if (expensesList != null)
            expensesList.addHeaderView(accountOverview);

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
        overviewTotalBalance.setText(String.valueOf(currency + " " + totalAvailableBalance));

        overviewSavingsAmount.setText(String.valueOf(currency + " " + totalSaving));
        overviewTotalIncome.setText(String.valueOf(currency + " " + totalIncomeOfTheMonth));

        overviewBudget.setText(totalBudget < 1 ? "Can't show" : String.valueOf(currency + " " + totalBudget));
        overviewTotalExpense.setText(String.valueOf(currency + " " + totalExpensesOfTheMonth));
    }


    @Override
    public void onTransactionClick(Transaction transaction, int listPosition) {
        getBaseActivity().toast(String.valueOf("Amount  = " + transaction.account.currency + " " + transaction
                .transactionAmount));
    }
}

