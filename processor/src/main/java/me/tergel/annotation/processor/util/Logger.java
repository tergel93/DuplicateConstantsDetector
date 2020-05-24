package me.tergel.annotation.processor.util;


public class Logger {
    private Logger() {
        //no instance
    }

    private static final Logger sLogger = new Logger();
    private static final String TAG = "[UniqueString]:";

    private static Logger get() {
        return sLogger;
    }

    public static void log(String content) {
        get().print(content);
    }

    private void print(String content) {
        System.out.println(TAG + content);
    }
}

