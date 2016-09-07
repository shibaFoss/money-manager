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
    public ArrayList<String> incomeCategories = new ArrayList<>();
    public ArrayList<String> expenseCategories = new ArrayList<>();


    public AccountManager() {
        if (incomeCategories.size() < 1) {
            incomeCategories.add("Job");
            incomeCategories.add("Salary");
            incomeCategories.add("Other");
        }

        if (expenseCategories.size() < 1) {
            expenseCategories.add("Food & Drinks");
            expenseCategories.add("Shopping");
            expenseCategories.add("Rent");
            expenseCategories.add("Daily Needs");
            expenseCategories.add("Transportation");
            expenseCategories.add("Leisure");
            expenseCategories.add("Other");
        }

    }


    public static AccountManager readData(App app) {
        AccountManager accountManager = new AccountManager();
        try {
            AccountManager am = accountManager.read(app);
            if (am != null)  accountManager = am;
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


    public static double getTotalAvailableBalance(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = getTransactionsOf(accounts);
        double totalBalance = 0;
        for (Transaction tran : transactions) {
            if (tran.year <= year && tran.month <= month) {
                if (tran.isExpense) totalBalance -= tran.transactionAmount;
                else totalBalance += tran.transactionAmount;
            }
        }

        return totalBalance;
    }


    public static double getTotalBudget(ArrayList<Account> accounts) {
        double money = 0;
        for (Account acc : accounts)
            money += acc.monthlyBudget;
        return money;
    }


    public static double getTotalIncomeOfTheMonth(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = findTransactions(accounts, month, year);
        double income = 0;
        for (Transaction tran : transactions)
            if (!tran.isExpense)
                income += tran.transactionAmount;

        return income;
    }


    public static double getTotalExpensesOfTheMonth(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> transactions = findTransactions(accounts, month, year);
        double expenses = 0;
        for (Transaction tran : transactions)
            if (tran.isExpense)
                expenses += tran.transactionAmount;

        return expenses;
    }


    public static ArrayList<Transaction> findTransactions(ArrayList<Account> accounts, int month, int year) {
        ArrayList<Transaction> trans = new ArrayList<>();
        for (Transaction tran : getTransactionsOf(accounts)) {
            if (tran.month == month && tran.year == year)
                trans.add(tran);
        }

        return trans;
    }


    public static ArrayList<Transaction> findTransactions(ArrayList<Account> accounts, int day, int month, int year) {
        ArrayList<Transaction> trans = new ArrayList<>();
        for (Transaction tran : getTransactionsOf(accounts)) {
            if (tran.day == day && tran.month == month && tran.year == year)
                trans.add(tran);
        }

        return trans;
    }


    public static ArrayList<Transaction> getTransactionsOf(ArrayList<Account> accounts) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Account acc : accounts)
            if (!acc.transactions.isEmpty())
                transactions.addAll(acc.transactions);

        return transactions;
    }
}
