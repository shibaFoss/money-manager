package accounts;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

import utils.OsUtility;

public class Transaction implements Serializable, Comparable<Transaction> {
    private static final long serialVersionUID = 296684946043656L;

    public Account account;
    public boolean isExpense = true;
    public double transactionAmount = 0;

    public String transactionNote = "";
    public String transactionCategory = "Other";

    public String date;

    public int day;
    public int month;
    public int year;

    public long id;

    public void updateTransactionTime() {
        this.date = OsUtility.getCurrentDate("dd/MM/yyyy");
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int compareTo(Transaction tran) {
        if (id < tran.id)
            return -1;
        else if (id == tran.id)
            return 0;
        else
            return 1;
    }
}
