package utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;

import gui.BaseActivity;
import in.softc.aladindm.R;

public class DialogUtility {

    public static MaterialDialog.Builder getDefaultBuilder(BaseActivity activity) {
        return new MaterialDialog.Builder(activity)
                .contentColor(activity.getColorFrom(R.color.black_text))
                .positiveColor(activity.getColorFrom(R.color.black_text))
                .negativeColor(activity.getColorFrom(R.color.black_text_secondary))
                .neutralColor(activity.getColorFrom(R.color.black_text_secondary));
    }


    public static Dialog generateNewDialog(Activity activity, int layout) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(layout);
        fillParent(dialog);

        return dialog;
    }


    public static void fillParent(Dialog dialog) {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
    }

}
