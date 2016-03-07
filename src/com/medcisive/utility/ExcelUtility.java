//package com.medcisive.utility;
//
//import com.medcisive.utility.sql.DatabaseController;
//import com.medcisive.utility.sql.SQLUtility;
//import com.medcisive.utility.sql.TableMap;
//import java.io.File;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Map;
//import jxl.Workbook;
//import jxl.write.Label;
//import jxl.write.NumberFormats;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//
///**
// *
// * @author vhapalchambj
// */
//public class ExcelUtility {
//
//    private Date _date = Calendar.getInstance().getTime();
//    private SimpleDateFormat _formater = new SimpleDateFormat("yyyy.MM.dd_hh.mm");
//    private String _today = _formater.format(_date);
//    private String _filepath = "E:\\withPHI\\";
//    private WritableCellFormat _integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
//    private WritableCellFormat _floatFormat = new WritableCellFormat(NumberFormats.FLOAT);
//    private DatabaseController _dbc;
//    private int topDisplacement = 4;
//
//    public ExcelUtility() {
//        SQLUtility sqlUtil = SQLUtility.getSingleton("SQL_EXCEL");
//        sqlUtil.setDestinationDatabase("R01SCRDWH60", "1433", "Commend");
//        _dbc = sqlUtil.getDestinationDatabase();
//    }
//
//    public void saveDetails(String name) {
//        String filename = _filepath + _today + "_" + name + ".xls";
//        String sta3n = "640";
//        String fycat = "FYM0Flag";
//        try {
//            System.out.println("start");
//            WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
//            WritableSheet sheet = workbook.createSheet("Fiscal Year 2013", 0);
//            sheet.setColumnView(0, 8);
//            sheet.setColumnView(1, 25);
//            sheet.setColumnView(2, 5);
//            sheet.setColumnView(3, 25);
//            sheet.setColumnView(4, 5);
//            sheet.setColumnView(5, 10);
//            sheet.setColumnView(6, 10);
//            sheet.setColumnView(7, 10);
//            sheet.setColumnView(8, 10);
//            sheet.setColumnView(9, 10);
//            sheet.setColumnView(10,10);
//            sheet.setColumnView(11,10);
//            sheet.setColumnView(12,10);
//            TableMap providers = getProviders(sta3n,fycat);
//            int counter = topDisplacement;
//            for(int i : providers.keySet()) {
//                Map<String,Object> pro = providers.getRow(i);
//                String duz = (String)pro.get("providerDUZ");
//                sheet.addCell(insert(duz,0,counter));
//                sheet.addCell(insert(pro.get("providerName"),1,counter));
//                counter++;
//                sheet.addCell(insert("Sta3n",1,counter));
//                sheet.addCell(insert("L4",2,counter));
//                sheet.addCell(insert("Name",3,counter));
//                sheet.addCell(insert("FY",4,counter));
//                sheet.addCell(insert("Dx1",5,counter));
//                sheet.addCell(insert("Dx2",6,counter));
//                sheet.addCell(insert("First",7,counter));
//                sheet.addCell(insert("Last",8,counter));
//                sheet.addCell(insert("Completed",9,counter));
//                sheet.addCell(insert("Week 14",10,counter));
//                sheet.addCell(insert("Deadline",11,counter));
//                TableMap patients = getDetails(duz,sta3n,"2013");
//                if(patients==null) { continue; }
//                counter++;
//                for(int j : patients.keySet()) {
//                    Map<String,Object> pat = patients.getRow(j);
//                    String nl4 = (String)pat.get("ssn");
//                    nl4 = nl4.substring(5, 9);
//                    sheet.addCell(insert(sta3n,1,counter));
//                    sheet.addCell(insert(nl4,2,counter));
//                    sheet.addCell(insert(pat.get("name"),3,counter));
//                    sheet.addCell(insert(pat.get("fy"),4,counter));
//                    sheet.addCell(insert(pat.get("dx1"),5,counter));
//                    sheet.addCell(insert(pat.get("dx2"),6,counter));
//                    sheet.addCell(insert(pat.get("first"),7,counter));
//                    sheet.addCell(insert(pat.get("last"),8,counter));
//                    sheet.addCell(insert(pat.get("completed"),9,counter));
//                    sheet.addCell(insert(pat.get("week14"),10,counter));
//                    sheet.addCell(insert(pat.get("deadline"),11,counter));
//                    counter++;
//                }
//            }
//
//            System.out.println("first");
//
//            fycat = "FYM1Flag";
//            sheet = workbook.createSheet("Fiscal Year 2012", 0);
//            sheet.setColumnView(0, 8);
//            sheet.setColumnView(1, 25);
//            sheet.setColumnView(2, 5);
//            sheet.setColumnView(3, 25);
//            sheet.setColumnView(4, 5);
//            sheet.setColumnView(5, 10);
//            sheet.setColumnView(6, 10);
//            sheet.setColumnView(7, 10);
//            sheet.setColumnView(8, 10);
//            sheet.setColumnView(9, 10);
//            sheet.setColumnView(10,10);
//            sheet.setColumnView(11,10);
//            sheet.setColumnView(12,10);
//            providers = getProviders(sta3n,fycat);
//            counter = topDisplacement;
//            for(int i : providers.keySet()) {
//                Map<String,Object> pro = providers.getRow(i);
//                String duz = (String)pro.get("providerDUZ");
//                sheet.addCell(insert(duz,0,counter));
//                sheet.addCell(insert(pro.get("providerName"),1,counter));
//                counter++;
//                sheet.addCell(insert("Sta3n",1,counter));
//                sheet.addCell(insert("L4",2,counter));
//                sheet.addCell(insert("Name",3,counter));
//                sheet.addCell(insert("FY",4,counter));
//                sheet.addCell(insert("Dx1",5,counter));
//                sheet.addCell(insert("Dx2",6,counter));
//                sheet.addCell(insert("First",7,counter));
//                sheet.addCell(insert("Last",8,counter));
//                sheet.addCell(insert("Completed",9,counter));
//                sheet.addCell(insert("Week 14",10,counter));
//                sheet.addCell(insert("Deadline",11,counter));
//                TableMap patients = getDetails(duz,sta3n,"2012");
//                if(patients==null) { continue; }
//                counter++;
//                for(int j : patients.keySet()) {
//                    Map<String,Object> pat = patients.getRow(j);
//                    String nl4 = (String)pat.get("ssn");
//                    nl4 = nl4.substring(5, 9);
//                    sheet.addCell(insert(sta3n,1,counter));
//                    sheet.addCell(insert(nl4,2,counter));
//                    sheet.addCell(insert(pat.get("name"),3,counter));
//                    sheet.addCell(insert(pat.get("fy"),4,counter));
//                    sheet.addCell(insert(pat.get("dx1"),5,counter));
//                    sheet.addCell(insert(pat.get("dx2"),6,counter));
//                    sheet.addCell(insert(pat.get("first"),7,counter));
//                    sheet.addCell(insert(pat.get("last"),8,counter));
//                    sheet.addCell(insert(pat.get("completed"),9,counter));
//                    sheet.addCell(insert(pat.get("week14"),10,counter));
//                    sheet.addCell(insert(pat.get("deadline"),11,counter));
//                    counter++;
//                }
//            }
//
//            System.out.println("second");
//
//
//            fycat = "FYM0Flag";
//            sheet = workbook.createSheet("Fiscal Year 2013 OEFOIF", 0);
//            sheet.setColumnView(0, 8);
//            sheet.setColumnView(1, 25);
//            sheet.setColumnView(2, 5);
//            sheet.setColumnView(3, 25);
//            sheet.setColumnView(4, 5);
//            sheet.setColumnView(5, 10);
//            sheet.setColumnView(6, 10);
//            sheet.setColumnView(7, 10);
//            sheet.setColumnView(8, 10);
//            sheet.setColumnView(9, 10);
//            sheet.setColumnView(10,10);
//            sheet.setColumnView(11,10);
//            sheet.setColumnView(12,10);
//            providers = getProviders(sta3n,fycat);
//            counter = topDisplacement;
//            for(int i : providers.keySet()) {
//                Map<String,Object> pro = providers.getRow(i);
//                String duz = (String)pro.get("providerDUZ");
//                sheet.addCell(insert(duz,0,counter));
//                sheet.addCell(insert(pro.get("providerName"),1,counter));
//                counter++;
//                sheet.addCell(insert("Sta3n",1,counter));
//                sheet.addCell(insert("L4",2,counter));
//                sheet.addCell(insert("Name",3,counter));
//                sheet.addCell(insert("FY",4,counter));
//                sheet.addCell(insert("Dx1",5,counter));
//                sheet.addCell(insert("Dx2",6,counter));
//                sheet.addCell(insert("First",7,counter));
//                sheet.addCell(insert("Last",8,counter));
//                sheet.addCell(insert("Completed",9,counter));
//                sheet.addCell(insert("Week 14",10,counter));
//                sheet.addCell(insert("Deadline",11,counter));
//                TableMap patients = getOEFOIFDetails(duz,sta3n,"2013");
//                if(patients==null) { continue; }
//                counter++;
//                for(int j : patients.keySet()) {
//                    Map<String,Object> pat = patients.getRow(j);
//                    String nl4 = (String)pat.get("ssn");
//                    nl4 = nl4.substring(5, 9);
//                    sheet.addCell(insert(sta3n,1,counter));
//                    sheet.addCell(insert(nl4,2,counter));
//                    sheet.addCell(insert(pat.get("name"),3,counter));
//                    sheet.addCell(insert(pat.get("fy"),4,counter));
//                    sheet.addCell(insert(pat.get("dx1"),5,counter));
//                    sheet.addCell(insert(pat.get("dx2"),6,counter));
//                    sheet.addCell(insert(pat.get("first"),7,counter));
//                    sheet.addCell(insert(pat.get("last"),8,counter));
//                    sheet.addCell(insert(pat.get("completed"),9,counter));
//                    sheet.addCell(insert(pat.get("week14"),10,counter));
//                    sheet.addCell(insert(pat.get("deadline"),11,counter));
//                    counter++;
//                }
//            }
//
//            System.out.println("third");
//
//            fycat = "FYM1Flag";
//            sheet = workbook.createSheet("Fiscal Year 2012 OEFOIF", 0);
//            sheet.setColumnView(0, 8);
//            sheet.setColumnView(1, 25);
//            sheet.setColumnView(2, 5);
//            sheet.setColumnView(3, 25);
//            sheet.setColumnView(4, 5);
//            sheet.setColumnView(5, 10);
//            sheet.setColumnView(6, 10);
//            sheet.setColumnView(7, 10);
//            sheet.setColumnView(8, 10);
//            sheet.setColumnView(9, 10);
//            sheet.setColumnView(10,10);
//            sheet.setColumnView(11,10);
//            sheet.setColumnView(12,10);
//            providers = getProviders(sta3n,fycat);
//            counter = topDisplacement;
//            for(int i : providers.keySet()) {
//                Map<String,Object> pro = providers.getRow(i);
//                String duz = (String)pro.get("providerDUZ");
//                sheet.addCell(insert(duz,0,counter));
//                sheet.addCell(insert(pro.get("providerName"),1,counter));
//                counter++;
//                sheet.addCell(insert("Sta3n",1,counter));
//                sheet.addCell(insert("L4",2,counter));
//                sheet.addCell(insert("Name",3,counter));
//                sheet.addCell(insert("FY",4,counter));
//                sheet.addCell(insert("Dx1",5,counter));
//                sheet.addCell(insert("Dx2",6,counter));
//                sheet.addCell(insert("First",7,counter));
//                sheet.addCell(insert("Last",8,counter));
//                sheet.addCell(insert("Completed",9,counter));
//                sheet.addCell(insert("Week 14",10,counter));
//                sheet.addCell(insert("Deadline",11,counter));
//                TableMap patients = getOEFOIFDetails(duz,sta3n,"2012");
//                if(patients==null) { continue; }
//                counter++;
//                for(int j : patients.keySet()) {
//                    Map<String,Object> pat = patients.getRow(j);
//                    String nl4 = (String)pat.get("ssn");
//                    nl4 = nl4.substring(5, 9);
//                    sheet.addCell(insert(sta3n,1,counter));
//                    sheet.addCell(insert(nl4,2,counter));
//                    sheet.addCell(insert(pat.get("name"),3,counter));
//                    sheet.addCell(insert(pat.get("fy"),4,counter));
//                    sheet.addCell(insert(pat.get("dx1"),5,counter));
//                    sheet.addCell(insert(pat.get("dx2"),6,counter));
//                    sheet.addCell(insert(pat.get("first"),7,counter));
//                    sheet.addCell(insert(pat.get("last"),8,counter));
//                    sheet.addCell(insert(pat.get("completed"),9,counter));
//                    sheet.addCell(insert(pat.get("week14"),10,counter));
//                    sheet.addCell(insert(pat.get("deadline"),11,counter));
//                    counter++;
//                }
//            }
//
//            System.out.println("end");
//
//            workbook.write();
//            workbook.close();
//        } catch (Exception e) {
//            System.out.println("Exception with Excel export! File: " + filename + " Error: " + e);
//        }
//    }
//
//    private TableMap getProviders(String sta3n, String fiscalColumn) {
//        String query =
//                "SELECT DISTINCT providerDUZ,"
//                + "     providerName \n"
//                + "FROM Commend.dbo.CommendVISNPatientPanel \n"
//                + "WHERE " + fiscalColumn + " = 'Y' \n"
//                + "  AND sta3n = '" + sta3n + "' \n"
//                + "ORDER BY providerName";
//        return _dbc.getTable(query);
//    }
//
//    private TableMap getPatients(String duz, String sta3n, String fiscalColumn) {
//        String query =
//                "SELECT patientSID \n"
//                + "     ,patientIEN \n"
//                + "     ,patientName \n"
//                + "FROM Commend.dbo.CommendVISNPatientPanel \n"
//                + "WHERE providerDUZ = '" + duz + "' \n"
//                + "  AND " + fiscalColumn + " = 'Y' \n"
//                + "  AND sta3n = '" + sta3n + "'";
//        return _dbc.getTable(query);
//    }
//
//    private TableMap getDetails(String duz, String sta3n, String fy) {
//        String query =
//                "SELECT DISTINCT det.sta3n \n"
//                + "     ,det.ssn \n"
//                + "     ,det.ien \n"
//                + "     ,det.name \n"
//                + "     ,det.fy \n"
//                + "     ,det.dx1 \n"
//                + "     ,det.dx2 \n"
//                + "     ,det.first \n"
//                + "     ,det.last \n"
//                + "     ,det.completed \n"
//                + "     ,det.week14 \n"
//                + "     ,det.deadline \n"
//                + "FROM Commend.dbo.CommendVISNDetail det, \n"
//                + "     Commend.dbo.CommendVISNPatientPanel pan \n"
//                + "WHERE pan.providerDUZ = '" + duz + "' \n"
//                + "  AND pan.sta3n = '" + sta3n + "' \n"
//                + "  AND det.sta3n = pan.sta3n \n"
//                + "  AND det.fy = '" + fy + "' \n"
//                + "  AND pan.patientIEN = det.ien \n"
//                + "ORDER BY name";
//        return _dbc.getTable(query);
//    }
//
//    private TableMap getOEFOIFDetails(String duz, String sta3n, String fy) {
//        String query =
//                "SELECT DISTINCT det.sta3n \n"
//                + "     ,det.ssn \n"
//                + "     ,det.ien \n"
//                + "     ,det.name \n"
//                + "     ,det.fy \n"
//                + "     ,det.dx1 \n"
//                + "     ,det.dx2 \n"
//                + "     ,det.first \n"
//                + "     ,det.last \n"
//                + "     ,det.completed \n"
//                + "     ,det.week14 \n"
//                + "     ,det.deadline \n"
//                + "FROM Commend.dbo.CommendVISNDetail det,"
//                + "     Commend.dbo.CommendVISNPatientPanel pan,"
//                //+ "     VDWWork.PatSub.OEFOIFService o \n"
//                + "     VDW.SPatient.SPatient pt "
//                + "WHERE pan.providerDUZ = '" + duz + "' \n"
//                + "  AND pan.sta3n = '" + sta3n + "' \n"
//                + "  AND det.sta3n = pan.sta3n \n"
//                + "  AND det.fy = '" + fy + "' \n"
//                + "  AND pan.patientIEN = det.ien "
//                //+ "  AND pan.patientIEN = o.PatientIEN \n"
//
//
//
//                + "  and pan.patientIEN = pt.PatientIEN "
//                + "  and pt.Deceased = 'N' "
//                + "  and pt.VeteranFlag = 'Y' "
//                + "  and pt.LastServiceSeparationDate > '2001-09-11' "
//                + "  and pt.EligibilityVACode in ('1', '2', '3', '4', '5') "
//
//                + "ORDER BY name";
//        return _dbc.getTable(query);
//    }
//
//    private jxl.write.WritableCell insert(Object item, int columnIndex, int rowIndex) {
//        jxl.write.WritableCell result = null;
//        if(item==null) {
//            return new Label(columnIndex, rowIndex, (String) "");
//        }
//        if (item.getClass().equals(String.class)) {
//            result = new Label(columnIndex, rowIndex, (String) item);
//        } else if (item.getClass().equals(Integer.class)) {
//            result = new jxl.write.Number(columnIndex, rowIndex, (Integer) item, _integerFormat);
//        } else if (item.getClass().equals(Float.class)) {
//            result = new jxl.write.Number(columnIndex, rowIndex, (Float) item, _floatFormat);
//        } else if (item.getClass().equals(java.sql.Timestamp.class)) {
//            result = new jxl.write.DateTime(columnIndex, rowIndex, (java.sql.Timestamp)item);
//        }
//        return result;
//    }
//}
