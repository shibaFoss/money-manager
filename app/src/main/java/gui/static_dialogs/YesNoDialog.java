package gui.static_dialogs;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import gui.BaseActivity;
import utils.DialogUtility;

public abstract class YesNoDialog {

    public BaseActivity baseActivity;
    public MaterialDialog dialog;


    public YesNoDialog(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }


    public abstract void onYes(Dialog dialog);

    public abstract void onNo(Dialog dialog);


    public void show(String yesButton, String noButton, String message) {
        this.dialog = DialogUtility.getDefaultBuilder(baseActivity)
                .content(message)
                .positiveText(yesButton)
                .negativeText(noButton)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        onYes(dialog);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        onNo(dialog);
                    }
                }).build();

        this.dialog.show();
    }


    public void show(@StringRes int yes, @StringRes int no, @StringRes int message) {
        show(baseActivity.getString(yes), baseActivity.getString(no), baseActivity.getString(message));
    }
}
