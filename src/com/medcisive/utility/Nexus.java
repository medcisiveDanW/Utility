package com.medcisive.utility;

import java.util.List;

/**
 *
 * @author Chamtrain
 */
public class Nexus {

    private java.util.HashMap<Object, Object> _root;
    protected transient String _newlineCharacter = "\n";
    private transient String _printTreeStr;
    private transient java.util.HashMap<Object, KeyBuilder> _keyBuilders;


    public Nexus() {
        _root = new java.util.HashMap();
        _keyBuilders = new java.util.HashMap();
    }

    public Nexus(java.util.Map root) {
        _root = (java.util.HashMap)root;
    }

    public class KeyBuilder {

        private transient java.util.Stack<Object> _keys = new java.util.Stack();

        public KeyBuilder push(Object key) {
            if (key == null) {
                throw new NullPointerException();
            } else {
                _keys.push(key);
            }
            return this;
        }

        public KeyBuilder pop() {
            _keys.pop();
            return this;
        }

        public Object get() {
            return _get(_keys);
        }

        public java.util.Stack getInverse() {
            return _keys;
        }
        public void put(Object value) {
            if (value == null) {
                throw new NullPointerException();
            } else {
                _put(_keys,value);
            }
        }
    }

    public KeyBuilder key(Object key) {
        KeyBuilder result = null;
        if (key == null) {
            throw new NullPointerException();
        } else {
            if (_keyBuilders.containsKey(key)) {
                result = _keyBuilders.get(key);
            } else {
                result = new KeyBuilder();
                _keyBuilders.put(key, result);
            }
            return result;
        }
    }

    public Object getKeys() {
        return _keyBuilders.keySet();
    }

    public void removeKey(Object key) {
        _keyBuilders.remove(key);
    }

    public void clearKeys() {
        _keyBuilders.clear();
    }

    private void _put(List<Object> keys, Object value) {
        java.util.HashMap result = _root;
        Object current = null;
        Object last = null;
        int counter = 0;
        for (Object o : keys) {
            last = o;
            if (counter == keys.size() - 1) { // save last for insert value
                break;
            }
            current = ((java.util.HashMap) result).get(o);
            if (current == null) {
                java.util.HashMap map = new java.util.HashMap();
                ((java.util.HashMap) result).put(o, map);
                result = map;
            } else if (result.getClass().equals(java.util.HashMap.class)) {
                result = (java.util.HashMap) current;
            }
            counter++;
        }
        result.put(last, value);
    }

    private Object _get(List<Object> keys) {
        if (keys == null) {
            return _root.keySet();
        }
        Object result = _root;
        for (Object o : keys) {
            if (result.getClass().equals(java.util.HashMap.class) || result.getClass().equals(java.util.LinkedHashMap.class)) {
                result = ((java.util.HashMap) result).get(o);
            }
            else if(result.getClass().equals(java.util.ArrayList.class)) {
                result = ((java.util.ArrayList) result).get((Integer)o);
            }
            else {
                return result;
            }
        }
        return result;
    }

    public void print() {
        System.out.println(_getTreeStr());
    }
    
    @Override
    public String toString() {
        return _getTreeStr();
    }

    private String _getTreeStr() {
        int depth = 0;
        _printTreeStr = "";
        for (Object o : _root.keySet()) {
            Object cur = _root.get(o);
            if (cur == null) {
                _printTreeStr += "[" + (String) o + "]" + _newlineCharacter;
            } else if (cur.getClass().equals(java.util.HashMap.class)) {
                java.util.HashMap hash = (java.util.HashMap) cur;
                _printTreeStr += "[" + (String) o + "=" + _newlineCharacter;
                _getTreeStr(hash, depth);
            } else {
                _printTreeStr += "[" + o + "=" + cur + "]" + _newlineCharacter;
            }
        }
        int lastNewLine = _printTreeStr.lastIndexOf(_newlineCharacter);
        if (lastNewLine > -1) {
            _printTreeStr = _printTreeStr.substring(0, lastNewLine);
            return _printTreeStr;
        }
        return "Invalid Tree Structure";
    }

    private void _getTreeStr(java.lang.Object object, int depth) {
        String spacer = "";
        depth++;
        for (int i = 0; i < depth; i++) {
            spacer += "    ";
        }
        if (object.getClass().equals(java.util.HashMap.class)) {
            java.util.HashMap<Object, Object> hash = (java.util.HashMap) object;
            for (Object o : hash.keySet()) {
                String output = spacer + "[" + (String) o;
                Object cur = hash.get(o);
                if (cur == null) {
                    _printTreeStr += output + "]" + _newlineCharacter;
                } else {
                    _printTreeStr += output + "=" + _newlineCharacter;
                    _getTreeStr(cur, depth);
                }
            }
            int lastNewLine = _printTreeStr.lastIndexOf(_newlineCharacter);
            _printTreeStr = _printTreeStr.substring(0, lastNewLine) + "]" + _newlineCharacter;
        } else {
            _printTreeStr += spacer + "[" + object + "]" + _newlineCharacter;
        }
    }
}