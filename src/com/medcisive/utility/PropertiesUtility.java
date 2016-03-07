package com.medcisive.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author vhapalchambj
 */
public class PropertiesUtility {
    protected static java.util.Map<String,PropertiesUtility> _instances = new java.util.HashMap();
    protected Properties    _properties = null;
    private   InputStream   _stream = null;
    private   String        _filePath = null;

    private PropertiesUtility() {}

    public static PropertiesUtility get(String name) throws java.lang.Exception {
        if(name==null) {
            throw new java.lang.Exception("PropertiesUtility Exception: " + name + " dose not exsist.");
        } else if (_instances.get(name)==null) {
            return PropertiesUtility.create(name);
        }
        return _instances.get(name);
    }

    public static PropertiesUtility create(String name) throws java.lang.Exception {
        PropertiesUtility instance = null;
        if(name==null) {
            throw new java.lang.Exception("PropertiesUtility Exception: " + name + " is not a valid property name.");
        } else if (_instances.get(name)==null) {
            instance = new PropertiesUtility();
            _instances.put(name, instance);
        } else {
            instance = _instances.get(name);
        }
        return instance;
    }

    public PropertiesUtility initialize(String file) throws java.io.FileNotFoundException, java.io.IOException {
        if (file == null) { throw new java.io.FileNotFoundException(); }
        _filePath = file;
        _stream = new FileInputStream(file);
        return _initialize();
    }

    public PropertiesUtility initialize(java.io.File file) throws java.io.FileNotFoundException, java.io.IOException {
        if (file == null) { throw new java.io.FileNotFoundException(); }
        _filePath = file.getAbsolutePath();
        _stream = new FileInputStream(file);
        return _initialize();
    }

    private PropertiesUtility _initialize() throws java.io.IOException {
        if(_properties==null) { _properties = new Properties(); }
        if(_stream!=null) {
            _properties.load(_stream);
            _stream.close();
            _stream = null;
        }
        if(LogUtility._properties==null) {
            LogUtility._properties = _properties;
        }
        return this;
    }

    public PropertiesUtility refresh() {
        try {
            if(_filePath!=null) {
                initialize(_filePath);
            }
        } catch ( java.io.IOException e) { System.err.println(e); }
        return this;
    }

    public String getProperty(String name) {
        if (name == null || _properties == null) {
            return null;
        }
        return _properties.getProperty(name);
    }

    public void setProperty(String key, String value) {
        _properties.setProperty(key, value);
        if(_filePath!=null || _filePath.isEmpty()) {
            try {
                _properties.store(new java.io.FileOutputStream(_filePath), "");
            } catch (IOException ex) {
                LogUtility.error(ex);
            }
        }
    }

    public boolean runThisFunction() {
        String name = Thread.currentThread().getStackTrace()[2].getMethodName();
        String result = this.getProperty(name);
        if(result==null) { return true; }
        return result.equalsIgnoreCase("true");
    }

    public void print() {
        if (_properties == null) {
            System.out.println("Error: print()\nPropertiesUtility not configured properly");
        } else {
            List<String> propList = Collections.list((Enumeration<String>) _properties.propertyNames());
            for (String s : propList) {
                System.out.println(s + ": " + this.getProperty(s));
            }
        }
    }

    public java.util.Map<String,String> getContiansKeyMap(String charset) {
        java.util.Map<String,String> result = new java.util.HashMap();
        if (_properties == null) {
            System.out.println("Error: getContiansKeyMap(" + charset + ")\nPropertiesUtility not configured properly");
        } else {
            List<String> propList = Collections.list((Enumeration<String>) _properties.propertyNames());
            for (String s : propList) {
                if(s.contains(charset)){
                    result.put(s, this.getProperty(s));
                }
            }
        }
        return result;
    }

    public static void _printStream(java.io.InputStream inputStream) {
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
        String line = null;
        try {
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) { System.out.println("printStream error: " + e); }
    }

    public static int _parseInt(String s) {
        int i = -1;
        if (s != null) {
            try {
                i = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.err.println("Parse Int Error: " + e);
                i = -1;
            }
        }
        return i;
    }
}
