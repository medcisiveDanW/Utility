package com.medcisive.utility;

/**
 *
 * @author vhapalchambj
 */
public class ThreadCyclicManager {

    private java.util.ArrayList<Thread> _cycle = new java.util.ArrayList();
    public static int _triggerAt = 100;
    public boolean _print = true;
    private int _pumps = 0;
    private ThreadCyclicEvent _anonymous;

    public ThreadCyclicManager() {
    }

    public ThreadCyclicManager(ThreadCyclicEvent anonymous) {
        this();
        _anonymous = anonymous;
    }

    public void add(Thread t) {
        t.start();
        _cycle.add(t);
        process();
    }

    private void process() {
        if (_cycle.size() >= _triggerAt) {
            cycle();
        }
    }

    public void finalizeCycle() {
        if(_print) {
            System.out.println("Finalizing cycle...");
        }
        cycle();
    }

    private void cycle() {
        if(_print) {
            System.out.println("Pumping cycle(" + _pumps + ")...");
        }
        for (Thread t : _cycle) {
            try {
                t.join();
            } catch (java.lang.InterruptedException e) {
                System.err.println("Exception in SiteThreadCyclicManager: " + e);
            }
        }
        _cycle.clear();
        if(_anonymous!=null) {
            _anonymous.go();
        }
        _pumps++;
    }

    @Override
    protected void finalize() throws Throwable {
        cycle();
        super.finalize();
    }
}
