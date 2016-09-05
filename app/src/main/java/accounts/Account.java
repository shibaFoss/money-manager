package accounts;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {
    private static final long serialVersionUID = 296984946043656L;

    public String name;
    public String note;
    public String currency;
    public double monthlyBudget;

    public ArrayList<Transaction> transactions = new ArrayList<>();


    public void addNewTransaction(Transaction transaction) {
        transaction.id = generateId();
        this.transactions.add(0, transaction);
    }


    private long generateId() {
        long id = 1;
        for (Transaction tran : transactions) {
            if (tran.id >= id) {
                id += tran.id;
            }
        }
        return id;
    }
}
