package com.medcisive.utility;

/**
 *
 * @author vhapalchambj
 */
public class NoteBuilder extends HashTree {

    private transient java.util.Map<String, String> noteBlockMap = new java.util.LinkedHashMap();
    private transient String commendDataKey = "COMMEND DATA:";
    private transient String editComment =
            "********************************************************************************\n"
            + "*** Note: There are multiple sections in this progress note saved by COMMEND. **\n"
            + "*** Each section starts with a section name followed by a colon and ends with **\n"
            + "*** the terminating symbol [*]. Please limit manual edits in each section to  **\n"
            + "*** the content between the colon and terminating symbol.                     **\n"
            + "********************************************************************************\n";
    private transient String footer =
            "********************************************************************************\n"
            + "*** Note: Please do not manually edit any text in the COMMEND DATA section.   **\n"
            + "*** Its special format enables further automated data analysis.               **\n"
            + "********************************************************************************\n";
    private transient String blockTerminator = "[*]";
    public  transient java.sql.Timestamp encounterDate;
    public  transient java.sql.Timestamp noteDate;
    private transient String rawInputText;
    private transient static final java.util.List<String> blockList = new java.util.ArrayList();
    static {
        blockList.add("Principal Mental Health Provider:");
        blockList.add("Mental Health Treatment Coordinator:");
        blockList.add("INFORMATION:");
        blockList.add("Veteran's chart");
        blockList.add("Session focused on the client's concerns including:");
        blockList.add("CURRENT SUBSTANCE USE:");
        blockList.add("SUICIDAL/HOMICIDAL RISK:");
        blockList.add("MEDICATION MANAGEMENT:");
        blockList.add("ACTIVE PROBLEM LIST:");
        blockList.add("SIDE EFFECTS:");
        blockList.add("SIDE EFFECTS MEASUREMENTS:");
        blockList.add("LABS:");
        blockList.add("WEIGHT:");
        blockList.add("MENTAL STATUS EXAM:");
        blockList.add("OUTCOME MEASUREMENTS:");
        blockList.add("IMPRESSION:");
        blockList.add("ASSESSMENT:");
        blockList.add("DIAGNOSIS:");
        blockList.add("PLAN:");
        blockList.add("SEEN BY ATTENDING PHYSICIAN:");
        blockList.add("LPS CONSERVED:");
        blockList.add("Client states that");
        blockList.add("Medication refills");
        blockList.add("EDUCATION:");
        blockList.add("TOBACCO SCREEN:");
        blockList.add("OUTCOME NOTE:");
        blockList.add("COMMEND DATA:");
    }

    public NoteBuilder() { // Build note up on client raw
        super("");
    }

    public NoteBuilder(String note) { // Build note up on server with CPRS note text
        this();
        if(note==null) { return; }
        rawInputText = note;
        for (String key : blockList) {
            extractTextBlock(key);
        }
        if (noteBlockMap.containsKey(commendDataKey)) {
            add(noteBlockMap.get(commendDataKey));
        }
        extractDateTime();
    }

    public void addData(String data) {
        if(data==null) { return; }
        add(data);
        insertBlock(commendDataKey, " \n" + getTreeStr());
    }

    private void extractTextBlock(String key) {
        if(key==null || rawInputText==null) { return; }
        int index = rawInputText.indexOf(key);
        if (index >= 0) {
            String block = rawInputText.substring(index + key.length());
            index = block.indexOf(blockTerminator);
            if (index > 0) {
                block = block.substring(0, index);
                //block = _trim(block);
                insertBlock(key, block);
            }
        }
    }

    public void extractDateTime() {
        String noteDateString = getRawDateTime();
        //DATE OF NOTE: SEP 07, 2010@13:47:48  ENTRY DATE
        String dateTime = correctTimeFormat(get("Encounter"));
        if ((dateTime == null) || dateTime.isEmpty()) {
            dateTime = noteDateString;
        }
        try {
            java.text.DateFormat formatter = new java.text.SimpleDateFormat("MMM dd, yyyy@hh:mm:ss");
            java.util.Date dateUtil = (java.util.Date) formatter.parse(dateTime);
            java.util.Date noteDateUtil = (java.util.Date) formatter.parse(noteDateString);
            this.encounterDate = new java.sql.Timestamp(dateUtil.getTime());
            this.noteDate = new java.sql.Timestamp(noteDateUtil.getTime());
        } catch (Exception e) {
            LogUtility.error(e);
            //System.out.println("noteDateString: " + noteDateString + " ~~~~~~~ dateTime: " + dateTime + "\nException: ExtractDateTime - " + e);
        }
    }

