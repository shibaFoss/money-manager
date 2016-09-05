package accounts;

import java.io.Serializable;
import java.util.Calendar;

import utils.OsUtility;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 296684946043656L;

    public Account account;
    public boolean isExpense = true;
    public double transactionAmount;

    public String transactionNote;
    public String transactionCategory;

    public String memoImagePath;
    public String colorCode;

    public String date;

    public int day;
    public int month;
    public int year;

    public long id;


    public void updateTransactionTime() {
        this.date = OsUtility.getCurrentDate("dd/MM/YYYY");
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
