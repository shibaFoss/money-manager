package gui.transaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import accounts.Account;
import accounts.AccountManager;
import accounts.Transaction;
import core.App;
import gui.BaseActivity;
import in.softc.aladindm.R;
import utils.FileUtils;

public class TransactionActivity extends BaseActivity {

    public static final String ACCOUNT_ARRAY_POSITION = "ACCOUNT_ARRAY_POSITION";
    public static final String IS_EXPENSE = "IS_EXPENSE";

    private TransactionMainCashManager cashManager;

    private TransactIonMemoManager memoManager;
    private Transaction transaction;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_transaction;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        AccountManager accountManager = getApp().getAccountManager();

        Intent intent = getIntent();
        int accountPosition = intent.getIntExtra(ACCOUNT_ARRAY_POSITION, -1);
        boolean isExpenseTransaction = intent.getBooleanExtra(IS_EXPENSE, true);

        if (!validateAccount(accountPosition)) return;

        Account account = accountManager.totalAccounts.get(accountPosition);
        transaction = new Transaction();
        transaction.accountName = account.accountName;
        transaction.uniqueId = account.getNewTransactionUniqueId();
        transaction.isExpense = isExpenseTransaction;
        transaction.updateTransactionTime();

        cashManager = new TransactionMainCashManager(this, transaction);
        new TransactionCategoryManager(this, transaction);
        memoManager = new TransactIonMemoManager(this, transaction);
    }

    private boolean validateAccount(int accountPosition) {
        if (accountPosition == -1) {
            vibrate(10);
            toast(getString(R.string.something_went_wrong));
            finish();
            return false;
        }
        return true;
    }

    @Override
    public void onClosed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TransactIonMemoManager.SELECT_FILE_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == TransactIonMemoManager.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void onSaveTransaction(View view) {
        if (transaction.transactionAmount == 0) {
            vibrate(10);
            toast(getString(R.string.give_transaction_amount));
            return;
        }

        transaction.transactionNote = cashManager.getTransactionNote();
        Account account = getApp().getAccountManager().getAccountByName(transaction.accountName);
        account.addNewTransaction(transaction);
        getApp().getAccountManager().write(getApp());
        finish();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        if (bitmap == null) return;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        FileUtils.makeDirectory(new File(App.appDirectory));
        File destination = new File(App.appDirectory, memoManager.transaction.uniqueId + ".jpg");
        FileOutputStream outputStream;

        try {
            destination.createNewFile();
            outputStream = new FileOutputStream(destination);
            outputStream.write(bytes.toByteArray());
            outputStream.close();

        } catch (Throwable err) {
            err.printStackTrace();
        }

        memoManager.bntPickerTake.setText(getString(R.string.delete_img));
        memoManager.imagePreview.setVisibility(View.VISIBLE);
        memoManager.imagePreview.setImageBitmap(bitmap);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null)
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getApplicationContext().getContentResolver(), data.getData());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                FileUtils.makeDirectory(new File(App.appDirectory));
                File destination = new File(App.appDirectory, memoManager.transaction.uniqueId + ".jpg");
                FileOutputStream outputStream;

                try {
                    destination.createNewFile();
                    outputStream = new FileOutputStream(destination);
                    outputStream.write(bytes.toByteArray());
                    outputStream.close();

                } catch (Throwable err) {
                    err.printStackTrace();
                }
            } catch (Throwable err) {
                err.printStackTrace();
            }

        memoManager.bntPickerTake.setText(getString(R.string.delete_img));
        memoManager.imagePreview.setVisibility(View.VISIBLE);
        memoManager.imagePreview.setImageBitmap(bitmap);
    }

}
