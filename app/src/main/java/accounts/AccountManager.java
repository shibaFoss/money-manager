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

    public static final String ACCOUNT_JSON_FILE_NAME = "account_manager";

    public ArrayList<Account> totalAccounts = new ArrayList<>();


    public static AccountManager readData(App app) {
        AccountManager accountManager = new AccountManager();

        try {
            AccountManager am = accountManager.read(app);
            if (am != null)
                accountManager = am;
            else
                accountManager.write(app);

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

}
