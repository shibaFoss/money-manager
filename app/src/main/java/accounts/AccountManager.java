package accounts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;

import core.App;
import utils.WritableObject;

import static utils.WritableObject.readObject;
import static utils.WritableObject.writeObject;

public class AccountManager {

    public final String ACCOUNT_FILE_NAME = "account_manager";
    public App app;
    public File accountFile = new File(App.appDirectory, ACCOUNT_FILE_NAME);

    @SerializedName("totalAccount")
    public ArrayList<Account> totalAccounts = new ArrayList<>();


    public AccountManager(App app) {
        this.app = app;
    }


    public static AccountManager readData(App app) {
        AccountManager accountManager = new AccountManager(app);
        try {
            String data = (String) readObject(accountManager.accountFile);
            if (data != null) {
                AccountManager am = getAccountsData(data);
                if (am != null) accountManager = am;

            } else {
                Gson gson = new Gson();
                String jsonData = gson.toJson(accountManager);
                writeObject(jsonData, App.appDirectory, accountManager.ACCOUNT_FILE_NAME);
            }
        } catch (Throwable err) {
            err.printStackTrace();
        }

        return accountManager;
    }


    private static AccountManager getAccountsData(String data) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(data, AccountManager.class);
    }


    public void saveAccountManager() {
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(this);
            writeObject(jsonData, App.appDirectory, ACCOUNT_FILE_NAME);
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }

}
