package gui.launcher;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import gui.account_create.NewAccountCreateActivity;
import in.mobi_space.money_manager.R;
import utils.DialogUtility;

class InitialDialog {
    static final int FILE_SELECT_CODE = 0;
    private LauncherActivity activity;

    InitialDialog(final LauncherActivity activity) {
        this.activity = activity;
        DialogUtility.getDefaultBuilder(activity)
                .title(R.string.pic_a_choice)
                .items(activity.getString(R.string.i_am_new_user),
                        activity.getString(R.string.restore_data_from_sdcard),
                        "Help me")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            Intent intent = new Intent(activity, NewAccountCreateActivity.class);
                            intent.putExtra("isLauncherFired", true);
                            activity.startActivity(intent);
                            activity.finish();

                        } else if (which == 1) {
                            showFileChooser();

                        } else if (which == 2) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","shiba4email@gmail.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help Support from Money Manager");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                            activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            activity.finish();
                        }
                    }
                })
                .build().show();
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(Intent.createChooser(intent,
                    activity.getString(R.string.select_file_to_upload)), FILE_SELECT_CODE);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, activity.getString(R.string.please_install_a_file_mager), Toast.LENGTH_SHORT).show();
        }
    }
}
