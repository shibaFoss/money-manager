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

    private static final String Lato_Regular = "fonts/Lato-Regular.ttf";
    private static final String Lato_Medium = "fonts/Lato-Medium.ttf";
    private static final String Lato_Light = "fonts/Lato-Light.ttf";
    private static final String OpenSans_Regular = "fonts/OpenSans-Regular.ttf";
    private static final String Toolbar_Regular = "fonts/toolbar_font.ttf";

    public static Typeface LatoRegular;
    public static Typeface LatoMedium;
    public static Typeface LatoLight;
    public static Typeface ToolbarRegular;
    public static Typeface OpenSansRegular;

    public static void init(Context context) {
        OpenSansRegular = Typeface.createFromAsset(context.getAssets(), OpenSans_Regular);
        LatoRegular = Typeface.createFromAsset(context.getAssets(), Lato_Regular);
        LatoMedium = Typeface.createFromAsset(context.getAssets(), Lato_Medium);
        LatoLight = Typeface.createFromAsset(context.getAssets(), Lato_Light);
        ToolbarRegular = Typeface.createFromAsset(context.getAssets(), Toolbar_Regular);
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
