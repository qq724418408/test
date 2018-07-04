package com.boc.jx.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;

/**
 * 日历工具类
 *
 * @author kejia
 *
 */
@SuppressLint("SimpleDateFormat")
public class CalendarUtils {

	public final static String YEAR = "年";
	public final static String MONTH = "月";
	public final static String DAY = "日";
	public final static String[] WEEKDAY = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
	
	/**
	 * 获取系统日期
	 * 
	 * @return List<Integer>
	 */
	public List<Integer> getSystemDate() {
		Date date = new Date();
		List<Integer> dateList = convertDateStrToInt(dateFormat.format(date));   	

		return dateList;
	}

	/**
	 * 获取系统时间
	 * 
	 * @return List<Integer>
	 */
	public List<Integer> getSystemTime() {
		Date date = new Date();
		List<Integer> timeList = convertDateStrToInt(timeFormat.format(date)); 
		
		return timeList;
	}
	
	/**
	 * 转换日期字符串为整型列表
	 * 
	 * @param dateStr
	 * @return List<Integer>
	 */
	public List<Integer> convertDateStrToInt(String dateStr) {
		List<Integer> dateList = new ArrayList<Integer>();
    	dateList.add(Integer.parseInt(dateStr.split("-")[0]));
    	dateList.add(Integer.parseInt(dateStr.split("-")[1]));
    	dateList.add(Integer.parseInt(dateStr.split("-")[2]));  
    	
    	return dateList;
	}
	
	/**
	 * 获取时间字符串（自动补零）
	 * 
	 * @param hour
	 * @param min
	 * @return String
	 */
	public static String getTimeStr(int hour, int min) {
		String str = "";
		if (hour < 10) {
			str = "0";
		} 
		str = str + hour + ":";
		if (min < 10) {
			str = str + "0";
		}
		str = str + min;
		
		return str;		
	}
	
	/**
	 * 获取日期字符串（年月，如“2013年10月”）
	 * 
	 * @param year
	 * @param month
	 * @return String
	 */
	public static String getDateStr(int year, int month) {
		if (month < 10) {
			return year + YEAR + "0" + month + MONTH;
		} else {
			return year + YEAR + month + MONTH;
		}	
	}

	/**
	 * 获取日期字符串（年月日，如“2013年10月1日”）
	 * 
	 * @param year
	 * @param month
	 * @return String
	 */
	public static String getDateStr(int year, int month, int day) {
		return year + YEAR + month + MONTH + day + DAY;
	}
	
	/**
	 * 获取带分隔符的日期字符串（年月，如“2013-10”）
	 * 
	 * @param year
	 * @param month
	 * @return String
	 */
	public static String getDateWithSeparatorStr(int year, int month) {
		String str = year + "-";
		if (month < 10) {
			str = str + "0";
		} 
		str = str + month;	
		
		return str;
	}
	
	/**
	 * 获取带分隔符的日期字符串（年月日，如“2013-10-01”）
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return String
	 */
	public static String getDateWithSeparatorStr(int year, int month, int day) {
		String str = year + "-";
		if (month < 10) {
			str = str + "0";
		} 
		str = str + month + "-";
		if (day < 10) {
			str = str + "0";
		}
		str = str + day;
		
		return str;
	}

	/**
	 * 获取某月第一天及最后一天的日期字符串（年月日，如{"2013-10-01", "2013-10-31"}）
	 * 
	 * @param year
	 * @param month
	 * @return String[]
	 */
	public static String[] getSpecialDaysOfMonth(int year, int month) {
		String[] str = new String[2];
		str[0] = year + "-";
		if (month < 10) {
			str[0] = str[0] + "0";
		} 
		str[0] = str[0] + month;	
		str[1] = str[0];
		str[0] = str[0] + "-01";	
		str[1] = str[1] + "-" + getDaysOfMonth(isLeapYear(year), month);
			
		return str;
	}
	
	/**
	 * 获取当前一周的所有日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param middlePosition 当天在一周里的位置
	 * @return List<List<String>>
	 */
	public List<List<String>> getCurrentWeek(int year, int month, int day, int middlePosition) {
		if (middlePosition < 0 || middlePosition > 6) {
			return null;
		}
		List<List<String>> weekDates = new ArrayList<List<String>>();
		List<String> weeks = new ArrayList<String>();	
		List<String> dates = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		for (int i=0; i<7; i++) {
			if (i == 0) {
				cal.add(Calendar.DAY_OF_YEAR, (i - middlePosition));
			} else {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			String week = WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];			
			String date = dateFormat.format(cal.getTime());
			
			weeks.add(week);
			dates.add(date);		
		}
		weekDates.add(weeks);
		weekDates.add(dates);
		
		return weekDates;
	}
	
