package com.medcisive.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author vhapalchambj
 */
public class Timer {

    private long _start;
    private String _methodName;
    private static boolean _isShown = true;
    private static NumberFormat _numberFormat = new DecimalFormat("#0.00");

    private Timer() {
        _methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        _start = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Finished " + _methodName + ": " + _numberFormat.format(getDuration()) + " (seconds)";
    }

    public float getDuration() {
        return stop()/1000.0f;
    }
    
    public String getMethodName() {
        return _methodName;
    }

    public void print() {
        if(_isShown) { System.out.println(toString()); }
    }

    public void log() {
        print();
        LogUtility.info(toString());
    }

    public static void disable() {
        _isShown = false;
    }

    public static void enable() {
        _isShown = true;
    }

    public static Timer start() {
        Timer result = new Timer();
        if(_isShown) {
            System.out.println("Started " + result._methodName);
        }
        return result;
    }

    public long stop() {
        return System.currentTimeMillis() - _start;
    }

    public static String trace(StackTraceElement e[]) {
        String result = null;
        boolean doNext = false;
        for (StackTraceElement s : e) {
            if(_isShown) {
                System.out.println(s.getMethodName());
            }
            if (doNext) {
                result = s.getMethodName();
                break;
            }
            doNext = s.getMethodName().equals("getStackTrace");
        }
        return result;
    }
}