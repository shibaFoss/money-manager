package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;

import gui.BaseActivity;

public class DialogUtility {

    public static MaterialDialog.Builder getDefaultBuilder(BaseActivity activity) {
        return new MaterialDialog.Builder(activity)
                .contentColor(getThemeColor(activity, android.R.attr.textColorPrimary))
                .positiveColor(getThemeColor(activity, android.R.attr.textColorPrimary))
                .negativeColor(getThemeColor(activity, android.R.attr.textColorSecondary))
                .neutralColor(getThemeColor(activity, android.R.attr.textColorSecondary));
    }


    @ColorInt
    private static int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, value, true);
        return value.data;
    }


    public static Dialog generateNewDialog(Activity activity, int layout) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(layout);
        fillParent(dialog);

        return dialog;
    }


    private static void fillParent(Dialog dialog) {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
    }

}