	/**
	 * 获取上/下一周的所有日期
	 * 
	 * @param isLast
	 * @param year
	 * @param month
	 * @param day
	 * @param middlePosition
	 * @return List<List<String>>
	 */
	public List<List<String>> getOtherWeek(boolean isPrev, int year, int month, int day, int middlePosition) {
		if (middlePosition < 0 || middlePosition > 6) {
			return null;
		}
		List<List<String>> weekDates = new ArrayList<List<String>>();
		List<String> weeks = new ArrayList<String>();	
		List<String> dates = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		if (isPrev) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		} else {
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}

		for (int i=0; i<7; i++) {
			if (i == 0) {
				cal.add(Calendar.DAY_OF_YEAR, (i - middlePosition));
			} else {
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			String week = WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];			
			String date = dateFormat.format(cal.getTime());
			
			weeks.add(week);
			dates.add(date);		
		}
		weekDates.add(weeks);
		weekDates.add(dates);
		
		return weekDates;		
	}

	/**
	 * 获取未来一周的日期
	 */
	public static String[] getFutureWeek() {
		String[] dates = new String[7];
		Calendar cal = Calendar.getInstance();
		String w = WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];	
		String d = dateFormat.format(cal.getTime());
		
		dates[0] = d + "（" + w + "）";
		
		for (int i=0; i<6; i++) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			
			w = WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];			
			d = dateFormat.format(cal.getTime());
			
			dates[i + 1] = d + "（" + w + "）";	
		}
		
		return dates;		
	}
	
	/**
	 * 获取星期
	 */
	public static String getWeekDay(String date) {
		if (date == null || !date.contains("-"))	{
			return "";
		}
		String[] dates = date.split("-");
		if (dates.length < 3) {
			return "";
		}
		int year = Integer.valueOf(dates[0]);
		int month = Integer.valueOf(dates[1]);
		int day = Integer.valueOf(dates[2]);
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		
		return WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];		
	}
	
	/**
	 * 获取一个月的所有日期
	 * 
	 * @param year
	 * @param month
	 * @return List<List<String>>
	 */
	public List<List<String>> getDaysOfMonth(int year, int month) {
		List<List<String>> daysList = new ArrayList<List<String>>();
		List<String> weeks = new ArrayList<String>();	
		List<String> dates = new ArrayList<String>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);

		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); 
		cal.add(Calendar.DAY_OF_YEAR, 1 - dayOfWeek);
		for (int i=0; i<35; i++) {			
			String week = WEEKDAY[cal.get(Calendar.DAY_OF_WEEK) - 1];			
			String date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
			weeks.add(week);
			dates.add(date);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		daysList.add(weeks);
		daysList.add(dates);		
		
		return daysList;	
	}
	
	/**
	 * 获取上/下个月的日期（年月)
	 * 
	 * @param isLast
	 * @param year
	 * @param month
	 * @return List<Integer>
	 */
	public List<Integer> getOtherMonth(boolean isPrev, int year, int month) {
		List<Integer> otherMonth = new ArrayList<Integer>();
		if (isPrev) {
			month--;
			if (month < 1) {
				year--;
				month = 12;
			}
		} else {
			month++;			
			if (month > 12) {
				year++;
				month = 1;
			}			
		}
		otherMonth.add(year);
		otherMonth.add(month);
		
		return otherMonth;
	}
	
	/**
	 * getDatesWithApartDays(获取当前日期一定偏移天数的前后日期)
	 * 
	 * @param currentDate 当前日期
	 * @param apartDays 偏移天数
	 * @return String[]
	 */
	public String[] getDatesWithApartDays(String currentDate, int apartDays) {
		String[] dates = new String[2];
    	int year = Integer.parseInt(currentDate.split("-")[0]);
    	int month = Integer.parseInt(currentDate.split("-")[1]) - 1;
    	int day = Integer.parseInt(currentDate.split("-")[2]);  	
    	Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		cal.add(Calendar.DAY_OF_YEAR, -apartDays);
		dates[0] = dateFormat.format(cal.getTime());
		cal.add(Calendar.DAY_OF_YEAR, apartDays * 2);
		dates[1] = dateFormat.format(cal.getTime());
		
		return dates;
	}
	
	/**
	 * 是否为闰年
	 * 
	 * @param year
	 * @return true or false
	 */
	public static boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取该月的天数
	 * 
	 * @param isLeapyear
	 * @param month
	 * @return daysOfMonth
	 */
	public static int getDaysOfMonth(boolean isLeapyear, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (isLeapyear) {
				return 29;
			} else {
				return 28;
			}
		default:
			return 30;
		}
	}

	/**
	 * 某年某月的第一天是星期几
	 * 
	 * @param year
	 * @param month
	 * @return dayOfWeek
	 */
	public static int getWeekdayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
}
