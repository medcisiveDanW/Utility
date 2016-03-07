package com.medcisive.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

/**
 *
 * @author vhapalchambj
 */
public class LogUtility {
    public static java.util.Properties _properties = null;
    private static LogUtility _instance;
    private SimpleDateFormat _formatter = new SimpleDateFormat("yyyy-MMM-dd");
    private Date _today = new Date();
    private String _mainClassName = "UnknownMain";

    private LogUtility() {
        if(_properties!=null) {
            org.apache.log4j.PropertyConfigurator.configure(_properties);
        }
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            _initializeDefault();
        }
    }

    private static LogUtility _getSinglton() { // This could be public but decided to hide.
        if(_instance==null) {
            _instance = new LogUtility();
        }
        return _instance;
    }

    public static void fatal(Object message) {
        LogUtility._getSinglton()._getLogger().fatal(message);
    }

    public static void error(Object message) {
        LogUtility._getSinglton()._getLogger().error(message);
    }

    public static void warn(Object message) {
        LogUtility._getSinglton()._getLogger().warn(message);
    }

    public static boolean isInfoEnabled() { return LogUtility._getSinglton()._getLogger().isInfoEnabled(); }

    public static void info(Object message) {
        LogUtility._getSinglton()._getLogger().info(message);
    }

    public static boolean isDebugEnabled() { return LogUtility._getSinglton()._getLogger().isDebugEnabled(); }

    public static void debug(Object message) {
        LogUtility._getSinglton()._getLogger().debug(message);
    }

    public static boolean isTraceEnabled() { return LogUtility._getSinglton()._getLogger().isTraceEnabled(); }

    public static void trace(Object message) {
        LogUtility._getSinglton()._getLogger().trace(message);
    }

    private Logger _getLogger() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        return Logger.getLogger(className);
    }

    private void _initializeDefault() {
        Logger root = Logger.getRootLogger();
        FileAppender fa = new FileAppender();
        fa.setName("FileLogger");
        _getMainClassName();
        String filename = _mainClassName + "Logs\\" + _formatter.format(_today) + "." + _mainClassName + ".log";
        String workingDir = System.getProperty("user.dir");
        System.out.println("Initialize logging folder in " + workingDir + " : " + filename);
        fa.setThreshold(Priority.WARN);
        fa.setFile(workingDir+ "\\" + filename);
        fa.setLayout(new PatternLayout("%d %-5p %c %M %m%n"));
        fa.setAppend(true);
        fa.activateOptions();
        root.addAppender(fa);
    }

    private void _getMainClassName() {
        java.util.Map<Thread,StackTraceElement[]> m = Thread.getAllStackTraces();
        for(Thread t : m.keySet()) {
            StackTraceElement[] stack = m.get(t);
            for(StackTraceElement main : stack) {
                String temp = main.toString();
                if(temp.contains(".main")) {
                    String name = main.getClassName();
                    int index = name.lastIndexOf('.');
                    if(index>1) { _mainClassName = name.substring(index+1); }
                }
            }
        }
    }

    private void asdf(java.lang.Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        e.printStackTrace(new java.io.PrintWriter(sw));
        String stacktrace = sw.toString();
    }
}