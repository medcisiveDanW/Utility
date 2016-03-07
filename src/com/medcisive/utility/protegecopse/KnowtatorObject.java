package com.medcisive.utility.protegecopse;

import java.util.*;

/**
 *
 * @author vhapalchambj
 */
public class KnowtatorObject {

    public HashMap myData;        //List of key value data pairs within this object
    public ArrayList<KnowtatorObject> myChildren;    //Pointer to child XMLObjects

    public KnowtatorObject() {
        myData = new HashMap();
        myChildren = new ArrayList();
    }

    public String pullData(int depth, String tagName) {
        if (depth <= 0) {
            return (String) myData.get(tagName);
        } else {
            depth--;
            //String temp = null;
            for (Object o : myChildren) {
                KnowtatorObject ko = (KnowtatorObject) o;
                String s = ko.pullData(depth, tagName);
                if (s != null) {
                    return s;
                }
            }
        }
        return null;
    }

    public void print() {
        print(0);
    }

    private void print(int depth) {
        String endent = "";
        for (int i = 0; i < depth; i++) {
            endent += "   ";
        }
        for (Object o : myData.keySet()) {
            String key = (String) o;
            String value = (String) myData.get(key);
            System.out.println(endent + key + ": " + value);
        }
        depth++;
        for (Object o : myChildren) {
            KnowtatorObject ko = (KnowtatorObject) o;
            ko.print(depth);
        }
    }

    public KnowtatorObject pull(String pullTagName, String refernceValue, ObjectPosition rp) {
        KnowtatorObject returnKO = null;
        KnowtatorObject tempKO = null;
        for (KnowtatorObject koChild : myChildren) {
            if (koChild.contains(refernceValue)) {
                returnKO = returnKnowtatorObject(koChild, rp);
            }

        }
        if (returnKO == null) {
            for (KnowtatorObject koChild : myChildren) {
                tempKO = koChild.pull(pullTagName, refernceValue, rp);
                if (tempKO != null) {
                    returnKO = tempKO;
                }
            }

        }
        return returnKO;
    }

    private KnowtatorObject returnKnowtatorObject(KnowtatorObject child, ObjectPosition rp) {
        switch (rp) {
            case CURRENT:
                return child;
            case CHILD:
                return child.myChildren.get(0);
            case PARENT:
                return this;
            default:
                break;
        }
        return null;
    }

    private boolean contains(String value) {
        for (Object o : myData.values()) {
            String s = (String) o;
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
