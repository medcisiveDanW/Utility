package com.medcisive.utility;

/**
 *
 * @author vhapalchambj
 */
public class SHashTree extends Nexus {

    public transient String resultSeparator = ",";
    public transient String pathSeparator = "->";

    public SHashTree() {
        super();
    }

    public SHashTree(String strHash) {
        //_tree = buildTree(strHash);
    }

    public void put(Object key, Object value) {
        String strHash = "[" + key + "=" + value + "]";
        java.util.LinkedHashMap<Object, Object> subTree = buildTree(strHash);
        if (subTree != null) {
            //_tree = mergeTrees(_tree, subTree);
        }
    }

    public void add(String strHash) {
        java.util.LinkedHashMap<Object, Object> subTree = buildTree(strHash);
        if (subTree != null) {
            //_tree = mergeTrees(_tree, subTree);
        }
    }

    public String get(String str) {
        java.util.ArrayList path = getPath(str);
        String value = "";
        /*java.util.LinkedHashMap<Object, Object> hash = this._tree;
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
         *
         */
        return value;
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
        /*java.util.LinkedHashMap<Object, Object> hash = this._tree;
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
         *
         */
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


    private Object[] appendObject(Object[] oArray, Object o) {
        Object[] result = new Object[oArray.length + 1];
        System.arraycopy(oArray, 0, result, 0, oArray.length); // very nice
        result[oArray.length] = o;
        return result;
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

    private void build(String str) {

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
