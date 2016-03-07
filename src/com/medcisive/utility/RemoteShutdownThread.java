package com.medcisive.utility;

/**
 *
 * @author vhapalchambj
 */
public class RemoteShutdownThread extends Thread {
    private String _args;
    private int _delay = 20000;

    public RemoteShutdownThread(String[] args) {
        _args = args[0];
    }

    @Override
    public void run() {
        while (!isRemoteShutdown()) {
            try { Thread.sleep(_delay);
            } catch (InterruptedException e) {}
        }
        System.exit(0);
    }

    public boolean isRemoteShutdown() {
        PropertiesUtility prop = UtilityFramework.init2(_args);
        String remoteShutdown = prop.getProperty("REMOTE_SHUTDOWN");
        if (remoteShutdown.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }
}
