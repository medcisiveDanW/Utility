package com.medcisive.utility.sql2;

public class DBCUtil {
    public com.medcisive.utility.PropertiesUtility _properties;
    public com.medcisive.utility.sql2.SQL _sql;
    public com.medcisive.utility.sql2.DBC _dest = null;
    public com.medcisive.utility.sql2.DBC _src = null;

    public DBCUtil() {
        try {
            _properties = com.medcisive.utility.PropertiesUtility.get("FRAMEWORK");
            _sql = com.medcisive.utility.sql2.SQL.getSingleton();
            if(_properties.getProperty("DEST_DB")!=null && !_properties.getProperty("DEST_DB").isEmpty()) {
                _dest = _sql.getDBC(_properties.getProperty("DEST_DB"));
            }
            if(_properties.getProperty("SOURCE_DB")!=null && !_properties.getProperty("SOURCE_DB").isEmpty()) {
                _src = _sql.getDBC(_properties.getProperty("SOURCE_DB"));
            }
            if(_dest==null && _src!=null){
                _dest=_src;
            }
            if(_dest!=null && _src==null){
                _src=_dest;
            }
        } catch (java.lang.Exception e) {
            System.err.println(e);
        }
    }
}
