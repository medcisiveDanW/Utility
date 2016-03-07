package com.medcisive.utility.protegecopse;

import java.util.*;

/**
 *
 * @author Chamtrain
 */
public class XMLObject {

    public String myClass;
    public String myID;
    public XMLObject myParent;      //If not a Root node, you have a parent
    public HashMap myData;        //List of key value data pairs within this object
    public HashMap myChildren;    //Pointer to child XMLObjects
    private HashMap global;       //Local list of all XMLObjects.

    public XMLObject(String myClass, String myID, HashMap myData, HashMap global) {
        this.myClass = myClass;
        this.myID = myID;
        this.myParent = null;
        this.myData = myData;
        this.global = global;
        this.myChildren = new HashMap();
    }

    public void findChildren() {
        for (Object g : global.values()) {
            XMLObject globalChild = (XMLObject) g;
            for (Object md : myData.values()) {
                String myChild = (String) md;
                if (globalChild.myID.equalsIgnoreCase(myChild)) {
                    if (!rootExist(myChild)) {
                        globalChild.myParent = this;
                        myChildren.put(globalChild.myID, globalChild);
                    }
                }
            }
        }
    }

    public void assignChildrenToParents() {
        ArrayList<String> removeList = new ArrayList();
        for (Object o : myData.keySet()) {
            String key = (String) o;
            String value = (String) myData.get(key);
            if (rootExist(value)) {
                XMLObject parentChild = findParent(this);
                XMLObject parent = (XMLObject) global.get("ROOT" + value);
                parentChild.myChildren.put(parent.myID, parent);
                //parent.myChildren.put(parentChild.myID, parentChild);
                removeList.add(key);
            }
        }
        for (String s : removeList) {
            myData.remove(s);
        }
    }

    private boolean rootExist(String id) {
        String possibleChildPointsToRoot = "ROOT" + id;
        for (Object o : global.values()) {
            XMLObject x = (XMLObject) o;
            String possibleRootID = x.myID;
            if (possibleRootID.equalsIgnoreCase(possibleChildPointsToRoot) && !myID.equalsIgnoreCase(possibleChildPointsToRoot)) {
                return true;
            }
        }
        return false;
    }

    public XMLObject findParent(XMLObject child) {
        if (child.myParent != null) {
            return findParent(child.myParent);
        }
        return child;
    }

    public void printData() {
        System.out.println("myClass: " + myClass + "\n"
                + "myID: " + myID + "\n"
                + "myData: ");
        for (Object o : myData.keySet()) {
            String s = (String) o;
            System.out.println("    " + s + " : " + myData.get(s).toString());
        }
        if (!myChildren.isEmpty()) {
            System.out.println("        STARTchildren");
            for (Object o : myChildren.values()) {
                XMLObject s = (XMLObject) o;
                System.out.println("        " + s.myID);
                s.printData();
            }
        }
    }

    public KnowtatorObject buildKnowtatorObject() {
        KnowtatorObject ko = new KnowtatorObject();
        ko.myData = filterMyData();
        ko.myChildren = buildMyChildren();
        return ko;
    }

    private ArrayList buildMyChildren() {
        ArrayList al = new ArrayList();
        for (Object o : myChildren.values()) {
            XMLObject xmlObj = (XMLObject) o;
            al.add(xmlObj.buildKnowtatorObject());
        }
        return al;
    }

    private HashMap filterMyData() {
        HashMap ht = new HashMap();
        for (Object data : myData.keySet()) {
            boolean linkDetected = false;
            String key = (String) data;
            for (Object child : myChildren.values()) {
                XMLObject xmlObj = (XMLObject) child;
                if (xmlObj.myID.equalsIgnoreCase(myData.get(key).toString())) {
                    linkDetected = true;
                }
            }
            if (!linkDetected) {
                ht.put(key, myData.get(key));
            }
        }
        return ht;
    }
}
