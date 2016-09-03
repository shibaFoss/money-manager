package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

public class FontUtility {

    public static final String Roboto_Regular = "fonts/Roboto-Regular.ttf";
    public static final String Roboto_Medium = "fonts/Roboto-Medium.ttf";
    public static final String Roboto_Light = "fonts/Roboto-Light.ttf";

    public static Typeface RobotoRegular;
    public static Typeface RobotoLight;
    public static Typeface RobotoMedium;


    public static void initialize(Context context) {
        RobotoRegular = Typeface.createFromAsset(context.getAssets(), Roboto_Regular);
        RobotoLight = Typeface.createFromAsset(context.getAssets(), Roboto_Light);
        RobotoMedium = Typeface.createFromAsset(context.getAssets(), Roboto_Medium);
    }


    public static void setFontFromAssetManager(String fontName, TextView textView) {
        AssetManager assetManager = textView.getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, fontName);
        textView.setTypeface(font);
    }


    public static void setFont(Typeface font, Activity activity, @IdRes int... resIds) {
        for (int id : resIds) {
            View view = activity.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }
    }


    public static void setFont(Typeface font, View mainView, @IdRes int... resIds) {
        for (int id : resIds) {
            View view = mainView.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }
    }

}
