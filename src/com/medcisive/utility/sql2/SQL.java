package com.medcisive.utility.sql2;

/**
 *
 * @author vhapalchambj
 */
public class SQL {

    private final java.util.Map<String, DataSource> _dsMap = new java.util.HashMap();
    private static SQL _instance;

    private SQL() {
    }

    public static SQL getSingleton() {
        if (_instance == null) {
            _instance = new SQL();
        }
        return _instance;
    }

    public void createDataSource(String server, int port, String database) {
        if(_dsMap.get(database)!=null) { return; } // dont duplicate
        DataSource result = DataSource.init(server, port, database,"","");
        _dsMap.put(database, result);
    }

    public void createDataSource(String server, int port, String database, String username, String password) {
        if(_dsMap.get(database)!=null) { return; } // dont duplicate
        DataSource result = DataSource.init(server, port, database, username, password);
        _dsMap.put(database, result);
    }

    public DBC getDBC(String database) {
        DBC result = null;
        if (_dsMap.get(database) != null) {
            result = new DBC(_dsMap.get(database));
        }
        return result;
    }
}
