package accounts;

import java.util.ArrayList;

import core.App;

import static accounts.AccountManager.getAllTransactions;

public class SearchManager {

    public static ArrayList<Transaction> findTransactionsByNote(App app, String matchingQuote) {
        ArrayList<Transaction> matches = new ArrayList<>();
        AccountManager manager = app.getAccountManager();

        for (Transaction tran : getAllTransactions(manager.accounts)) {
            String note = tran.transactionNote.toLowerCase();
            if (note.contains(matchingQuote.toLowerCase()))
                matches.add(tran);
        }
        return matches;
    }

    public static ArrayList<Transaction> findTransactionsByAccountId(App app, long accountId) {
        ArrayList<Transaction> matches = new ArrayList<>();
        AccountManager manager = app.getAccountManager();
        for (Transaction tran : getAllTransactions(manager.accounts)) {
            if (tran.associateAccountId == accountId)
                matches.add(tran);
        }
        return matches;
    }

    public static ArrayList<Transaction> findTransactionsByAccountName(App app, String accountName) {
        ArrayList<Transaction> matches = new ArrayList<>();
        AccountManager manager = app.getAccountManager();
        for (Transaction tran : getAllTransactions(manager.accounts)) {
            if (tran.accountName.equals(accountName))
                matches.add(tran);
        }
        return matches;
    }


    public Account findAccountsByName(App app, String accountName) {
        for (Account acc : app.getAccountManager().accounts) {
            if (acc.accountName.equals(accountName))
                return acc;
        }
        return null;
    }

}