    public String getNoteInfo() {
        if(rawInputText==null) { return ""; }
        String info = "";
        String endMarker = "STANDARD TITLE:";
        int endIndex = rawInputText.indexOf(endMarker);
        if (endIndex > 0) {
            info = rawInputText.substring(0, endIndex - 1) + "\n";
            info += "    Raw  Date: " + getRawDateTime() + "\n";
            info += "    Date Save: " + noteDate + "\n";
            info += "    Enc  Date: " + encounterDate;
        }
        return info;
    }

    private String getRawDateTime() {
        if(rawInputText==null) { return ""; }
        String start = "DATE OF NOTE:";
        String end = "ENTRY DATE";
        String dateString = "";
        int index = rawInputText.indexOf(start);
        if (index > 0) {
            dateString = rawInputText.substring(index + start.length());
            index = dateString.indexOf(end);
            if (index > 0) {
                dateString = dateString.substring(0, index);
            }
        }
        dateString = dateString.trim();
        int count = dateString.replaceAll("[^:]", "").length();
        if (count == 0) {
            dateString += "@00:00:00";
        } else if (count == 1) {
            dateString += ":00";
        }
        return dateString;
    }

    public String getRawProviderName() {
        if(rawInputText==null) { return ""; }
        String start = "AUTHOR:";
        String end = "EXP COSIGNER:";
        String providerName = "";
        int index = rawInputText.indexOf(start);
        if (index > 0) {
            providerName = rawInputText.substring(index + start.length());
            index = providerName.indexOf(end);
            if (index > 0) {
                providerName = providerName.substring(0, index);
            }
        }
        return providerName.trim();
    }

    private String correctTimeFormat(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int count = str.replaceAll("[^:]", "").length();
        if (count == 1) {
            str += ":00";
        }
        return str;
    }

