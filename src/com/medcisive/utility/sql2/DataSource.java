package com.medcisive.utility.sql2;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

/**
 *
 * @author vhapalchambj
 */
public class DataSource extends SQLServerDataSource {

    private DataSource(String server, int port, String database) {
        setIntegratedSecurity(true);
        setServerName(server);
        setPortNumber(port);// 1396 local port and RDW is default 1433
        setDatabaseName(database);
    }

    private DataSource(String server, int port, String database, String username, String password) {
        setIntegratedSecurity(false);
        setServerName(server);
        setPortNumber(port);// 1396 local port and RDW is default 1433
        setDatabaseName(database);
        setUser(username);
        setPassword(password);
    }

    public static DataSource init(String server, int port, String database, String username, String password) {
        System.out.println("Created DataSource: Server(" + server + ") Port(" + port + ") Database(" + database + ") Username(" + username + ") Password(" + password + ")");
        DataSource result = null;
        if(username==null || username.isEmpty() || password==null || password.isEmpty()) {
            result = new DataSource(server, port, database);
        } else {
            result = new DataSource(server, port, database, username, password);
        }
        return result;
    }
}
