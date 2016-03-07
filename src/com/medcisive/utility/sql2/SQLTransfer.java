package com.medcisive.utility.sql2;

/**
 *
 * @author vhapalchambj
 */
public class SQLTransfer {
    protected com.medcisive.utility.sql2.DBC _dest;
    protected com.medcisive.utility.sql2.DBC _src;
    public SQLTransfer(com.medcisive.utility.sql2.DBC src, com.medcisive.utility.sql2.DBC dest){
        _src = src;
        _dest = dest;
    }
    
    public void execute(String iQuery, String oQuery) {
        if(_src==null || _dest==null || iQuery==null || oQuery==null) { System.out.println("SQLTransfer is not setup."); return; }
        com.medcisive.utility.sql2.SQLTable t = _src.getTable(iQuery);
        java.util.ArrayList<String> result;
        if (_isMapped(iQuery)) {
            result = t.getMappedResult();
        } else {
            result = t.getUnmappedResult(pullColumnNames(oQuery));
        }
        if (result != null) {
            _dest._insertTable(oQuery, result);
        }
    }
    
    private java.util.ArrayList<String> pullColumnNames(String query) {
        String nameList = pullNameList(query);
        return createNameArray(nameList);
    }

    private String pullNameList(String query) {
        String result = null;
        int index = query.indexOf('(');
        if ((index > -1) && (query.length() > (index + 1))) {
            query = query.substring(index + 1, query.length());
            index = query.indexOf(')');
            if (index > -1) {
                result = query.substring(0, index);
            }
        }
        return result;
    }
    
    private java.util.ArrayList<String> createNameArray(String names) {
        java.util.ArrayList<String> list = new java.util.ArrayList();
        int index = names.indexOf(',');
        if ((index > -1) && (names.length() > (index + 1))) {
            list.add(names.substring(0, index));
            names = names.substring(index + 1, names.length());
            list.addAll(createNameArray(names));
        } else if ((index == -1) && (names.length() > 0)) {
            list.add(names);
        }
        return list;
    }
    
    private boolean _isMapped(String str) {
        int index = str.indexOf(" * ");
        if (index < 0) {
            return true;
        }
        return false;
    }
}
