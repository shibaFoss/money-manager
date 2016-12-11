package gui.home;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import in.mobi_space.money_manager.R;
import utils.Font;
import utils.OsUtility;
import utils.ViewUtility;

class TransactionListAdapter extends BaseAdapter {

    private BaseActivity activity;
    private OnTransactionClick onTransactionClick;
    private ArrayList<Item> items = new ArrayList<>();
    private int month, year;
    private ArrayList<Account> accounts;


    TransactionListAdapter(HomeActivity homeActivity) {
        this.onTransactionClick = homeActivity;
        this.activity = homeActivity;
    }


    public void setAccounts(ArrayList<Account> accounts, int month, int year) {
        this.accounts = accounts;
        this.month = month;
        this.year = year;
    }


    private void setTransactions(ArrayList<Account> accounts, int month, int year) {
        items.clear();
        for (int day = 31; day > 0; day--) {
            ArrayList<Transaction> transactions = AccountManager.getTransactions(accounts, day, month, year);
            if (!transactions.isEmpty()) {
                Collections.sort(transactions, Collections.reverseOrder());
                Item selectionItem = new Item();
                selectionItem.isSelectionHeader = true;
                selectionItem.date = day + " " + OsUtility.getFullMonthName(month);
                items.add(selectionItem);

                for (Transaction tran : transactions) {
                    Item item = new Item();
                    item.date = tran.transactionDate;
                    item.day = tran.dayCode;
                    item.isSelectionHeader = false;
                    item.transaction = tran;
                    items.add(item);
                }
            }
        }

    }


    @Override
    public int getCount() {
        setTransactions(accounts, month, year);
        return items.size();
    }


    @Override
    public Object getItem(int i) {
        return items.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Item item = items.get(position);
        if (item.isSelectionHeader) {
            view = View.inflate(activity, R.layout.activity_home_transaction_date_header, null);
            TextView txtDate = ((TextView) view.findViewById(R.id.txt_date));
            txtDate.setText(items.get(position).date);
            txtDate.setTypeface(Font.RobotoRegular);
            return view;
        }

        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            view = View.inflate(activity, R.layout.activity_home_transaction, null);
            inflateView(view, viewHolder);
        } else {
            if (view.getId() == R.id.layout_transaction) {
                viewHolder = (ViewHolder) view.getTag();

            } else {
                view = View.inflate(activity, R.layout.activity_home_transaction, null);
                inflateView(view, viewHolder);
            }
        }

        Font.setFont(Font.RobotoRegular, view, R.id.txt_transaction_note);
        Font.setFont(Font.RobotoRegular, view, R.id.txt_transaction_amount);
        Font.setFont(Font.RobotoLight, view, R.id.txt_transactional_account, R.id.txt_transaction_category);

        final Transaction transaction = items.get(position).transaction;
        Account account = activity.getApp().getAccountManager().getAccountByName(transaction.accountName);
        String currency = "";
        if (account != null)
            currency = account.currencySymbol;

        viewHolder.transactionNote.setText(transaction.transactionNote);

        String transactionAmount;
        if (transaction.isExpense) {
            transactionAmount = currency + " " + ViewUtility.getFormattedNumber(transaction.transactionAmount);
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.red_600));
            viewHolder.transactionAmount.setText(transactionAmount);

        } else {
            transactionAmount = currency + " " + ViewUtility.getFormattedNumber(transaction.transactionAmount);
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.green_500));
            viewHolder.transactionAmount.setText(transactionAmount);
        }

        viewHolder.associatedAccount.setText(transaction.accountName);
        viewHolder.transactionCategory.setText(transaction.transactionCategory);
        viewHolder.transactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTransactionClick.onTransactionClick(transaction, position);
            }
        });

        return view;
    }


    private void inflateView(View view, ViewHolder viewHolder) {
        viewHolder.transactionLayout = view.findViewById(R.id.transaction_layout);
        viewHolder.transactionNote = (TextView) view.findViewById(R.id.txt_transaction_note);
        viewHolder.transactionAmount = (TextView) view.findViewById(R.id.txt_transaction_amount);
        viewHolder.associatedAccount = (TextView) view.findViewById(R.id.txt_transactional_account);
        viewHolder.transactionCategory = (TextView) view.findViewById(R.id.txt_transaction_category);
        view.setTag(viewHolder);
    }


    interface OnTransactionClick {
        void onTransactionClick(Transaction transaction, int listPosition);
    }

    private static class ViewHolder {
        View transactionLayout;
        TextView transactionNote, transactionAmount, associatedAccount, transactionCategory;
    }

    private static class Item {
        String date;
        int day;
        boolean isSelectionHeader = false;
        public Transaction transaction;
    }
}
