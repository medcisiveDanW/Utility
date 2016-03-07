package com.medcisive.utility;

import com.google.gson.Gson;
/**
 *
 * @author vhapalchambj
 */
public class HashTree {

    private java.util.LinkedHashMap<Object, Object> _tree;
    private transient static Gson _gson = new Gson();
    private transient String _printTreeStr;
    private transient String _newlineCharacter = "\n";
    public  transient String resultSeparator = ",";
    public  transient String pathSeparator = "->";

    public HashTree() {
        _tree = new java.util.LinkedHashMap();
    }

    public HashTree(String strHash) {
        _tree = buildTree(strHash);
    }

    public void put(Object key, Object value) {
        String strHash = "[" + key + "=" + value + "]";
        java.util.LinkedHashMap<Object, Object> subTree = buildTree(strHash);
        if (subTree != null) {
            _tree = mergeTrees(_tree, subTree);
        }
    }

    public void add(String strHash) {
        java.util.LinkedHashMap<Object, Object> subTree = buildTree(strHash);
        if (subTree != null) {
            _tree = mergeTrees(_tree, subTree);
        }
    }

    public String get(String str) {
        java.util.ArrayList path = getPath(str);
        String value = "";
        java.util.LinkedHashMap<Object, Object> hash = this._tree;
        for (Object o : path) {
            Object cur = hash.get((String) o);
            if (cur == null) {
                //System.out.println("Invalid node structure: " + str);
                return null;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                hash = (java.util.LinkedHashMap) hash.get((String) o);
            } else if (cur.getClass().equals(String.class)) {
                value = (String) hash.get((String) o);
            } else if (cur.getClass().equals(java.util.ArrayList.class)) {
                value = (String) hash.get((String) o).toString();
            }
        }
        if (value.equalsIgnoreCase("")) {
            Object cur = hash;
            if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                for (Object o : hash.keySet()) {
                    value += (String) o + resultSeparator;
                }
                value = value.substring(0, value.lastIndexOf(resultSeparator));
            }
        }
        return value;
    }

    public String toJSON() {
        return _gson.toJson(this);
    }
    public java.util.ArrayList<String> getArray(String str) {
        if (str == null) {
            return null;
        }
        java.util.ArrayList<String> result = new java.util.ArrayList();
        java.util.ArrayList path = getPath(str);
        if (path == null) {
            return null;
        }
        java.util.LinkedHashMap<Object, Object> hash = this._tree;
        for (Object o : path) {
            Object cur = hash.get((String) o);
            if (cur == null) {
                return null;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                hash = (java.util.LinkedHashMap) hash.get((String) o);
            }
        }
        if (hash.getClass().equals(java.util.LinkedHashMap.class)) {
            for (Object o : hash.keySet()) {
                result.add((String) o);
            }
        }
        return result;
    }

    public Object[] getHierarchy() {
        Object[] result = null;
        Object[] hierarchy = {
            "javax.swing",
            "javax.swing.border",
            "javax.swing.colorchooser",
            "javax.swing.event",
            "javax.swing.filechooser",
            new Object[]{"javax.swing.plaf",
                "javax.swing.plaf.basic",
                "javax.swing.plaf.metal",
                "javax.swing.plaf.multi"},
            "javax.swing.table",
            new Object[]{"javax.swing.text",
                new Object[]{"javax.swing.text.html",
                    "javax.swing.text.html.parser"},
                "javax.swing.text.rtf"},
            "javax.swing.tree",
            "javax.swing.undo"
        };
        result = hierarchy;
        return result;
    }

    public Object[] getObjectTree(String rootName) {
        Object[] result = getObjectTree(rootName, _tree);
        return result;
    }

    private Object[] getObjectTree(String name, java.lang.Object object) {
        java.util.LinkedHashMap<Object, Object> hash = (java.util.LinkedHashMap) object;
        Object[] result = new Object[hash.size() + 1];
        result[0] = name;
        int index = 1;
        for (Object o : hash.keySet()) {
            Object cur = hash.get(o);
            if (cur == null) {
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                result[index] = getObjectTree((String) o, cur);
            } else if (cur.getClass().equals(String.class)) {
                result[index] = (String) o + " (Example Value: " + (String) cur + ")";
            }
            index++;
        }
        return result;
    }

    private Object[] appendObject(Object[] oArray, Object o) {
        Object[] result = new Object[oArray.length + 1];
        System.arraycopy(oArray, 0, result, 0, oArray.length); // very nice
        result[oArray.length] = o;
        return result;
    }

    public HashTree pickCherry(String str) {
        HashTree result = null;
        java.util.ArrayList path = getPath(str);
        java.util.LinkedHashMap<Object, Object> hash = this._tree;
        for (Object o : path) {
            Object cur = hash.get((String) o);
            if (cur == null) {
                return null;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                hash = (java.util.LinkedHashMap) hash.get((String) o);
            }
        }
        result = new HashTree();
        result._tree = hash;
        return result;
    }

    public String getString(String str) {
        java.util.ArrayList path = getPath(str);
        String result = "";
        java.util.LinkedHashMap<Object, Object> hash = this._tree;
        for (Object o : path) {
            Object cur = hash.get((String) o);
            if (cur == null) {
                return null;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                hash = (java.util.LinkedHashMap) hash.get((String) o);
            } else if (cur.getClass().equals(String.class)) {
                result = (String) hash.get((String) o);
                if (isNullString(result)) {
                    return null;
                }
                return result;
            }
        }
        for (Object o : hash.keySet()) {
            result += (String) o + resultSeparator;
        }
        if (result.contains(resultSeparator)) {
            result = result.substring(0, result.lastIndexOf(resultSeparator));
        }
        if (!isNullString(result)) {
            return result;
        }
        return null;
    }

    public java.util.Map<Object, Object> getMap(String str) { // Note: returns the list of branches, no leafs!
        java.util.ArrayList path = getPath(str);
        java.util.Map<Object, Object> result = this._tree;

        for (Object o : path) {
            Object cur = result.get((String) o);
            if (cur == null) {
                return null;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                result = (java.util.LinkedHashMap) result.get((String) o);
            } else if (cur.getClass().equals(String.class)) {
                return null;
            }
        }
        return result;
    }

    public String getTreeStr() {
        int depth = 0;
        _printTreeStr = "";
        for (Object o : _tree.keySet()) {
            Object cur = _tree.get(o);
            if (cur == null) {
                _printTreeStr += "[" + (String) o + "]" + _newlineCharacter;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                java.util.LinkedHashMap<Object, Object> hash = (java.util.LinkedHashMap) _tree.get(o);
                _printTreeStr += "[" + (String) o + "=" + _newlineCharacter;
                getTreeStr(hash, depth);
            } else if (cur.getClass().equals(String.class)) {
                _printTreeStr += "[" + (String) o + "=" + (String) cur + "]" + _newlineCharacter;
            }
        }
        int lastNewLine = _printTreeStr.lastIndexOf(_newlineCharacter);
        if (lastNewLine > -1) {
            _printTreeStr = _printTreeStr.substring(0, lastNewLine);
            return _printTreeStr;
        }
        return "Invalid Tree Structure";
    }

    private void getTreeStr(java.lang.Object object, int depth) {
        String spacer = "";
        depth++;
        for (int i = 0; i < depth; i++) {
            spacer += "    ";
        }
        java.util.LinkedHashMap<Object, Object> hash = (java.util.LinkedHashMap) object;
        for (Object o : hash.keySet()) {
            String output = spacer + "[" + (String) o;
            Object cur = hash.get(o);
            if (cur == null) {
                _printTreeStr += output + "]" + _newlineCharacter;
            } else if (cur.getClass().equals(java.util.LinkedHashMap.class)) {
                _printTreeStr += output + "=" + _newlineCharacter;
                getTreeStr(cur, depth);
            } else if (cur.getClass().equals(String.class)) {
                _printTreeStr += output + "=" + (String) cur + "]" + _newlineCharacter;
            }
        }
        int lastNewLine = _printTreeStr.lastIndexOf(_newlineCharacter);
        _printTreeStr = _printTreeStr.substring(0, lastNewLine) + "]" + _newlineCharacter;
    }

    private java.util.ArrayList<String> getPath(String str) {
        if (str == null) {
            return null;
        }
        java.util.ArrayList<String> path = new java.util.ArrayList();
        while (str.length() > 0) {
            int index = str.indexOf(pathSeparator);
            if ((index < 0) && (str.length() > 0)) {
                path.add(str);
                str = "";
            } else {
                String name = str.substring(0, index);
                path.add(name);
                str = str.substring(index + pathSeparator.length(), str.length());
            }
        }
        if (path.isEmpty()) {
            return null;
        }
        return path;
    }

    private java.util.LinkedHashMap<Object, Object> buildTree(String dataIn) {
        String data = removeFormatting(dataIn);
        String block = "";
        String key = "";
        String value = "";
        java.util.LinkedHashMap<Object, Object> newMap = new java.util.LinkedHashMap();
        while (data.length() > 0) {
            block = getBlock(data);
            if (isBlockLeaf(block)) {
                key = getKey(block);
                value = getValue(block);
                if (key != null) {
                    newMap.put(key, value);
                    data = removeBlock(data, block);
                }
            } else if (!isWhiteSpace(block)) {
                key = getKey(block);
                value = getValue(block);
                java.util.LinkedHashMap<Object, Object> v = buildTree(value);
                newMap.put(key, v);
                data = removeBlock(data, block);
            } else {
                break;
            }
        }
        return newMap;
    }

    private java.util.LinkedHashMap<Object, Object> mergeTrees(java.util.LinkedHashMap<Object, Object> first, java.util.LinkedHashMap<Object, Object> second) { // the two trees to merge, normal hash behavior is to copy over and drop old leafs.
        for (Object o : second.keySet()) {
            if (first.containsKey(o)) {
                Object f = first.get(o);
                Object s = second.get(o);
                if (f.getClass().equals(java.util.LinkedHashMap.class)) {
                    first.put(o, mergeTrees((java.util.LinkedHashMap<Object, Object>) f, (java.util.LinkedHashMap<Object, Object>) s));
                }
            } else {
                first.put(o, second.get(o));
            }
        }
        return first;
    }

    private String removeFormatting(String str) {
        str = fixVistaNewlineSpacingErrors(str);
        return removeNewLines(str);
    }

    private String removeSpaces(String s) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(s, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    public String fixVistaNewlineSpacingErrors(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\n') {
                try {
                    c = str.charAt(i - 1);
                    if (checkChar(c)) {
                        try {
                            c = str.charAt(i + 1);
                            if (checkChar(c)) {
                                String before = str.substring(0, i);
                                String after = str.substring(i + 1, str.length());
                                str = before + " " + after;
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        return str;
    }

    private boolean checkChar(char c) {
        if ((c != ' ') && (c != '\n')) {
            return true;
        }
        return false;
    }

    private String removeNewLines(String s) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(s, _newlineCharacter, false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    public String replaceCharacter(String s, String character) {
        java.util.StringTokenizer st = new java.util.StringTokenizer(s, character, false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    private String getKey(String str) {
        int index = str.indexOf('=');
        if (index > -1) {
            return str.substring(0, index);
        }
        if (!str.equalsIgnoreCase("")) {
            return str;
        }
        return null;
    }

    private String getValue(String str) {
        int index = str.indexOf('=');
        String result = null;
        if (index > -1) {
            result = str.substring(index + 1, str.length());
            if ((result.length() > 0) && !isNullString(result)) {
                return result;
            }
        }
        return null;
    }

    private boolean isBlockLeaf(String str) {
        if (str.contains("[") && str.contains("]") && str.contains(",") && !str.contains("=") && !str.isEmpty()) {
            return true;
        }
        if (str.contains("[") || str.contains("]") || str.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isWhiteSpace(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ') {
                return false;
            }
        }
        return true;
    }

    private String getBlock(String str) {
        int counter = 1;
        int blockStart = str.indexOf('[');
        int blockEnd = 0;
        for (int i = blockStart + 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '[') {
                counter++;
            } else if (c == ']') {
                counter--;
            }
            if (counter == 0) {
                blockEnd = i;
                break;
            }
        }
        if ((blockStart > -1) && (blockEnd > -1)) {
            String result = "";
            try {
                return str.substring(blockStart + 1, blockEnd);
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return "";
    }

    private String removeBlock(String doc, String block) {
        return replace(doc, "[" + block + "]", "");
    }

    private String replace(String myString, String replace, String with) {
        int start = myString.indexOf(replace);
        if (start >= 0) {
            int end = replace.length() + start;
            if (end >= 0) {
                String before = myString.substring(0, start);
                String after = myString.substring(end, myString.length());
                return before + with + after;
            }
        }
        return "";
    }

    private boolean isNullString(String str) {
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\r", "");
        if (str.length() > 0) {
            return false;
        }
        return true;
    }
}
