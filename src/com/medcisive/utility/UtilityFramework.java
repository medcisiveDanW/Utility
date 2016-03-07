package com.medcisive.utility;

import java.io.FileNotFoundException;

/**
 *
 * @author vhapalchambj
 */
public class UtilityFramework {
    public static PropertiesUtility _properties;

    public UtilityFramework() {}

    public static PropertiesUtility init2(String init) {
        try {
            _properties = PropertiesUtility.create("FRAMEWORK").initialize(init);
        } catch (Exception ex) { System.out.println("Failed to start UtilityFramework: " + ex); }
        setupDBC();
        return _properties;
    }
    
    public static PropertiesUtility init2(java.io.File file) {
        try {
            _properties = PropertiesUtility.create("FRAMEWORK").initialize(file);
        } catch (Exception ex) { System.out.println("Failed to start UtilityFramework: " + ex); }
        setupDBC();
        return _properties;
    }
    
    
    private static void setupDBC() {
        if(_properties!=null) {
            com.medcisive.utility.sql2.SQL sql = com.medcisive.utility.sql2.SQL.getSingleton();
            int port = _getPort("DEST_DB_PORT");
            if(_properties.getProperty( "DEST_DB_SERVER" )!=null && port>0) {
                sql.createDataSource(_properties.getProperty( "DEST_DB_SERVER" ), port, _properties.getProperty( "DEST_DB" ), _properties.getProperty( "DEST_DB_USERNAME" ), _properties.getProperty( "DEST_DB_PASSWORD" ));
            }
            port = _getPort("SOURCE_DB_PORT");
            if(_properties.getProperty( "SOURCE_DB_SERVER" )!=null && port>0) {
                sql.createDataSource(_properties.getProperty( "SOURCE_DB_SERVER" ), port, _properties.getProperty( "SOURCE_DB" ), _properties.getProperty( "SOURCE_DB_USERNAME" ), _properties.getProperty( "SOURCE_DB_PASSWORD" ));
            }
        }
    }

    private static int _getPort(String portName) {
        Integer result = -1;
        PropertiesUtility prop;
        try {
            prop = PropertiesUtility.get("FRAMEWORK");
        } catch (Exception ex) { return -1; }
        String port = prop.getProperty(portName);
        if(port==null) {
            return -1;
        } else {
            try {
                result = Integer.parseInt(port);
            } catch (java.lang.NumberFormatException e) {
                LogUtility.error("Bad Port Number: (" + e + ")");
            }
        }
        return result;
    }
}