package com.medcisive.utility.sql2;

import com.medcisive.utility.LogUtility;
import java.sql.*;

/**
 *
 * @author vhapalchambj
 */
public class DBC {
    private final DataSource _ds;
    private Connection _con;
    private ResultSet _rs;
    private Statement _statement;
    private DBCException _DBCException;

    public DBC(DBC dbc) {
        _ds = dbc._ds;
    }
    
    public DBC(DataSource ds) {
        _ds = ds;
    }

    public synchronized void query(String query, SQLObject obj) {
        if(query==null || query.isEmpty()) { throw new java.lang.NullPointerException("Error in DBC: query() - query is empty."); }
        if(obj==null) { throw new java.lang.NullPointerException("Error in DBC: query() - SQLObject is null."); }
        try {
            open();
            _rs = _statement.executeQuery(query);
            obj.pre(_rs);
            while (_rs.next()) {
                obj.row(_rs);
            }
            obj.post();
        } catch (SQLException e) {
            System.out.println(e);
            _throwException(e,query);
        } finally {
            close();
        }
    }

    public synchronized int update(String query) {
        if(query==null || query.isEmpty()) { throw new java.lang.NullPointerException("Error in DBC: update() - query is empty."); }
        int result = -1;
        try {
            open();
            result = _statement.executeUpdate(query);
            _con.commit();
            if (result == 0) { result = _statement.getUpdateCount(); }
        } catch (java.sql.SQLException e) {
            try { _con.rollback(); } catch (java.sql.SQLException r) { LogUtility.error(r); }
            System.out.println(e);
            _throwException(e,query);
        } finally {
            close();
        }
        return result;
    }

    public synchronized SQLTable getTable(String query) {
        SQLTable result = null;
        if(query==null || query.isEmpty()) { throw new java.lang.NullPointerException("Error in DBC: getTable() - query is empty."); }
        try {
            open();
            result = new SQLTable(_statement.executeQuery(query));
        } catch (SQLException e) {
            _throwException(e,query);
        } finally {
            close();
        }
        return result;
    }
    
    public synchronized void _insertTable(String outputQuery, java.util.ArrayList<String> inputQueryArray) {
        String outputToLower = outputQuery.toLowerCase();
        String preQuery = null;
        String postQuery = null;
        int insertIndex = outputToLower.indexOf("insert ");
        postQuery = outputQuery.substring(insertIndex, outputQuery.length());
        if (insertIndex >= 0) {
            if (insertIndex > 0) {
                preQuery = outputQuery.substring(0, insertIndex);
            }
        }
        if (preQuery != null) {
            update(preQuery);
        }
        if (postQuery != null) {
            for (String result : inputQueryArray) {
                update(postQuery + " " + result);
            }
        }
    }

    @Override
    public synchronized DBC clone() {
        return new DBC(_ds);
    }

    private void open() throws SQLException {
        _con = _ds.getConnection();
        _con.setAutoCommit(false);
        _statement = _con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    private void close() {
        try {
            if (_rs != null) {
                _rs.close();
            }
            if (_statement != null) {
                _statement.close();
            }
            if (_con != null) {
                _con.close();
            }
        } catch (java.sql.SQLException e) {
            System.err.println("Error in DBC: close() - " + e.getMessage());
        }
    }
    
    public static String fixTimestamp(java.sql.Timestamp timestamp) {
        if (timestamp != null) {
            return "'" + timestamp + "'";
        }
        return null;
    }

    public static String fixString(String str) {
        if (str != null) {
            return "'" + fixApostropheForSQLInsertion(str) + "'";
        }
        return null;
    }

    private static String fixApostropheForSQLInsertion(String str) {
        int index = str.indexOf('\'');
        String temp = str;
        if (index >= 0) {
            String before = str.substring(0, index+1) + "'";
            String after = str.substring(index + 1);
            temp = before + fixApostropheForSQLInsertion(after);
        }
        return temp;
    }
    
    public static Object fix(Object o) {
        if(o==null) {
            return null;
        } else if(o.getClass().equals(String.class)) {
            return fixString((String)o);
        } else if(o.getClass().equals(java.sql.Timestamp.class)) {
            return fixTimestamp((java.sql.Timestamp)o);
        } else {
            return o;
        }
    }
    
    public static String javaListToSQLList(java.util.List list) {
        String result = "('";
        for(Object o : list) {
            result += o + "','";
        }
        result = result.substring(0,result.lastIndexOf("','"));
        result += "')";
        return result;
    }
    
    public void setException(DBCException DBCException) {
        _DBCException = DBCException;
    }
    
    private void _throwException(SQLException e, String query) {
        if(_DBCException!=null) {
            _DBCException.exception(e,query);
        }
    }
}
