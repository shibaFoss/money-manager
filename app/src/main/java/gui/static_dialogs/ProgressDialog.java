package gui.static_dialogs;

import android.app.Dialog;

import com.afollestad.materialdialogs.MaterialDialog;

import gui.BaseActivity;
import libs.AsyncJob;
import utils.DialogUtility;

/**
 * This class very useful if you want to show a progressDialog.
 *
 * @author Shiba
 */
public final class ProgressDialog {

    private MaterialDialog dialog;


    public ProgressDialog(BaseActivity activity, boolean cancelable, String progressText) {
        dialog = DialogUtility.getDefaultBuilder(activity)
                .cancelable(cancelable)
                .content(progressText)
                .progress(true, 0)
                .build();
    }


    public void showInMainThread() {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                try {
                    dialog.show();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }


    public void closeInMainThread() {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                try {
                    dialog.dismiss();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }


    public Dialog getDialog() {
        return this.dialog;
    }
}
