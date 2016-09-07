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
import in.softc.aladindm.R;
import utils.OsUtility;

public class TransactionListAdapter extends BaseAdapter {

    private BaseActivity activity;
    private OnTransactionClick onTransactionClick;
    private ArrayList<Item> items = new ArrayList<>();

    public TransactionListAdapter(HomeActivity homeActivity, ArrayList<Account> accounts, int month, int year) {
        this.onTransactionClick = homeActivity;
        this.activity = homeActivity;
        setTransactions(accounts, month, year);
    }

    private void setTransactions(ArrayList<Account> accounts, int month, int year) {
        items.clear();
        for (int day = 31; day > 0; day--) {
            ArrayList<Transaction> transactions = AccountManager.findTransactions(accounts, day, month, year);
            if (!transactions.isEmpty()) {
                Collections.sort(transactions, Collections.reverseOrder());
                Item selectionItem = new Item();
                selectionItem.isSelectionHeader = true;
                selectionItem.date = day + " " + OsUtility.getFullMonthName(month);
                items.add(selectionItem);

                for (Transaction tran : transactions) {
                    Item item = new Item();
                    item.date = tran.date;
                    item.day = tran.day;
                    item.isSelectionHeader = false;
                    item.transaction = tran;
                    items.add(item);
                }
            }
        }

    }

    @Override
    public int getCount() {
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
            ((TextView) view.findViewById(R.id.txt_date)).setText(items.get(position).date);
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

        final Transaction transaction = items.get(position).transaction;
        String moneyAmount = String.valueOf(transaction.transactionAmount);
        String currency = transaction.account.currency;

        viewHolder.transactionNote.setText(transaction.transactionNote);

        String transactionAmount;
        if (transaction.isExpense) {
            transactionAmount = currency + " " + moneyAmount;
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.red_500));
            viewHolder.transactionAmount.setText(transactionAmount);

        } else {
            transactionAmount = currency + " " + moneyAmount;
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.green_500));
            viewHolder.transactionAmount.setText(transactionAmount);
        }

        viewHolder.assosiateAccount.setText(transaction.account.name);
        viewHolder.transactionCategory.setText(transaction.transactionCategory);
        viewHolder.transactionLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onTransactionClick.onTransactionClick(transaction, position);
                return true;
            }
        });

        return view;
    }

    private void inflateView(View view, ViewHolder viewHolder) {
        viewHolder.transactionLayout = view.findViewById(R.id.transaction_layout);
        viewHolder.transactionNote = (TextView) view.findViewById(R.id.txt_transaction_note);
        viewHolder.transactionAmount = (TextView) view.findViewById(R.id.txt_transaction_amount);
        viewHolder.assosiateAccount = (TextView) view.findViewById(R.id.txt_transactional_account);
        viewHolder.transactionCategory = (TextView) view.findViewById(R.id.txt_transaction_category);
        view.setTag(viewHolder);
    }

    public interface OnTransactionClick {
        void onTransactionClick(Transaction transaction, int listPosition);
    }

    public static class ViewHolder {
        public View transactionLayout;
        public TextView transactionNote, transactionAmount, assosiateAccount, transactionCategory;
    }

    public static class Item {
        public String date;
        public int day;
        public boolean isSelectionHeader = false;
        public Transaction transaction;
    }
}
