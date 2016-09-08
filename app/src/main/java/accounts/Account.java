package accounts;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private static final long serialVersionUID = 296984946043656L;

    public long uniqueId;
    public String accountName;
    public String currencySymbol;
    public double monthlyBudget;
    public ArrayList<Transaction> transactions = new ArrayList<>();

    public void addNewTransaction(Transaction transaction) {
        this.transactions.add(0, transaction);
    }

    public long getNewTransactionUniqueId() {
        long id = 1;
        for (Transaction tran : transactions)
            if (tran.uniqueId >= id)
                id += tran.uniqueId;
        return id;
    }

}
