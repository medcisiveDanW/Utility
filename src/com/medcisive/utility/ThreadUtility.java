package com.medcisive.utility;

/**
 *
 * @author vhapalchambj
 */
public class ThreadUtility extends Thread {

    private int _delay = 1000;
    private boolean _isRunning = true;
    private int _timeout;
    private boolean _isTimeout = false;
    private PropertiesUtility _pu;

    public ThreadUtility(String name) {
        try {
            _pu = PropertiesUtility.get(name);
            _timeout = PropertiesUtility._parseInt(_pu.getProperty("TIME_OUT"));
        }
        catch (java.lang.Exception e) {_timeout=-1; _pu = null; System.err.println("ThreadUtility: " + e);}
        if (_timeout > 0) {
            _isTimeout = true;
        }
    }

    @Override
    public void run() {
        while (_isRunning) {
            if (_isRemoteShutdown()) {
                System.exit(0);
            }
            if (_isTimeout && (_timeout < 0)) {
                _timeoutException();
                System.exit(0);
            }
            if (_timeout > -1) {
                _timeout--;
            }
            _snooze();
        }
    }

    public void kill() {
        _isRunning = false;
    }

    protected void _timeoutException() {}

    private void _snooze() {
        try {
            Thread.sleep(_delay);
        } catch (InterruptedException e) {}
    }

    private boolean _isRemoteShutdown() {
        boolean result = false;
        if(_pu!=null) {
            _pu.refresh();
            String remoteShutdown = (String) _pu.getProperty("REMOTE_SHUTDOWN");
            if (remoteShutdown != null) {
                if (remoteShutdown.equalsIgnoreCase("true")) {
                    result = true;
                    System.out.println("Remote shutdown called.");
                }
            }
        }
        return result;
    }
}
