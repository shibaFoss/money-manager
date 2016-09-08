package accounts;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import core.App;
import utils.WritableObject;

public class AccountManager extends WritableObject {
    private static final long serialVersionUID = 296984946043256L;

    public static final String ACCOUNT_JSON_FILE_NAME = "account_manager";

    public ArrayList<Account> totalAccounts = new ArrayList<>();
    public ArrayList<String> totalIncomeCates = new ArrayList<>();
    public ArrayList<String> expenseCates = new ArrayList<>();

    public AccountManager() {
        addDefaultTransactionsCategories();

    }

    public static AccountManager readData(App app) {
        AccountManager accountManager = new AccountManager();
        try {
            AccountManager am = accountManager.read(app);
            if (am != null) accountManager = am;
            else accountManager.write(app);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return accountManager;
    }

    public void write(App app) {
        try {
            FileOutputStream fileOutput = app.openFileOutput(ACCOUNT_JSON_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
            outputStream.writeObject(this);
            outputStream.close();
            fileOutput.close();
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

    public AccountManager read(App app) {
        try {
            FileInputStream fileInputStream = app.openFileInput(ACCOUNT_JSON_FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (AccountManager) objectInputStream.readObject();
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
    }

    private void addDefaultTransactionsCategories() {
        if (totalIncomeCates.size() < 1) {
            totalIncomeCates.add("Job");
            totalIncomeCates.add("Salary");
            totalIncomeCates.add("Other");
        }

        if (expenseCates.size() < 1) {
            expenseCates.add("Food & Drinks");
            expenseCates.add("Shopping");
            expenseCates.add("Rent");
            expenseCates.add("Daily Needs");
            expenseCates.add("Transportation");
            expenseCates.add("Leisure");
            expenseCates.add("Other");
        }
    }


    public void removeTransaction(ArrayList<Transaction> allTransactions, Transaction tranToDelete) {
        for (Transaction tran : allTransactions) {
            if (tran.uniqueId == tranToDelete.uniqueId)
                allTransactions.remove(tran);
        }
    }

    public static double getTotalBudget(ArrayList<Account> accounts) {
        double money = 0;
        for (Account acc : accounts)
            money += acc.monthlyBudget;
        return money;
    }

    public static double getTotalAvailableBalance(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = getAllTransactions(accounts);
        double totalBalance = 0;
        for (Transaction tran : transactions) {
            if (tran.yearCode <= year && tran.monthCode <= month) {
                if (tran.isExpense) totalBalance -= tran.transactionAmount;
                else totalBalance += tran.transactionAmount;
            }
        }

        return totalBalance;
    }

    public static double getTotalIncome(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = getTransactions(accounts, month, year);
        double income = 0;
        for (Transaction tran : transactions)
            if (!tran.isExpense)
                income += tran.transactionAmount;

        return income;
    }

    public static double getTotalExpenses(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = getTransactions(accounts, month, year);
        double expenses = 0;
        for (Transaction tran : transactions)
            if (tran.isExpense)
                expenses += tran.transactionAmount;

        return expenses;
    }

    public static ArrayList<Transaction> getTransactions(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> trans = new ArrayList<>();
        for (Transaction tran : getAllTransactions(accounts)) {
            if (tran.monthCode == month && tran.yearCode == year)
                trans.add(tran);
        }

        return trans;
    }

    public static ArrayList<Transaction> getTransactions(ArrayList<Account> accounts, int day, int month, int year) {
        ArrayList<Transaction> trans = new ArrayList<>();
        for (Transaction tran : getAllTransactions(accounts)) {
            if (tran.dayCode == day && tran.monthCode == month && tran.yearCode == year)
                trans.add(tran);
        }

        return trans;
    }

    public static ArrayList<Transaction> getAllTransactions(ArrayList<Account> accounts) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Account acc : accounts)
            if (!acc.transactions.isEmpty())
                transactions.addAll(acc.transactions);

        return transactions;
    }

    public long getUniqueAccountId() {
        long id = 1;
        for (Account acc : totalAccounts)
            if (acc.uniqueId >= id)
                id += acc.uniqueId;
        return id;
    }

    public Account getAccountByName(String accountName) {
        for (Account ac : totalAccounts)
            if (ac.accountName.equals(accountName))
                return ac;

        return null;
    }
}
