/**
 * 
 */
package com.bocop.xms.utils;
/** 
 * @author luoyang  E-mail: luoyang8714@163.com
 * @version 创建时间：2016-12-9 下午5:56:40 
 * 类说明 
 */
/**
 * @author zhongye
 *
 */
import java.text.SimpleDateFormat;   
import java.util.Calendar;   
import java.util.Date;   
import java.util.GregorianCalendar;   
import java.util.HashMap;   
import java.util.Map;   
import java.util.Set;   

import com.boc.jx.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
  
  
public class IdcardInfoExtractor {   
    // 省份   
    private String province;   
    // 城市   
    private String city;   
    // 区县   
    private String region;   
    // 年份   
    private int year;   
    // 月份   
    private int month;   
    // 日期   
    private int day;   
    
    // 年份   
    private int curYear;   
    // 月份   
    private int curMonth;   
    // 日期   
    private int curDay;  
    
    // 性别   
    private String gender;   
    // 出生日期   
    private Date birthday;   
    
    //判断当天是不是生日
    private Boolean isBirthday;
  
    private Map<String, String> cityCodeMap = new HashMap<String, String>() {   
        {   
            this.put("11", "北京");   
            this.put("12", "天津");   
            this.put("13", "河北");   
            this.put("14", "山西");   
            this.put("15", "内蒙古");   
            this.put("21", "辽宁");   
            this.put("22", "吉林");   
            this.put("23", "黑龙江");   
            this.put("31", "上海");   
            this.put("32", "江苏");   
            this.put("33", "浙江");   
            this.put("34", "安徽");   
            this.put("35", "福建");   
            this.put("36", "江西");   
            this.put("37", "山东");   
            this.put("41", "河南");   
            this.put("42", "湖北");   
            this.put("43", "湖南");   
            this.put("44", "广东");   
            this.put("45", "广西");   
            this.put("46", "海南");   
            this.put("50", "重庆");   
            this.put("51", "四川");   
            this.put("52", "贵州");   
            this.put("53", "云南");   
            this.put("54", "西藏");   
            this.put("61", "陕西");   
            this.put("62", "甘肃");   
            this.put("63", "青海");   
            this.put("64", "宁夏");   
            this.put("65", "新疆");   
            this.put("71", "台湾");   
            this.put("81", "香港");   
            this.put("82", "澳门");   
            this.put("91", "国外");   
        }   
    };   
       
    private IdcardValidator validator = null;   
       
      
    public IdcardInfoExtractor(String idcard) {   
        try {   
            validator = new IdcardValidator();   
            if (validator.isValidatedAllIdcard(idcard)) {   
                if (idcard.length() == 15) {   
                    idcard = validator.convertIdcarBy15bit(idcard);   
                }   
                // 获取省份   
                String provinceId = idcard.substring(0, 2);   
                Set<String> key = this.cityCodeMap.keySet();   
                for (String id : key) {   
                    if (id.equals(provinceId)) {   
                        this.province = this.cityCodeMap.get(id);   
                        break;   
                    }   
                }   
  
                // 获取性别   
                String id17 = idcard.substring(16, 17);   
                if (Integer.parseInt(id17) % 2 != 0) {   
                    this.gender = "男";   
                } else {   
                    this.gender = "女";   
                }   
  
//                idcard = "362202198712191234";
                // 获取出生日期   
                String birthday = idcard.substring(6, 14);   
                Date birthdate = new SimpleDateFormat("yyyyMMdd")   
                        .parse(birthday);   
                this.birthday = birthdate;   
                GregorianCalendar currentDay = new GregorianCalendar();   
                currentDay.setTime(birthdate);   
                this.year = currentDay.get(Calendar.YEAR);   
                this.month = currentDay.get(Calendar.MONTH) + 1;   
                this.day = currentDay.get(Calendar.DAY_OF_MONTH);   
                
                //获取当天日期
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
    			String str = formatter.format(curDate);
    			currentDay.setTime(curDate);   
    			Log.i("tag", "当天日期：" + curDate );
    			this.curYear = currentDay.get(Calendar.YEAR);   
                this.curMonth = currentDay.get(Calendar.MONTH) + 1;   
                this.curDay = currentDay.get(Calendar.DAY_OF_MONTH);  
    			Log.i("tag", "生日，年：" + this.year + "月：" + this.month +  "日:" + this.day );
    			Log.i("tag", "当天，年：" + this.curYear + "月：" + this.curMonth +  "日:" + this.curDay );
    			
    			if(this.month == this.curMonth && this.day == this.curDay){
    				this.isBirthday = true;
    			}else{
    				this.isBirthday = false;
    			}
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }   
  
      
    public String getProvince() {   
        return province;   
    }   
  
      
    public String getCity() {   
        return city;   
    }   
  
      
    public String getRegion() {   
        return region;   
    }   
  
      
    public int getYear() {   
        return year;   
    }   
  
      
    public int getMonth() {   
        return month;   
    }   
  
      
    public int getDay() {   
        return day;   
    }   
  
      
    public String getGender() {   
        return gender;   
    }   
  
      
    public Date getBirthday() {   
        return birthday;   
    }   
  
    
    
    public int getCurYear() {
		return curYear;
	}


	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}


	public int getCurMonth() {
		return curMonth;
	}


	public void setCurMonth(int curMonth) {
		this.curMonth = curMonth;
	}


	public int getCurDay() {
		return curDay;
	}


	public void setCurDay(int curDay) {
		this.curDay = curDay;
	}

	

	public Boolean getIsBirthday() {
		return isBirthday;
	}


	public void setIsBirthday(Boolean isBirthday) {
		this.isBirthday = isBirthday;
	}


	@Override  
    public String toString() {   
        return "省份：" + this.province + ",性别：" + this.gender + ",出生日期："  
                + this.birthday;   
    }   
  
    public static void main(String[] args) {   
        String idcard = "";   
        IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);   
        System.out.println(ie.toString());   
    }   
}  
