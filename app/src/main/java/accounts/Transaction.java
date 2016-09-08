package accounts;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

import utils.OsUtility;

public class Transaction implements Serializable, Comparable<Transaction> {
    private static final long serialVersionUID = 296684946043656L;

    public long uniqueId;
    public String accountName;
    public long associateAccountId;

    public boolean isExpense = true;
    public double transactionAmount = 0;

    public String transactionNote = "";
    public String transactionCategory = "Other";

    public String transactionDate;
    public int dayCode;
    public int monthCode;
    public int yearCode;

    public void updateTransactionTime() {
        this.transactionDate = OsUtility.getCurrentDate("dd/MM/yyyy");
        this.monthCode = Calendar.getInstance().get(Calendar.MONTH);
        this.yearCode = Calendar.getInstance().get(Calendar.YEAR);
        this.dayCode = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int compareTo(@NonNull Transaction tran) {
        if (uniqueId < tran.uniqueId)
            return -1;
        else if (uniqueId == tran.uniqueId)
            return 0;
        else
            return 1;
    }

}
