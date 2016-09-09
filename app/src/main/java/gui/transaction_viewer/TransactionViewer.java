package gui.transaction_viewer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import gui.BaseActivity;
import gui.static_dialogs.YesNoDialog;
import in.softc.aladindm.R;

public class TransactionViewer extends BaseActivity {
    public static final String TRANSACTION_KEY = "TRANSACTION_KEY";

    private Transaction transaction;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_transaction_viewer;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        Intent intent = getIntent();
        Transaction tran = (Transaction) intent.getSerializableExtra(TRANSACTION_KEY);
        if (tran != null) {
            transaction = tran;
        }

    }

    @Override
    public void onClosed() {
        finish();
    }

    public void onDeleteTransaction(View view) {
        YesNoDialog deletePrompt = new YesNoDialog(this) {
            @Override
            public void onYes(Dialog dialog) {
                dialog.dismiss();
                if (transaction != null) {
                    AccountManager manager = getApp().getAccountManager();
                    Account account = manager.getAccountByName(transaction.accountName);
                    account.removeTransaction(transaction.uniqueId);

                    manager.write(getApp());
                    vibrate(5);
                    toast(R.string.deleted);
                    finish();
                }
            }

            @Override
            public void onNo(Dialog dialog) {
                dialog.dismiss();
            }
        };
        deletePrompt.show(R.string.delete, R.string.cancel, R.string.are_you_sure_want_to_delete);
    }

    public void onEditTransaction(View view) {

    }

    public void onBack(View view) {
        finish();
    }
}
