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
import gui.home.AccountSwitcherPopupMenu.OnAccountSelection;
import gui.home.TransactionListAdapter.OnTransactionClick;
import gui.transaction.TransactionActivity;
import gui.transaction_viewer.TransactionViewerActivity;
import in.mobi_space.money_manager.R;
import libs.Remember;
import utils.DialogUtility;
import utils.Font;
import utils.OsUtility;
import utils.ViewUtility;

import static accounts.AccountManager.getTotalAvailableBalance;
import static accounts.AccountManager.getTotalBudget;
import static accounts.AccountManager.getTotalExpenses;
import static accounts.AccountManager.getTotalIncome;

public class HomeActivity extends BaseActivity implements OnTransactionClick {

    public static final String DEFAULT_ACCOUNT_INDEX = "DEFAULT_ACCOUNT_INDEX";

    private TextView toolbar;
    private ListView transactionList;
    private View overviewHeaderLayout;

    private AccountManager accountManager;
    private TransactionListAdapter transactionListAdapter;
    private NavigationDrawer navigationDrawer;
    private AccountSwitcherPopupMenu accountSwitcherPopupMenu;

    private int month, year;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        Font.setFont(Font.RobotoRegular, this, R.id.txt_toolbar);

        accountManager = getApp().getAccountManager();
        transactionList = (ListView) findViewById(R.id.list_transaction);
        toolbar = (TextView) findViewById(R.id.txt_toolbar);
        accountSwitcherPopupMenu = new AccountSwitcherPopupMenu(this);
        navigationDrawer = new NavigationDrawer(this);

        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);

        //First we need to select the default account that user has been saved as his default account to be
        //shown at the overview cards. But we don't find any default account then we will show all account's
        //total information on the overview cards.
        //-------------------------------------------------------------------------------//
        TextView toolbar = (TextView) findViewById(R.id.txt_toolbar);
        int defaultAccountIndex = Remember.getInt(DEFAULT_ACCOUNT_INDEX, -1);
        updateUI(getAccountsByIndexId(defaultAccountIndex), month, year);
        toolbar.setText((defaultAccountIndex == -1) ?
                getString(R.string.all_accounts) :
                getAccountsByIndexId(defaultAccountIndex).get(0).accountName);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUIOnResume();
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }


    @Override
    public void onTransactionClick(Transaction transaction, int listPosition) {
        Intent intent = new Intent(this, TransactionViewerActivity.class);
        intent.putExtra(TransactionViewerActivity.TRANSACTION_KEY, transaction);
        startActivity(intent);
    }


    public void onAccountSwitcher(final View view) {
        //We need to add an extra option called "All Accounts" before all other acc.
        String[] options = new String[1 + accountManager.accounts.size()];
        for (int i = 0; i < options.length; i++) {
            if (i == 0) options[i] = getString(R.string.all_accounts) + "                  ";
            else options[i] = accountManager.accounts.get(i - 1).accountName;
        }


        //Building the dialog and show to user.
        accountSwitcherPopupMenu.setAccountSelectionListener(new OnAccountSelection() {
            @Override
            public void onSelect(String accountName) {
                setDefaultAccountToBeOverviewFirst(accountName);
            }
        });
        accountSwitcherPopupMenu.setAnchorView(findViewById(R.id.txt_toolbar));
        accountSwitcherPopupMenu.addItems(options);
        accountSwitcherPopupMenu.show();
    }


    private ArrayList<Account> getAccountsByIndexId(int accountIndexId) {
        if (accountIndexId == -1) { //it means we need to show all account's total overview.
            return accountManager.accounts;

        } else {
            Account acc = accountManager.accounts.get(accountIndexId);
            if (acc != null) {
                ArrayList<Account> accounts = new ArrayList<>();
                accounts.add(acc);
                return accounts;
            }
        }

        return accountManager.accounts;
    }


    private void setDefaultAccountToBeOverviewFirst(String accountName) {
        Account account = accountManager.getAccountByName(accountName);
        if (account != null) {
            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(account);

            TextView toolbar = (TextView) findViewById(R.id.txt_toolbar);
            toolbar.setText(accountName);
            updateUI(accounts, month, year);

            //we need to save the account as the default account.
            Remember.putInt(DEFAULT_ACCOUNT_INDEX, accountManager.getAccountIndexByName(accountName));

        } else {
            TextView toolbar = (TextView) findViewById(R.id.txt_toolbar);
            toolbar.setText(getString(R.string.all_accounts));
            updateUI(accountManager.accounts, month, year);

            //we need to save the account as the default account.
            Remember.putInt(DEFAULT_ACCOUNT_INDEX, -1);
        }
    }


    /**
     * Update the transaction list information after activity get resumed. This
     * function is used if any new transaction got listed in account and need to update
     * in the list view.
     */
    private void updateUIOnResume() {
        if (toolbar == null)
            toolbar = (TextView) findViewById(R.id.txt_toolbar);

        int defaultAccount = Remember.getInt(DEFAULT_ACCOUNT_INDEX, -1);
        if (defaultAccount == -1) { //-1 represent for all accounts.
            updateListAdapter(accountManager.accounts);
            toolbar.setText(getString(R.string.all_accounts));

        } else {
            Account acc = accountManager.accounts.get(defaultAccount);
            if (acc != null) {
                ArrayList<Account> accounts = new ArrayList<>();
                accounts.add(acc);
                updateListAdapter(accounts);
                toolbar.setText(acc.accountName);
            }
        }
    }


    private void updateListAdapter(ArrayList<Account> accounts) {
        updateHeaderView(accounts, month, year);
        if (transactionListAdapter != null) {
            transactionListAdapter.setAccounts(accounts, month, year);
            transactionListAdapter.notifyDataSetChanged();

        } else {
            vibrate(10);
            updateUI(accounts, month, year);
        }
    }


    public void onAddExpense(View view) {
        addNewTransaction(true);
    }


    public void onAddIncome(View view) {
        addNewTransaction(false);
    }


    private void addNewTransaction(final boolean isExpense) {
        int accountIndex = Remember.getInt(DEFAULT_ACCOUNT_INDEX, -1);
        if (accountIndex == -1) {
            String accNames[] = new String[accountManager.accounts.size()];
            for (int i = 0; i < accNames.length; i++)
                accNames[i] = accountManager.accounts.get(i).accountName;

            promptUserToChooseTransactionAccount(isExpense, accNames);
        } else {
            startTransactionActivity(accountIndex, isExpense);
        }
    }


    private void promptUserToChooseTransactionAccount(final boolean isExpense, CharSequence[] accountNameArray) {
        DialogUtility.getDefaultBuilder(this)
                .title(isExpense ? getString(R.string.choose_expense_account) : getString(R.string.choose_income_account))
                .items(accountNameArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        startTransactionActivity(which, isExpense);
                        dialog.dismiss();
                    }
                }).show();
    }


    private void startTransactionActivity(int which, boolean isExpense) {
        Intent intent = new Intent(HomeActivity.this, TransactionActivity.class);
        intent.putExtra(TransactionActivity.ACCOUNT_ARRAY_POSITION, which);
        intent.putExtra(TransactionActivity.IS_EXPENSE, isExpense);
        startActivity(intent);
    }


    public void updateUI(ArrayList<Account> accounts, int month, int year) {
        updateHeaderView(accounts, month, year);
        if (transactionListAdapter == null)
            transactionListAdapter = new TransactionListAdapter(this);

        transactionListAdapter.setAccounts(accounts, month, year);
        transactionList.setAdapter(transactionListAdapter);
    }


    private void updateHeaderView(ArrayList<Account> accounts, int month, int year) {
        if (overviewHeaderLayout == null)
            overviewHeaderLayout = View.inflate(this, R.layout.activity_home_overview, null);

        View headerView = overviewHeaderLayout;
        TextView overviewDate = (TextView) headerView.findViewById(R.id.txt_overview_month);
        TextView overviewTotalBalance = (TextView) headerView.findViewById(R.id.txt_total_balance);
        TextView overviewSavingsAmount = (TextView) headerView.findViewById(R.id.txt_overview_savings);
        TextView overviewTotalIncome = (TextView) headerView.findViewById(R.id.txt_total_income);
        TextView overviewBudget = (TextView) headerView.findViewById(R.id.txt_overview_budget);
        TextView overviewTotalExpense = (TextView) headerView.findViewById(R.id.txt_total_expenses);

        Font.setFont(Font.RobotoRegular, headerView, R.id.txt_month, R.id.txt_total_available_balance,
                R.id.txt_saving, R.id.txt_total_income_preview, R.id.txt_budget,
                R.id.txt_total_expenses_preview);

        Font.setFont(Font.RobotoRegular, headerView, R.id.txt_overview_month, R.id.txt_total_balance,
                R.id.txt_overview_savings, R.id.txt_total_income, R.id.txt_overview_budget,
                R.id.txt_total_expenses);

        if (transactionList != null)
            if (transactionList.getHeaderViewsCount() < 1)
                transactionList.addHeaderView(headerView);

        //----------------------------------------------------------------------------------------//
        String currency = accounts.get(0).currencySymbol;
        String monthName = OsUtility.getFullMonthName(month) + " " + year;
        double totalAvailableBalance = getTotalAvailableBalance(accounts, month, year);

        double totalIncomeOfTheMonth = getTotalIncome(accounts, month, year);
        double totalExpensesOfTheMonth = getTotalExpenses(accounts, month, year);

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
            overviewTotalBalance.setTextColor(getColorFrom(R.color.indigo_500));

        overviewTotalBalance.setText(currency + " " + ViewUtility.getFormattedNumber(totalAvailableBalance));
        if (totalSaving < 1) {
            overviewSavingsAmount.setText(R.string.none);

        } else {
            overviewSavingsAmount.setText(currency + " " + ViewUtility.getFormattedNumber(totalSaving));
        }

        overviewTotalIncome.setText(currency + " " + ViewUtility.getFormattedNumber(totalIncomeOfTheMonth));
        overviewBudget.setText(totalBudget < 1 ? getString(R.string.none) : currency + " " + ViewUtility.getFormattedNumber(totalBudget));
        overviewTotalExpense.setText(currency + " " + ViewUtility.getFormattedNumber(totalExpensesOfTheMonth));
    }


}
