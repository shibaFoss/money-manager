package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

/**
 * Font : The class is a utility class, and offers some useful functions for applying
 * custom fonts to text views.
 */
public class Font {

    private static final String Roboto_Light = "fonts/RobotoCondensed-Light.ttf";
    private static final String Roboto_Regular = "fonts/RobotoCondensed-Regular.ttf";
    private static final String Roboto_Italic = "fonts/RobotoCondensed-Italic.ttf";

    public static Typeface RobotoRegular;
    public static Typeface RobotoLight;
    public static Typeface RobotoRegularItalic;

    public static void init(Context context) {
        RobotoRegularItalic = Typeface.createFromAsset(context.getAssets(), Roboto_Italic);
        RobotoRegular = Typeface.createFromAsset(context.getAssets(), Roboto_Regular);
        RobotoLight = Typeface.createFromAsset(context.getAssets(), Roboto_Light);
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
