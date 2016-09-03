package gui.static_dialogs;

import android.support.annotation.NonNull;

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


    public abstract void onYes();


    public abstract void onNo();


    public void show(String yesButton, String noButton, String message) {
        this.dialog = DialogUtility.getDefaultBuilder(baseActivity)
                .content(message)
                .positiveText(yesButton)
                .negativeText(noButton)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        onYes();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        onNo();
                    }
                }).build();

        this.dialog.show();
    }

}