    public String parseSingleDigitNumber(String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c == '0') || (c == '1') || (c == '2')
                    || (c == '3') || (c == '4') || (c == '5')
                    || (c == '6') || (c == '7') || (c == '8') || (c == '9')) {
                return "" + c;
            }
        }
        return null;
    }

    public String parseFirstNumber(String str) {
        if (str == null) {
            return null;
        }
        boolean start = false;
        boolean end = false;
        String number = "";
        for (char c : str.toCharArray()) {
            if (isNumberChar(c)) {
                if (!start && !end) {
                    number += c;
                    start = true;
                } else if (!end) {
                    number += c;
                }
            } else {
                if (start) {
                    end = true;
                }
            }
        }
        if (!number.equalsIgnoreCase("")) {
            return number;
        }
        return null;
    }

    private boolean isNumberChar(char c) {
        if (c == '\u0000') {
            return false;
        }
        if ((c == '0') || (c == '1') || (c == '2')
                || (c == '3') || (c == '4') || (c == '5')
                || (c == '6') || (c == '7') || (c == '8') || (c == '9')) {
            return true;
        }
        return false;
    }

    public java.util.Collection parseDelimited(String str) {
        java.util.ArrayList<String> list = new java.util.ArrayList();
        String block = "";
        if (str != null) {
            while (str.length() > 0) {
                int index = str.indexOf(resultSeparator);
                if (index > 0) {
                    try {
                        block = removeLeadingWhiteSpace(str.substring(0, index));
                        str = str.substring(index + 1, str.length());
                        list.add(block);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Error: commaIndexBlock");
                    }
                } else if (str.length() > 0) {
                    list.add(removeLeadingWhiteSpace(str));
                    str = "";
                } else {
                    break;
                }
            }
        }
        return list;
    }

    private String removeLeadingWhiteSpace(String str) {
        if (str == null) {
            return null;
        }
        if ((str.charAt(0) == ' ') && (str.length() > 0)) {
            return removeLeadingWhiteSpace(str.substring(1, str.length()));
        }
        return str;
    }

    public String removeTrailingWhiteSpace(String str) {
        if (str == null) {
            return null;
        }
        if (str.charAt(str.length() - 1) == ' ' && str.length() > 0) {
            return removeLeadingWhiteSpace(str.substring(0, str.length() - 1));
        }
        return str;
    }

    public String sideEffectComment(String str) {
        if (str != null) {
            int begin = str.indexOf('(');
            int end = str.indexOf(')');
            if ((begin > 0) && (end > 0)) {
                return str.substring(begin, end + 1);
            }
        }
        return "";
    }

    private String commaIndexBlock(String str) {
        int index = str.indexOf(',');
        String block = "";
        try {
            block = str.substring(0, index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: commaIndexBlock");
        }
        return block;
    }

    public void insertBlock(String key, String value) {
        if (value != null) {
            noteBlockMap.put(key, value);
        }
    }

    public String getBlock(String key) {
        String result = noteBlockMap.get(key);
        if(result==null) {
            result = "";
        }
        return result;
    }

    public void removeBlock(String key) {
        noteBlockMap.remove(key);
    }

    public java.sql.Timestamp getDate() {
        return this.encounterDate;
    }

    public void print() {
        for (String key : noteBlockMap.keySet()) {
            System.out.println("Key: " + key + "\nBlock Data: \n" + noteBlockMap.get(key));
        }
    }

    private String endBlock() {
        String str = "";
        int nubmerOfSpaces = 80 - blockTerminator.length();
        for (int i = 0; i < nubmerOfSpaces; i++) {
            str += " ";
        }
        str += blockTerminator + "\n";
        return str;
    }

    public java.util.Map<String, String> getNoteBlocks() {
        return noteBlockMap;
    }

    public String getRawText() {
        return this.rawInputText;
    }

    public String toMDWSString() {
        return formatText(toString());
    }

    private String formatText(String text) {
        boolean done = false;
        while (!done) {
            if (text != null) {
                int index = text.indexOf('\n');
                if (index > 0) {
                    String a = text.substring(0, index);
                    String b = text.substring(index + 1, text.length());
                    text = a + "|" + b;
                } else {
                    done = true;
                }
            } else {
                done = true;
            }
        }
        return text;
    }

    @Override
    public String toString() {
        String returnStr = "";
        for (String key : noteBlockMap.keySet()) {
            if (key.equalsIgnoreCase(commendDataKey)) {
                returnStr += editComment;
            }
            returnStr += key + noteBlockMap.get(key) + "\n" + endBlock();
        }
        returnStr += footer;
        return returnStr;
    }

    public String clipHead(String head, String str) {
        if(head==null || str==null) { return null; }
        String result = str;
        int index = result.indexOf(head);
        if(index>0) {
            result = result.substring(index + head.length());
        }
        return result;
    }

    public String clipTail(String tail, String str) {
        if(tail==null || str==null) { return null; }
        String result = str;
        int index = result.indexOf(tail);
        if(index>0) {
            result = result.substring(0,index);
        }
        return result;
    }

    public String getBlockSubSection(String blockKey, String start, int startOffset, String end, int endOffset) {
        boolean isEndString = true;
        if (end.equalsIgnoreCase(blockTerminator)) { // if this is true goto the end of the string!
            isEndString = false;
        }
        String block = getBlock(blockKey);
        if (!block.isEmpty()) {
            int index = 0;
            if (!start.equalsIgnoreCase("")) {
                index = block.indexOf(start) + startOffset;
            }
            if (index >= 0 && index < block.length()) {
                block = block.substring(index + start.length());
                if (isEndString) {
                    index = block.indexOf(end) + endOffset;
                    if (index >= 0 && index < block.length()) {
                        block = block.substring(0, index);
                    }
                }
            }
        }
        return block;
    }

    public String _trim(String str) {
        String result = "";
        char[] carr = str.toCharArray();
        int start = 0;
        int end = 0;
        for(char c : carr) {
            if(c!=' ') {
                break;
            } else {
                start++;
            }
        }
        for(int i = carr.length-1 ; i > 0; i--) {
            if(carr[i]!=' ') {
                break;
            } else {
                end = i;
            }
        }
        if(start>0 && end >0) {
            result = str.substring(start,end);
        }
        return result;
    }

    private String removeNewline(String str) {
        return str.replaceAll("\n", " ");
    }
}