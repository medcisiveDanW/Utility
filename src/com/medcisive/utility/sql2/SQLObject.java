package com.medcisive.utility.sql2;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vhapalchambj
 */
public abstract class SQLObject {
    public void pre(ResultSet rs) throws SQLException{};
    public abstract void row(ResultSet rs) throws SQLException;
    public void post(){};
}
