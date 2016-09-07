package gui.transaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import accounts.Transaction;
import core.App;
import gui.BaseActivity;
import gui.static_dialogs.YesNoDialog;
import in.softc.aladindm.R;
import utils.DialogUtility;

public class TransactIonMemoManager implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE_REQUEST = 2;

    public TransactionActivity activity;
    public Transaction transaction;
    public ImageView imagePreview;
    public TextView bntPickerTake;

    public TransactIonMemoManager(TransactionActivity activity, Transaction transaction) {
        this.activity = activity;
        this.transaction = transaction;
        imagePreview = (ImageView) activity.findViewById(R.id.img_memo_photo);
        bntPickerTake = (TextView) activity.findViewById(R.id.bnt_memo_photo_taker);
        bntPickerTake.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bnt_memo_photo_taker) {
            if (App.isPermissionGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (bntPickerTake.getText().toString().equals(activity.getString(R.string.delete_img))) {
                    File imageFile = new File(App.appDirectory, transaction.id + ".jpg");
                    if (imageFile.exists())
                        imageFile.delete();
                    bntPickerTake.setText(activity.getString(R.string.take_a_photo));

                } else {
                    DialogUtility.getDefaultBuilder(activity)
                            .title(R.string.choose)
                            .items(new String[]{activity.getString(R.string.camera), activity.getString(R.string.gallery)})
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    if (which == 0) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        activity.startActivityForResult(intent, REQUEST_CAMERA);
                                    } else if (which == 1) {
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        activity.startActivityForResult(
                                                Intent.createChooser(intent, activity.getString(R.string.select_file)),
                                                SELECT_FILE_REQUEST);
                                    }
                                }
                            }).show();
                }

                imagePreview.setImageBitmap(null);
                imagePreview.setVisibility(View.GONE);
            } else {
                askForSdcardWritingPermission();
            }
        }
    }

    private void askForSdcardWritingPermission() {
        YesNoDialog yesNoDialog = new YesNoDialog(activity) {

            @Override
            public void onYes(Dialog dialog) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(activity,
                        App.REQUIRED_PERMISSIONS,
                        BaseActivity.USES_PERMISSIONS_REQUEST_CODE);
            }

            @Override
            public void onNo(Dialog dialog) {
                dialog.dismiss();
            }
        };

        yesNoDialog.show(
                R.string.grant_permission,
                R.string.cancel,
                R.string.app_require_write_permissions);
    }
}
