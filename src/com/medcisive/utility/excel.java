package com.medcisive.utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author vhapalchambj
 */
public class excel {


    public void saveToExcel()
    {
        System.out.println("Saving!");
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd_hh.mm");
        String today = formater.format(date);
        String filepath = "C:\\Documents and Settings\\vhapalchambj\\Desktop\\";
        String filename = filepath + today + "_TEXT.xls";
        try
        {
            WritableCellFormat integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
            WritableCellFormat floatFormat = new WritableCellFormat(NumberFormats.FLOAT);
            jxl.write.Number number;

            WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
            WritableSheet s = workbook.createSheet("Sheet1", 0);

            s.addCell(new Label(0, 0, ""));
            s.addCell(new Label(1, 0, "Total"));
            s.addCell(new Label(2, 0, "Total(NLP)"));
            s.addCell(new Label(3, 0, "TP"));
            s.addCell(new Label(4, 0, "FN"));
            s.addCell(new Label(5, 0, "FP"));
            s.addCell(new Label(6, 0, "Recall "));
            s.addCell(new Label(7, 0, "Precision "));

            ArrayList<String> strMap = new ArrayList();
            strMap.add("device_mention");
            strMap.add("Inserted");
            strMap.add("Removed");
            strMap.add("Present");
            strMap.add("Absent");

            int count = 1;
            for(String str : strMap)
            {
                s.addCell(new Label(0, count, str));
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(1, count, 0, integerFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(2, count, 0, integerFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(3, count, 0, integerFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(4, count, 0, integerFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(5, count, 0, integerFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(6, count, 0, floatFormat);
                s.addCell(number);
                count++;
            }
            count = 1;
            for(String str : strMap)
            {
                number = new jxl.write.Number(7, count, 0, floatFormat);
                s.addCell(number);
                count++;
            }
            count++;
            s.addCell(new Label(0, count, "File Name"));
            s.addCell(new Label(1, count, "TP"));
            s.addCell(new Label(2, count, "FN"));
            s.addCell(new Label(3, count, "FP"));
            s.addCell(new Label(4, count, "TPPM"));
            s.addCell(new Label(5, count, "TPEM"));
            count++;
            workbook.write();
            workbook.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception with Excel export! File: " + filename + " Error: " + e);
        }
        System.out.println("Done!");
    }
}
