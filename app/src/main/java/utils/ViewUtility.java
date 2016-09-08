package utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

public class ViewUtility {

    /**
     * Remove the drawable from all of the children views of the given view. That's useful for free up
     * lots of memory.
     */
    public static void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null)
                view.getBackground().setCallback(null);

            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(null);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    unbindDrawables(viewGroup.getChildAt(i));

                if (!(view instanceof AdapterView))
                    viewGroup.removeAllViews();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static void setViewOnClickListener(View.OnClickListener clickListener, Activity activity, @IdRes int... ids) {
        for (int id : ids) {
            activity.findViewById(id).setOnClickListener(clickListener);
        }
    }

    public static void setViewOnClickListener(View.OnClickListener clickListener, View view, @IdRes int... ids) {
        for (int id : ids) {
            view.findViewById(id).setOnClickListener(clickListener);
        }
    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static String makeRoundedValue(double number) {
        try {
            String result = "";
            String numberString = String.valueOf(number);
            if (numberString.contains(".")) {
                String numberDivider[] = numberString.split("\\.");

                result = numberDivider[0];
                if (numberDivider[1] != null) {
                    int value = Integer.parseInt(numberDivider[1]);
                    if (value > 1) {
                        if (value > 9)
                            result += String.valueOf("." + value);
                        else
                            result += String.valueOf(value) + "0";
                    }
                }
            }
            return result;
        } catch (Throwable err) {
            err.printStackTrace();
            return String.valueOf(number);
        }
    }
}
