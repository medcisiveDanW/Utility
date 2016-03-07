package com.medcisive.utility;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author vhapalchambj
 */
public class FileUtility {

    public static java.io.File get(String path) throws FileNotFoundException {
        if (path == null) {
            return null;
        }
        java.io.File result = null;
        // regex for abs path is "^.:\\\\.*"
        if (path.matches("^[a-zA-Z]*\\..*")) {
            result = searchFor(path);
        } else {
            result = new java.io.File(path);
            if(result==null) {
                throw new java.io.FileNotFoundException("Cannot find file " + path);
            }
        }
        return result;
    }

    public static String toString(java.io.File file) throws IOException {
        if (file == null) {
            return null;
        }
        return fileToString(file);
    }

    public static java.io.File searchFor(String fileName) throws FileNotFoundException {
        java.io.File currentFile = new java.io.File(".");
        java.io.File result = searchFor(fileName, currentFile);
        if(result==null) {
            throw new java.io.FileNotFoundException("Cannot find file " + fileName);
        }
        return result;
    }

    private static java.io.File searchFor(String fileName, java.io.File path) {
        if (fileName == null || path == null) {
            return null;
        }
        java.io.File result = null;
        java.io.File[] fileList = path.listFiles();
        for (java.io.File f : fileList) {
            String name = f.getName();
            if (name.toString().equalsIgnoreCase(fileName)) {
                return f.getAbsoluteFile();
            }
            if (f.isDirectory()) {
                java.io.File file = new java.io.File(f.getAbsolutePath());
                java.io.File temp = searchFor(fileName, file);
                if (temp != null) {
                    result = temp;
                }
            }
        }
        return result;
    }

    private static String fileToString(java.io.File file) throws IOException, FileNotFoundException {
        String result = null;
        java.io.FileInputStream stream = null;
        stream = new java.io.FileInputStream(file);
        java.nio.channels.FileChannel fc = stream.getChannel();
        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fc.size());
        stream.close();
        result = java.nio.charset.Charset.defaultCharset().decode(bb).toString();
        return result;
    }

    public static com.medcisive.utility.HashTree tabToHashTree(java.io.File file) {
        boolean firstPass = true;
        java.util.ArrayList<String> header = null;
        com.medcisive.utility.HashTree result = new com.medcisive.utility.HashTree();
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                if (firstPass) {
                    firstPass = false;
                    header = pullTokens(str);
                } else {
                    java.util.ArrayList<String> data = pullTokens(str);
                    for (int i = 1; i < data.size(); i++) {
                        String reportName = removePrefix(data.get(0));
                        result.add("[" + reportName + "=[" + header.get(i) + "=" + data.get(i) + "]]");
                    }
                }
            }
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String removePrefix(String str) {
        String result = null;
        int index = str.indexOf('.');
        if (index > 0) {
            result = str.substring(0, index);
        } else {
            result = str;
        }
        return result;
    }

    private static java.util.ArrayList<String> pullTokens(String line) {
        java.util.ArrayList<String> result = new java.util.ArrayList();
        try {
            java.util.Scanner scanner = new java.util.Scanner(line);
            while (scanner.hasNext()) {
                result.add(scanner.next());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int count(String str, char c) {
        int result = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i)==c) {
                result++;
            }
        }
        return result;
    }

    public static int tabCount(String str) {
        int result = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i)=='\t') {
                result++;
            }
        }
        return result;
    }

    public static int semicolonCount(String str) {
        int result = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i)==';') {
                result++;
            }
        }
        return result;
    }
}
