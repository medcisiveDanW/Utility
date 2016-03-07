package com.medcisive.utility.protegecopse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author vhapalchambj
 */
public class ObjectCopse {

    private HashMap rawData;
    private ArrayList<KnowtatorObject> data;
    private File dataFile;
    private DocumentBuilderFactory factory;
    private Document doc;
    private Element root;
    private HashMap classeTypes;

    public ObjectCopse() {
    }

    public ObjectCopse(String filename) {
        loadData(filename);
    }

    public void loadData(String filename) {
        rawData = new HashMap();
        data = new ArrayList();
        dataFile = new File(filename);
        factory = DocumentBuilderFactory.newInstance();
        doc = null;
        root = null;
        generateDataStructure();
        this.generateKnowtatorDataStructure();
    }

    private void generateDataStructure() {
        if (dataFile.exists()) {
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse(dataFile);
                root = doc.getDocumentElement();

                findXMLclassTypes();
                generateBaseDataStructure();
                finalizeBaseDataStructure();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void findXMLclassTypes() {
        classeTypes = new HashMap();
        NodeList nnm = root.getChildNodes();
        for (int i = 0; i < nnm.getLength(); i++) {
            if (!classeTypes.containsKey(nnm.item(i).getNodeName()) && (!nnm.item(i).getNodeName().equalsIgnoreCase("#text"))) {
                classeTypes.put(nnm.item(i).getNodeName(), new Hashtable());
            }
        }
    }

    private void generateBaseDataStructure() {
        for (Object o : classeTypes.keySet()) {
            String s = (String) o;
            NodeList list = root.getElementsByTagName(s);
            for (int i = 0; i < list.getLength(); i++) {
                HashMap data = new HashMap();
                Element e = (Element) list.item(i);
                NodeList l = e.getChildNodes();
                int counter = 0;
                for (int j = 0; j < l.getLength(); j++) {
                    if (!l.item(j).getNodeName().equalsIgnoreCase("#text")) {
                        Element currentNode = (Element) l.item(j);
                        if (data.containsKey(currentNode.getNodeName())) {
                            data.put(currentNode.getNodeName() + (counter++), pullDataFromCurrentElement(currentNode));
                        } else {
                            data.put(currentNode.getNodeName(), pullDataFromCurrentElement(currentNode));
                        }
                    }
                }
                XMLObject newXMLobj;
                if (e.getAttribute("id").isEmpty()) {
                    newXMLobj = new XMLObject(s, "ROOT" + data.get("mention").toString(), data, rawData);
                } else {
                    newXMLobj = new XMLObject(s, e.getAttribute("id"), data, rawData);
                }
                rawData.put(newXMLobj.myID, newXMLobj);
            }
        }
    }

    private void finalizeBaseDataStructure() {
        for (Object o : rawData.values()) {
            XMLObject x = (XMLObject) o;
            x.findChildren();
        }

        for (Object o : rawData.values()) {
            XMLObject x = (XMLObject) o;
            x.assignChildrenToParents();
        }

        HashMap removeList = new HashMap();
        for (Object o1 : rawData.values()) {
            XMLObject currentNode = (XMLObject) o1;
            for (Object o2 : currentNode.myChildren.values()) {
                XMLObject myChild = (XMLObject) o2;
                if (rawData.containsValue(myChild)) {
                    removeList.put(myChild.myID, myChild);
                }
            }
        }
        for (Object o : removeList.values()) {
            XMLObject x = (XMLObject) o;
            rawData.remove(x.myID);
        }
    }

    private void generateKnowtatorDataStructure() {
        for (Object o : rawData.values()) {
            XMLObject xmlObj = (XMLObject) o;
            data.add(xmlObj.buildKnowtatorObject());
        }
    }

    public void printForest() {
        for (Object o : data) {
            System.out.println("========================XML Object=========================");
            KnowtatorObject ko = (KnowtatorObject) o;
            ko.print();
        }
    }

    private String pullDataFromCurrentElement(Element currentNode) {
        String str = "";
        if (currentNode.getNodeName().equalsIgnoreCase("span")) {
            str = currentNode.getAttribute("start") + "," + currentNode.getAttribute("end");
        } else if (!currentNode.getTextContent().isEmpty()) {
            str = currentNode.getTextContent();
        } else if (!currentNode.getAttribute("id").isEmpty()) {
            str = currentNode.getAttribute("id");
        } else {
            str = currentNode.getAttribute("value");
        }
        return str;
    }

    public int sizeOf() {
        return data.size();
    }

    public String pullData(int objNumber, int depth, String tagName) {
        return data.get(objNumber).pullData(depth, tagName);
    }

    public String pull(int objNumber, String pullTagName, String refernceValue, ObjectPosition rp) {
        String result = null;
        if(objNumber < data.size()){
            KnowtatorObject ko = data.get(objNumber);
            ko = ko.pull(pullTagName, refernceValue, rp);
            if (ko != null) {
                result = (String) ko.myData.get(pullTagName);
            }
        }
        return result;
    }
}