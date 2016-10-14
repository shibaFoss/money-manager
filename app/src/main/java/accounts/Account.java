package accounts;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
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

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void removeTransaction(long transactionId) {
        for (int index = 0; index < transactions.size(); index++)
            if (transactions.get(index).uniqueId == transactionId)
                transactions.remove(index);
    }

    public long getNewTransactionUniqueId(AccountManager accountManager) {
        long id = 1;
        for (Transaction tran : AccountManager.getAllTransactions(accountManager.accounts))
            if (tran.uniqueId >= id)
                id += tran.uniqueId;
        return id;
    }

}
