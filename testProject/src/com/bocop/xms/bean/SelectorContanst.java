package com.bocop.xms.bean;

import java.util.ArrayList;

import android.R.integer;

/**
 * Created by santa on 16/7/19.
 */
public class SelectorContanst {
    private static SelectorContanst instance = null;
    public static ArrayList<String> MONTHS = null;
    public static ArrayList<String> YEARS = null;
    public static ArrayList<String> DAYS = null;
    public static ArrayList<String> HOURS = null;
    public static ArrayList<String> MINS = null;
    private int daysOfMonth = 0;      //某月的天数


    public ArrayList<String> getMonths() {
        if (null == MONTHS) {
            synchronized (SelectorContanst.class){
                MONTHS = new ArrayList<>();
                for (int i = 1 ; i<=12; i++) {
                    MONTHS.add((i<10?"0"+i:i)+"月");
                }
            }
        }
        return MONTHS;
    }


    public ArrayList<String> getYears() {
        if (null == YEARS) {
            synchronized (SelectorContanst.class){
                YEARS = new ArrayList<>();
                for (int i = 1900 ; i<=3000; i++) {
                    YEARS.add(i+"年");
                }
            }
        }
        return YEARS;
    }


    public  ArrayList<String> getDays(int year,int month) {
    	boolean flag=isLeapYear(year);
            synchronized (SelectorContanst.class){
                DAYS = new ArrayList<>();
                for (int i = 1 ; i<=getDaysOfMonth(flag, month); i++) {
                    DAYS.add((i<10?"0"+i:i)+"日");
                }
            
        }
        System.out.println("-----------size"+DAYS.size());
        return DAYS;
    }


    public  ArrayList<String> getHours() {
        if (null == HOURS) {
            synchronized (SelectorContanst.class){
                HOURS = new ArrayList<>();
                for (int i = 0 ; i<=23; i++) {
                    HOURS.add((i<10?"0"+i:i)+"时");
                }
            }
        }
        return HOURS;
    }


    public  ArrayList<String> getMins() {
        if (null == MINS) {
            synchronized (SelectorContanst.class){
                MINS = new ArrayList<>();
                for (int i = 0 ; i<=59; i++) {
                    MINS.add((i<10?"0"+i:i)+"分");
                }
            }
        }
        return MINS;
    }
    

	// 判断是否为闰年
	private boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}

	//得到某月有多少天数
	private  int getDaysOfMonth(boolean isLeapyear, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysOfMonth = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daysOfMonth = 30;
			break;
		case 2:
			if (isLeapyear) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}
	

}
