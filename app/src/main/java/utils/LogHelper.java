package utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import in.mobi_space.money_manager.BuildConfig;

/**
 * <p>LogHelper is a utility class that helps you to print a log message
 * without writing the tag name again & again. You can use the class with
 * the following way.
 * <br><br/>
 * <code>
 * LogHelper LOG = LogHelper.from(SomeObject.class);<br>
 * LOG.i("LogHelper is awesome.");
 * </code>
 * </p>
 */
public final class LogHelper {

    private Class class_;
    private boolean isDebuggingMode;


    private LogHelper(Class<?> class_) {
        this.class_ = class_;
        this.isDebuggingMode = BuildConfig.DEBUG;
    }


    public static LogHelper from(Class<?> class_) {
        return new LogHelper(class_);
    }


    /**
     * The method returns the entire stack list of the given throwable
     * error in a String form.
     *
     * @param throwable the error to be processed.
     */
    public static String toString(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }


    public void e(String message) {
        if (isDebuggingMode)
            Log.e(class_.getSimpleName(), toMessage(message));
    }


    public void d(String message) {
        if (isDebuggingMode)
            Log.d(class_.getSimpleName(), toMessage(message));
    }


    public void i(String message) {
        if (isDebuggingMode)
            Log.i(class_.getSimpleName(), toMessage(message));
    }


    private String toMessage(String message) {
        return message == null ? "Error Message = NULL!!" : message;
    }
}
