package com.medcisive.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author vhapalchambj
 */
public class Util {
    private static int _fileNameCount = 0;

    public static String listToString(java.util.List list) throws java.lang.IllegalArgumentException {
        if(list==null) { throw new java.lang.IllegalArgumentException("Error: List is null."); }
        String result = "";
        for(Object o : list) {
            result += o + ",";
        }
        result = result.substring(0,result.lastIndexOf(","));
        return result;
    }

    public static java.util.List<String> stringToList(String list) throws java.lang.IllegalArgumentException {
        if(list==null) { throw new java.lang.IllegalArgumentException("Error: List is null."); }
        java.util.List<String> result = new java.util.ArrayList();
        int index = -1;
        while(list.length()>0) {
            index = list.indexOf(',');
            if(index>0) {
                result.add(list.substring(0,index));
                list = list.substring(index+1,list.length());
            } else {
                result.add(list);
                list = "";
            }
        }
        return result;
    }

    public static java.util.List<Integer> stringToIntegerList(String list) throws java.lang.IllegalArgumentException {
        if(list==null) { throw new java.lang.IllegalArgumentException("Error: List is null."); }
        java.util.List<Integer> result = new java.util.ArrayList();
        int index = -1;
        while(list.length()>0) {
            index = list.indexOf(',');
            if(index>0) {
                int cur = Integer.parseInt(list.substring(0,index));
                result.add(cur);
                list = list.substring(index+1,list.length());
            } else {
                int cur = Integer.parseInt(list.substring(0,index));
                result.add(cur);
                list = "";
            }
        }
        return result;
    }
    
    public static float round(float d, int decimalPlace){
        BigDecimal result = new BigDecimal(Float.toString(d));
        result = result.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return result.floatValue();
    }
    
    public static void print(String[] arr){
        for(String str : arr){
            System.out.println(str);
        }
    }
    
    public static java.io.File getFileFromStream(java.io.InputStream in) throws java.io.IOException{
        final java.io.File result = java.io.File.createTempFile("GeneratedUtilityFile" + _fileNameCount, ".properties");
        _fileNameCount++;
        result.deleteOnExit();
        java.io.FileOutputStream out = new java.io.FileOutputStream(result);
        org.apache.commons.io.IOUtils.copy(in, out);
        return result;
    }
    
    public static String readFile(String path) throws java.io.IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }
    
    public static String readFile(String path, Charset encoding) throws java.io.IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    public static byte[] Zip(String in) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream(in.length());
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(in.getBytes());
        gzip.close();
        out.close();
        return out.toByteArray();
    }
    
    public static String Unzip(byte[] in) throws IOException{
        ByteArrayInputStream is = new ByteArrayInputStream(in);
        GZIPInputStream gis = new GZIPInputStream(is);
        InputStreamReader reader = new InputStreamReader(gis);
        BufferedReader br = new BufferedReader(reader);
        
        StringBuilder result = new StringBuilder();
        String currentLine;
        while((currentLine = br.readLine()) != null) {
            result.append(currentLine);
        }
        is.close();
        gis.close();
        reader.close();
        br.close();
        return result.toString();
    }
    
    public static byte[] UnzipB(byte[] in) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream is = new ByteArrayInputStream(in);
        GZIPInputStream gis = new GZIPInputStream(is);
        byte[] buffer = new byte[256];
        int n;
        while((n = gis.read(buffer)) >= 0){
            os.write(buffer,0,n);
        }
        gis.close();
        //is.close();
        return os.toByteArray();
    }
}
