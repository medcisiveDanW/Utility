package com.medcisive.utility.sql2;

import java.sql.SQLException;

/**
 *
 * @author vhapalchambj
 */
public class DBCException extends DBC {
    
    public DBCException(DBC dbc) {
        super(dbc);
    }
    
    public void exception(SQLException e, String query){}
}
