package com.spider.core.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String  DATE_FORMAT="yyyy-MM-dd";
    public static final String MSEC_FORMAT="yyyyMMddHHmmssSSS";
    private static final int FIELDS[]={24,60,1000};
    public static String subDateStr(Date date,String other,String fromat,DateUnitEnum unit){
        long result=subDate(date,convertStr2Date(other,fromat),unit);
        return String.valueOf(result);
    }
    public static long subDate(Date date,Date other,DateUnitEnum unit){
        return convertMesc2Unit(date.getTime()-other.getTime(),unit);
    }
    private static long convertMesc2Unit(long unitDigit,DateUnitEnum unit){

        switch (unit){
            case DAY:
                unitDigit=unitDigit/FIELDS[0]/FIELDS[1]/FIELDS[1]/FIELDS[2];
                break;
            case HOUR:
                unitDigit=unitDigit/FIELDS[1]/FIELDS[1]/FIELDS[2];
                break;
            case MINUTE:
                unitDigit=unitDigit/FIELDS[1]/FIELDS[2];
                break;
            case SECOND:
                unitDigit=unitDigit/FIELDS[2];
                break;
            case MESC:
                break;
        }
        return unitDigit;
    }
    public static Date addDate(Date date,long offset){
        return new Date(date.getTime()+offset);
    }
    private static String getDateStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *  get default time with default fromat
     * @return The default format is yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateStr() {
        return getDateStr(new Date(), DATETIME_FORMAT);
    }

    /**
     *
     * @param format
     * @return Returns the current time in the specified format
     */
    public static String getCurrentDateStr(String format) {
        return getDateStr(new Date(), format);
    }

    public  static Date getCurrentDate(String format){
        return convertStr2Date(getCurrentDateStr(format),format);
    }
    /**
     *
     * @param str
     * @param format
     * @return
     */
    public static Date convertStr2Date(String str, String format) {
        String pattern = format == null ? DATETIME_FORMAT : format;
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     *
     * @param date
     * @param format
     * @return return date string
     */
    public static String convertDate2Str(Date date,String format){
        if(date==null){
            return null;
        }
        String pattern = format == null ? DATETIME_FORMAT : format;
        return getDateStr(date,pattern);
    }

}
