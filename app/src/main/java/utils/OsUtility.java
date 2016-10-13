package utils;

import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OsUtility {

    public static int getBuildSdk() {
        return Build.VERSION.SDK_INT;
    }


    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(calendar.getTime());
    }


    public static String getFullMonthName(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }

        return null;
    }
}
