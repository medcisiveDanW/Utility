package com.medcisive.utility.protegecopse;

import java.io.*;

public class DeviceData implements Serializable {
    private String m_device;
    private TextSpan m_deviceSpan;
    private String m_devFragPlural;
    private TextSpan m_devFragPluralSpan;
    private String m_devStatus;
    private TextSpan m_devStatusSpan;
    private String m_statusInference;
    private String m_numerality;
    private TextSpan m_numeralitySpan;
    private Integer m_numeralityInf;
    private String m_laterality;
    private TextSpan m_laterSpan;

    public DeviceData(String dev, TextSpan devSpan,
            String devStatus, TextSpan statusSpan, String inf) {
        m_device = dev;
        m_deviceSpan = devSpan;
        m_devStatus = devStatus;
        m_devStatusSpan = statusSpan;
        m_statusInference = inf;
    }

    public DeviceData(String dev, TextSpan devSpan,
            String devStatus, TextSpan statusSpan, String inf,
            String laterality, TextSpan lateralitySpan) {
        this(dev, devSpan, devStatus, statusSpan, inf);
        m_laterality = laterality;
        m_laterSpan = lateralitySpan;

    }

    public DeviceData(String dev, TextSpan devSpan,
            String devStatus, TextSpan statusSpan, String inf,
            String numerality, TextSpan numeralitySpan, Integer numeralityInf,
            String laterality, TextSpan lateralitySpan) {

        this(dev, devSpan, devStatus, statusSpan, inf, laterality, lateralitySpan);
        m_numerality = numerality;
        m_numeralitySpan = numeralitySpan;
        m_numeralityInf = numeralityInf;
    }

    public DeviceData(String dev, TextSpan devSpan,
            String devFragPlural, TextSpan devFragPluralSpan,
            String devStatus, TextSpan statusSpan, String inf,
            String numerality, TextSpan numeralitySpan, Integer numeralityInf,
            String laterality, TextSpan lateralitySpan) {
        this(dev, devSpan,
                devStatus, statusSpan, inf,
                numerality, numeralitySpan, numeralityInf,
                laterality, lateralitySpan);
        m_devFragPlural = devFragPlural;
        m_devFragPluralSpan = devFragPluralSpan;
    }

    public void setDevice(String str) {
        m_device = str;
    }

    public String getDevice() {
        return m_device;
    }

    public TextSpan getDeviceSpan() {
        return m_deviceSpan;
    }

    public void setDevFragPlural(String str) {
        m_devFragPlural = str;
    }

    public String getDevFragPlural() {
        return m_devFragPlural;
    }

    public TextSpan getDevFragPluralSpan() {
        return m_devFragPluralSpan;
    }

    public void setDevStatus(String str) {
        m_devStatus = str;
    }

    public String getDevStatus() {
        return m_devStatus;
    }

    public TextSpan getDevStatusSpan() {
        return m_devStatusSpan;
    }

    public void setStatusInference(String str) {
        m_statusInference = str;
    }

    public String getStatusInference() {
        return m_statusInference;
    }

    public void setNumerality(String str) {
        m_numerality = str;
    }

    public String getNumerality() {
        return m_numerality;
    }

    public void setNumeralitySpan(TextSpan span) {
        m_numeralitySpan = span;
    }

    public TextSpan getNumeralitySpan() {
        return m_numeralitySpan;
    }

    public void setNumeralityInf(Integer anInt) {
        m_numeralityInf = anInt;
    }

    public Integer getNumeralityInf() {
        return m_numeralityInf;
    }

    public void setLaterality(String str) {
        m_laterality = str;
    }

    public String getLaterality() {
        return m_laterality;
    }

    public void setLaterSpan(TextSpan span) {
        m_laterSpan = span;
    }

    public TextSpan getLaterSpan() {
        return m_laterSpan;
    }

    public void print() {
        System.out.println("Device: " + m_device + " " + m_deviceSpan.toString());
        if (m_devFragPlural != null) {
            System.out.println("     FragPlural: " + m_devFragPlural.toString() + " " + m_devFragPluralSpan.toString());
        }
        if (m_devStatus != null) {
            System.out.println("     Status: " + m_devStatus + " " + m_devStatusSpan.toString() + " Inf: " + m_statusInference);
        }
        if ((m_numerality != null) && (m_numeralitySpan != null)) {
            System.out.println("     Numerality: " + m_numerality + " " + m_numeralitySpan.toString() + " Inf: " + m_numeralityInf);
        }
        if (m_laterality != null) {
            System.out.println("     Laterality: " + m_laterality + " " + m_laterSpan.toString());
        }
    }
}