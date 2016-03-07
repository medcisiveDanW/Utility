package com.medcisive.utility.protegecopse;

import java.util.*;
import java.io.*;

/**
 *
 * @author vhapalchambj
 */
public class CXRDataBuilder {

    private String SPANNEDTEXT;
    private String SPAN;
    private String DEVICE;
    private String STATUS;
    private String STATUSSLOTTYPE;
    private String STATUSINF;
    private String NUMERALITY;
    private String NUMERALITYSLOTTYPE;
    private String NUMERALITYINF;
    private String LATERALITY;
    private String DOCUMENTQUALITY;
    private String start;
    private String end;
    public String docQuality;

    public CXRDataBuilder() {
        SPANNEDTEXT = "spannedText";
        SPAN = "span";
        DEVICE = "Device/Line";
        STATUS = "Device/Line_Status";
        STATUSINF = "a_Device/Line_Status_Determination";
        NUMERALITY = "Device/Line_Qty";
        NUMERALITYINF = "a_Device/Line_Qty_Determination";
        LATERALITY = "Laterality";
        STATUSSLOTTYPE = "stringSlotMentionValue";
        NUMERALITYSLOTTYPE = "stringSlotMentionValue";
        DOCUMENTQUALITY = "Document_Quality";
    }

    public CXRDataBuilder(String configFilename) {
        Properties settings = new Properties();
        try {
            FileInputStream sf = new FileInputStream(configFilename);
            settings.load(sf);
        } catch (Exception ex) {
            System.out.println("Unable to open: " + configFilename);
        }
        SPANNEDTEXT = settings.getProperty("SPANNEDTEXT", "");
        SPAN = settings.getProperty("SPAN", "");
        DEVICE = settings.getProperty("DEVICE", "");
        STATUS = settings.getProperty("STATUS", "");
        STATUSSLOTTYPE = settings.getProperty("STATUSSLOTTYPE", "");
        STATUSINF = settings.getProperty("STATUSINF", "");
        NUMERALITY = settings.getProperty("NUMERALITY", "");
        NUMERALITYSLOTTYPE = settings.getProperty("NUMERALITYSLOTTYPE", "");
        NUMERALITYINF = settings.getProperty("NUMERALITYINF", "");
        LATERALITY = settings.getProperty("LATERALITY", "");
    }

    public ArrayList<DeviceData> build(ObjectCopse oc) {
        ArrayList<DeviceData> devData = new ArrayList();
        for (int i = 0; i < oc.sizeOf(); i++) {
            String inf;
            String devMention = oc.pull(i, SPANNEDTEXT, DEVICE, ObjectPosition.PARENT);
            String span = oc.pull(i, SPAN, DEVICE, ObjectPosition.PARENT);
            splitSpan(span);
            TextSpan tsDevMen = new TextSpan(tryParseInt(start), tryParseInt(end));
            if (devMention != null) {
                String devStatus = oc.pull(i, SPANNEDTEXT, STATUS, ObjectPosition.PARENT);
                span = oc.pull(i, SPAN, STATUS, ObjectPosition.PARENT);
                splitSpan(span);
                TextSpan tsDevStat = new TextSpan(tryParseInt(start), tryParseInt(end));
                inf = oc.pull(i, STATUSSLOTTYPE, STATUSINF, ObjectPosition.CURRENT);

                DeviceData d = new DeviceData(devMention, tsDevMen, devStatus, tsDevStat, inf);

                String num = oc.pull(i, SPANNEDTEXT, NUMERALITY, ObjectPosition.PARENT);
                span = oc.pull(i, SPAN, NUMERALITY, ObjectPosition.PARENT);
                splitSpan(span);
                TextSpan tsNumerality = new TextSpan(tryParseInt(start), tryParseInt(end));
                inf = oc.pull(i, NUMERALITYSLOTTYPE, NUMERALITYINF, ObjectPosition.CURRENT);
                if (num != null) {
                    d.setNumerality(num);
                    d.setNumeralitySpan(tsNumerality);
                    d.setNumeralityInf(tryParseInt(inf));
                }

                String lat = oc.pull(i, SPANNEDTEXT, LATERALITY, ObjectPosition.PARENT);
                span = oc.pull(i, SPAN, LATERALITY, ObjectPosition.PARENT);
                splitSpan(span);
                TextSpan tsLat = new TextSpan(tryParseInt(start), tryParseInt(end));
                if (lat != null) {
                    d.setLaterality(lat);
                    d.setLaterSpan(tsLat);
                }
                if (d.getDevice() != null) {
                    devData.add(d);
                } else {
                    docQuality = oc.pull(i, STATUSSLOTTYPE, DOCUMENTQUALITY, ObjectPosition.CHILD);
                }
            }
        }
        return devData;
    }

    private int tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: " + e + "\nCannot parse int from " + s);
        }
        return -1;
    }

    private void splitSpan(String s) {
        if (s != null) {
            int index = s.indexOf(',');
            end = s.substring(index + 1);
            start = s.substring(0, index);
        } else {
            start = "-1";
            end = "-1";
        }
    }
}
