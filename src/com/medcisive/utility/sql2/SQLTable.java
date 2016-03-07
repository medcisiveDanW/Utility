package com.medcisive.utility.sql2;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 *
 * @author vhapalchambj
 */
public class SQLTable {

    private java.util.LinkedHashMap<Integer, java.util.LinkedHashMap> _table;
    private transient java.util.LinkedHashMap<Integer, java.util.LinkedHashMap> _addtable;
    private transient java.util.LinkedHashMap<Integer, java.util.LinkedHashMap> _removetable;
    protected transient java.util.ArrayList<String> _columnNameList;
    private transient java.util.ArrayList<String> _columnNameListToLower;
    private transient java.util.LinkedHashMap<String, String> _classNameList;
    private transient Integer _index = 0;
    private transient Integer _jumpIndex = 3000;

    /**
     *
     */
    public SQLTable() {
        _table = new java.util.LinkedHashMap();
        _addtable = new java.util.LinkedHashMap();
        _removetable = new java.util.LinkedHashMap();
        _columnNameList = new java.util.ArrayList();
        _columnNameListToLower = new java.util.ArrayList();
        _classNameList = new java.util.LinkedHashMap();
    }

    /**
     *
     * @param rs
     */
    public SQLTable(ResultSet rs) {
        this();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            for (int index = 1; index <= numberOfColumns; index++) {
                String name = rsmd.getColumnName(index);
                String nameToLower = name.toLowerCase();
                _columnNameListToLower.add(nameToLower);
                _columnNameList.add(name);
                _classNameList.put(name, rsmd.getColumnClassName(index));
                //System.out.println("Name: " + name + " Class: " + rsmd.getColumnClassName(index) + " Type: " + rsmd.getColumnTypeName(index));
            }
            while (rs.next()) {
                java.util.LinkedHashMap<String, Object> row = new java.util.LinkedHashMap();
                for (String columnName : _columnNameList) {
                    if (_classNameList.get(columnName).equalsIgnoreCase("java.lang.Integer")) {
                        row.put(columnName, rs.getInt(columnName));
                    } else if (_classNameList.get(columnName).equalsIgnoreCase("java.lang.Short")) {
                        row.put(columnName, rs.getInt(columnName));
                    } else if (_classNameList.get(columnName).equalsIgnoreCase("java.lang.Double")) {
                        row.put(columnName, rs.getDouble(columnName));
                    } else if (_classNameList.get(columnName).equalsIgnoreCase("java.sql.Timestamp")) {
                        row.put(columnName, rs.getTimestamp(columnName));
                    } else if (_classNameList.get(columnName).equalsIgnoreCase("java.sql.Date")) {
                        row.put(columnName, rs.getTimestamp(columnName));
                    } else if (_classNameList.get(columnName).equalsIgnoreCase("java.lang.String")) {
                        row.put(columnName, rs.getString(columnName));
                    }
                }
                _table.put(_index, row);
                _index++;
            }
        } catch (java.sql.SQLException e) {
            System.err.println("TableMap could not be created: " + e);
        }
    }

    /**
     *
     * @param cloneIndex
     * @return java.util.LinkedHashMap<String,Object>
     */
    public java.util.LinkedHashMap<String, Object> cloneRow(Integer cloneIndex) {
        java.util.LinkedHashMap<String, Object> clone = new java.util.LinkedHashMap();
        java.util.LinkedHashMap<String, Object> row = _table.get(cloneIndex);
        for (String key : row.keySet()) {
            clone.put(key, row.get(key));
        }
        _addtable.put(_index, clone);
        _index++;
        return clone;
    }

    /**
     *
     */
    public void applyChanges() {
        _table.putAll(_addtable);
        _addtable.clear();
        for (Integer i : _removetable.keySet()) {
            _table.remove(i);
        }
        _removetable.clear();
    }

    /**
     *
     */
    public void clearChanges() {
        _addtable.clear();
        _removetable.clear();
    }

    /**
     *
     * @param row
     */
    public void addRow(java.util.LinkedHashMap<String, Object> row) {
        _addtable.put(_index, row);
        _index++;
    }

    /**
     *
     * @param index
     */
    public void removeRow(Integer index) {
        _removetable.put(index, _table.get(index));
    }

    /**
     *
     * @param index
     * @return java.util.LinkedHashMap<String,Object>
     */
    public java.util.LinkedHashMap<String, Object> getRow(Integer index) {
        return _table.get(index);
    }

    /**
     *
     * @return java.util.Set<Integer>
     */
    public java.util.Set<Integer> keySet() {
        return _table.keySet();
    }

    /**
     *
     * @return java.util.Collection<LinkedHashMap>
     */
    public java.util.Collection<LinkedHashMap> values() {
        return _table.values();
    }

    /**
     *
     * @return java.util.ArrayList<String>
     */
    public java.util.ArrayList<String> getMappedResult() {
        java.util.ArrayList<String> unionArray = new java.util.ArrayList();
        for (java.util.LinkedHashMap<String, Object> row : _table.values()) {
            String localResult = "SELECT ";
            for (String key : row.keySet()) {
                Object o = row.get(key);
                localResult += DBC.fix(o) + ",";
            }
            localResult = localResult.substring(0, localResult.length() - 1) + " \n";
            localResult += "UNION ALL \n";
            unionArray.add(localResult);
        }
        return computeUnionSubsections(unionArray);
    }

    public Object getTable() {
        return _table;
    }

    /**
     *
     * @param list
     * @return java.util.ArrayList<String>
     */
    public java.util.ArrayList<String> getUnmappedResult(java.util.ArrayList<String> list) {
        java.util.ArrayList<String> toLower = new java.util.ArrayList();
        java.util.ArrayList<String> unionArray = new java.util.ArrayList();

        for (String s : list) {
            String elem = s.toLowerCase();
            toLower.add(elem);
        }
        for (java.util.LinkedHashMap<String, Object> row : _table.values()) {
            String localResult = "SELECT ";
            for (String key : toLower) {
                int index = _columnNameListToLower.indexOf(key);
                Object o = row.get(_columnNameList.get(index));
                localResult += DBC.fix(o) + ",";
            }
            localResult = localResult.substring(0, localResult.length() - 1) + " \n";
            localResult += "UNION ALL \n";
            unionArray.add(localResult);
        }
        return computeUnionSubsections(unionArray);
    }

    /**
     *
     * @param unionArray
     * @return java.util.ArrayList<String>
     */
    public java.util.ArrayList<String> computeUnionSubsections(java.util.ArrayList<String> unionArray) {
        String subStringResult = "";
        int jumpCounter = 0;
        java.util.ArrayList<String> builderList = new java.util.ArrayList();
        java.util.ArrayList<String> result = new java.util.ArrayList();
        for (String s : unionArray) { // condense packets size
            subStringResult += s;
            if (jumpCounter > _jumpIndex) {
                builderList.add(subStringResult);
                subStringResult = "";
                jumpCounter = 0;
            }
            jumpCounter++;
        }
        builderList.add(subStringResult);
        for (String s : builderList) { // remove "UNION ALL\n" from the end of each subset
            String subListUnion = s;
            if (subListUnion.length() > 12) {
                subListUnion = subListUnion.substring(0, subListUnion.length() - 12);
            }
            result.add(subListUnion);
        }
        return result;
    }

    public java.util.Collection<String> getColumn(String columnName) {
        java.util.Collection<String> result = new java.util.ArrayList();
        for (Integer key : this.keySet()) {
            java.util.LinkedHashMap<String, Object> map = this.getRow(key);
            Object o = map.get(columnName);
            if (o != null) {
                result.add(o.toString());
            }
        }
        return result;
    }

    /**
     *
     */
    public void print() {
        for (String columnName : _columnNameList) {
            System.out.print(columnName + " | ");
        }
        System.out.print("\n");
        for (java.util.LinkedHashMap<String, Object> row : _table.values()) {
            String result = "";
            for (String s : row.keySet()) {
                result += row.get(s) + " | ";
            }
            System.out.println(result);
        }
        System.out.println("Row count: " + _table.size());
    }

    /**
     *
     * @param str
     * @return String
     */
    public String returnFormattedInteger(String str) {
        int i = Integer.MIN_VALUE;
        String result = "";
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            i = Integer.MIN_VALUE;
        }
        if (i != Integer.MIN_VALUE) {
            result = "" + i;
        } else {
            result = null;
        }
        return result;
    }

    /**
     *
     * @return int
     */
    public int size() {
        return _index;
    }

    /**
     *
     * @param columnName
     * @param value
     * @return TableMap
     */
    public SQLTable columnEquals(String columnName, Object value) {
        SQLTable result = this.clone();
        for (Integer key : result.keySet()) {
            java.util.LinkedHashMap<String, Object> map = result.getRow(key);
            Object o = map.get(columnName);
            if ((o == null) || !o.equals(value)) {
                result.removeRow(key);
            }
        }
        if (result._removetable.size() == this.size()) {
            System.out.println("Value dose not exist for that columnName");
            return null;
        }
        result.applyChanges();
        return result;
    }

    /**
     *
     * @param columnName
     * @param isAscending
     */
    public void dispose() {
        _table = null;
        _addtable = null;
        _removetable = null;
        _columnNameList = null;
        _columnNameListToLower = null;
        _classNameList = null;
        System.gc();
    }

    public void sort(String columnName, boolean isAscending) {
        if (columnName == null) {
            return;
        }
        java.util.List list = new java.util.ArrayList();
        java.util.HashMap<Integer, java.util.LinkedHashMap<String, Object>> valueMap = new java.util.HashMap();
        java.util.HashMap<Object, java.util.ArrayList<Integer>> keyMap = new java.util.HashMap();
        for (Integer key : keySet()) {
            java.util.LinkedHashMap<String, Object> map = getRow(key);
            Object o = map.get(columnName);
            if (o == null) {
                return;
            }
            list.add(o);
            if (keyMap.get(o) == null) {
                java.util.ArrayList<Integer> keys = new java.util.ArrayList();
                keys.add(key);
                keyMap.put(o, keys);
            } else {
                java.util.ArrayList<Integer> keys = keyMap.get(o);
                keys.add(key);
            }
            valueMap.put(key, map);
            removeRow(key);
        }
        applyChanges();
        if (isAscending) {
            Collections.sort(list);
        } else {
            Collections.sort(list, Collections.reverseOrder());
        }
        java.util.LinkedHashMap<Object, Object> collapse = new java.util.LinkedHashMap();
        for (Object o : list) {
            collapse.put(o, o);
        }
        for (Object o : collapse.keySet()) {
            java.util.ArrayList<Integer> keys = keyMap.get(o);
            for (Integer key : keys) {
                addRow(valueMap.get(key));
            }
        }
        applyChanges();
    }

    public SQLTable clone() {
        SQLTable result = new SQLTable();
        result._table = new java.util.LinkedHashMap(_table);
        result._addtable = new java.util.LinkedHashMap(_addtable);
        result._removetable = new java.util.LinkedHashMap(_removetable);
        result._columnNameList = new java.util.ArrayList(_columnNameList);
        result._columnNameListToLower = new java.util.ArrayList(_columnNameListToLower);
        result._classNameList = new java.util.LinkedHashMap(_classNameList);
        return result;
    }
    
    public boolean isEmpty() {
        return _table.isEmpty();
    }
}
