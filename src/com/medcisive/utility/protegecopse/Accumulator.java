package com.medcisive.utility.protegecopse;

import java.util.*;
import java.io.*;

/**
 *
 * @author vhapalchambj
 */
public class Accumulator {

    private StringBuffer fileText;
    private java.util.Date todayDate;
    private java.sql.Timestamp now;
    private java.text.SimpleDateFormat sdf;
    private String today;
    private File[] fileLst;

    public Accumulator() {
        todayDate = new java.util.Date();
        now = new java.sql.Timestamp(todayDate.getTime());
        sdf = new java.text.SimpleDateFormat("ddMMMyyyy");
        today = sdf.format(now).toString();
        File srcDir = new File(".");
        fileLst = srcDir.listFiles();
        fileText = new StringBuffer();
        go();
        save();
    }

    public void go() {
        int counter = 0;
        fileText.append("\"Report_Number\",\"Device\",\"DeviceSpanStart\",\"DeviceSpanEnd\",\"Status\",\"StatusSpanStart\",\"StatusSpanEnd\",\"Laterality\",\"LateralitySpanStart\",\"LateralitySpanEnd\",\"Numerality\",\"NumeralitySpanStart\",\"NumeralitySpanEnd\",\"NumeralityInference\",\"Inference\",\"Document_Quality\"\r\n");
        for (int i = 0; i < fileLst.length; i++) {
            String s = fileLst[i].toString();
            if (s.endsWith(".txt.knowtator.xml")) {
                System.out.println("Attempting file: " + s);
                counter++;
                ObjectCopse o = new ObjectCopse(s);
                CXRDataBuilder builder = new CXRDataBuilder();
                ArrayList<DeviceData> devData = builder.build(o);
                if (devData != null) {
                    for (DeviceData dd : devData) {
                        String temp = "\"" + counter + "\",";
                        //Device name and span
                        temp += insertFragment(dd.getDevice(), dd.getDevFragPlural()) + ",";
                        temp += insertSpan(dd.getDeviceSpan()) + ",";
                        //Status and span
                        temp += insertString(dd.getDevStatus()) + ",";
                        temp += insertSpan(dd.getDevStatusSpan()) + ",";
                        //Laterality and span
                        temp += insertString(dd.getLaterality()) + ",";
                        temp += insertSpan(dd.getLaterSpan()) + ",";
                        //Numerality and span
                        temp += insertString(dd.getNumerality()) + ",";
                        temp += insertSpan(dd.getNumeralitySpan()) + ",";
                        if (dd.getNumeralityInf() != null) {
                            temp += dd.getNumeralityInf().toString();
                        }
                        temp += ",";
                        //Inference
                        temp += insertString(dd.getStatusInference()) + ",";
                        //Document_Quality
                        temp += insertString(builder.docQuality) + "\r\n";
                        fileText.append(temp);
                    }
                    System.out.println("Finished file!!!\n");
                } else {
                    System.out.println("No device data list for this file!");
                }
            }
        }
    }

    private void save() {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(today + "KnowtatorData.txt"));
            bufferedWriter.write(fileText.toString());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String insertString(String str) {
        String temp = "";
        if ((str != null) && (!str.equalsIgnoreCase(""))) {
            temp = "\"" + str + "\"";
        }
        return temp;
    }

    private String insertFragment(String str, String frag) {
        String temp = "";
        if ((str != null) && (!str.equalsIgnoreCase(""))) {
            temp = "\"" + str;
            if ((frag != null) && (!frag.equalsIgnoreCase(""))) {
                temp += " " + frag + "\"";
            } else {
                temp += "\"";
            }
        }
        return temp;
    }

    private String insertSpan(TextSpan ts) {
        String temp = "";
        if (ts != null) {
            if (ts.getStart() >= 0) {
                temp = "\"" + ts.getStart() + "\",";
            } else {
                temp = ",";
            }
            if (ts.getEnd() > 0) {
                temp = temp + "\"" + ts.getEnd() + "\"";
            }
        } else {
            temp = ",";
        }
        return temp;
    }
}