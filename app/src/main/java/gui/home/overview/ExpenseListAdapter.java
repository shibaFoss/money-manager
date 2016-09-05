package gui.home.overview;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accounts.Transaction;
import gui.BaseActivity;
import in.softc.aladindm.R;

public class ExpenseListAdapter extends BaseAdapter {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private BaseActivity activity;
    private OverviewFragment overviewFragment;
    private OnTransactionClick onTransactionClick;


    public ExpenseListAdapter(OverviewFragment overviewFragment) {
        this.overviewFragment = overviewFragment;
        this.onTransactionClick = overviewFragment;
        this.activity = overviewFragment.getBaseActivity();
    }


    public void setTransactions(ArrayList<Transaction> totalExpense) {
        this.transactions = totalExpense;
    }


    @Override
    public int getCount() {
        return transactions.size();
    }


    @Override
    public Object getItem(int i) {
        return transactions.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(activity, R.layout.layout_transaction, null);
            viewHolder = new ViewHolder();
            viewHolder.transactionLayout = view.findViewById(R.id.transaction_layout);
            viewHolder.categoryColor = (ImageView) view.findViewById(R.id.bnt_category_color);
            viewHolder.transactionNote = (TextView) view.findViewById(R.id.txt_transaction_note);
            viewHolder.transactionAmount = (TextView) view.findViewById(R.id.txt_transaction_amount);
            viewHolder.accountName = (TextView) view.findViewById(R.id.txt_transactional_account);
            viewHolder.transactionDate = (TextView) view.findViewById(R.id.txt_transaction_date);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Transaction transaction = transactions.get(position);
        String amount = String.valueOf(transaction.transactionAmount);
        String currency = transaction.account.currency;
        if (transaction.isExpense) viewHolder.categoryColor.setBackgroundColor(Color.parseColor("#f44336"));
        else viewHolder.categoryColor.setBackgroundColor(Color.parseColor("#66bb6a"));

        viewHolder.transactionNote.setText(transaction.transactionNote);

        String transactionAmount;
        if (transaction.isExpense) {
            transactionAmount = "- " + currency + " " + amount;
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.red_500));
            viewHolder.transactionAmount.setText(transactionAmount);
        } else {
            transactionAmount = "+ " + currency + " " + amount;
            viewHolder.transactionAmount.setTextColor(activity.getColorFrom(R.color.green_500));
            viewHolder.transactionAmount.setText(transactionAmount);
        }

        viewHolder.accountName.setText(transaction.account.name);
        viewHolder.transactionDate.setText(transaction.date);
        viewHolder.transactionLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onTransactionClick.onTransactionClick(transaction, position);
                return true;
            }
        });

        return view;
    }


    public interface OnTransactionClick {
        void onTransactionClick(Transaction transaction, int listPosition);
    }

    public static class ViewHolder {
        public View transactionLayout;
        public ImageView categoryColor;
        public TextView transactionNote, transactionAmount, accountName, transactionDate;
    }
}
