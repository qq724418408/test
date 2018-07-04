package com.bocop.xfjr.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.boc.jx.httptools.http.callback.IHttpCallback;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.XfjrMain;
import com.bocop.xfjr.activity.XfjrIndexActivity;
import com.bocop.xfjr.bean.login.LoginBean;
import com.bocop.xfjr.config.HttpRequest;
import com.bocop.xfjr.config.UrlConfig;
import com.bocop.xfjr.constant.XFJRConstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class XFJRUtil {

	public static String money = "";
	private static final int MIN_CLICK_DELAY_TIME = 500; // 单位毫秒
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }
	
	/**
	 * 数字%格式化
	 * 
	 * @param rate
	 * @return 0.00%
	 */
	public static String percentFormat(String rate) {
		NumberFormat nFormat = NumberFormat.getPercentInstance();
		nFormat.setMinimumFractionDigits(2);
		String percent = nFormat.format(Double.parseDouble(rate));
		return percent;
	}
	
	/**
	 * 在数字型字符串千分位加逗号
	 * 
	 * @param str
	 * @param edtext
	 * @return sb.toString()
	 */
//	public static String addComma(String str, EditText edtext) {
//
//		money = edtext.getText().toString().trim().replaceAll(",", "").replace("￥", "");
//		if (edtext.getText().toString().trim().endsWith(".")) {
//			money = edtext.getText().toString().trim().replaceAll(",", "").replace(".", "").replace("￥", "");
//		} 
//		boolean neg = false;
//		if (str.startsWith("-")) { // 处理负数
//			str = str.substring(1);
//			neg = true;
//		}
//		String tail = null;
//		if (str.indexOf('.') != -1) { // 处理小数点
//			tail = str.substring(str.indexOf('.'));
//			str = str.substring(0, str.indexOf('.'));
//		}
//		StringBuilder sb = new StringBuilder(str);
//		sb.reverse();
//		for (int i = 3; i < sb.length(); i += 4) {
//			sb.insert(i, ',');
//		}
//		sb.reverse();
//		if (neg) {
//			sb.insert(0, '-');
//		}
//		if (tail != null) {
//			sb.append(tail);
//		}
//		return sb.toString();
//	}
	
	/**
	 * 超过某个百分数返回true
	 * 
	 * @param percent
	 * @return
	 */
	public static boolean exceedPercent(String total, String current, double percent){
		double num1 = Double.parseDouble(total); // 总数
		double num2 = Double.parseDouble(current);
		double p = percent/100;
		double n3 = num1 * p;
		boolean b = num2 > n3;
		return b;
	}
	
	/**
	 * 隐藏输入软键盘
	 * 
	 * @param v
	 */
	public static void hideSoftInput(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	/**
	 * 暂时的自动登录方法
	 */
	public static void autoLogin(Context context) {
		HttpRequest.autoLogin(context, false, new IHttpCallback<LoginBean>() {
			
			@Override
			public void onSuccess(String url, LoginBean result) {
				XfjrMain.role = result.getRole();
				LogUtils.e("autoLogin onSuccess");
				if(result.getRole().equals("1")){
					PreferencesUtil.put(XFJRConstant.KEY_EHR_ID, result.getCustomerInfo().getEhr());
				}
			}
			
			@Override
			public void onFinal(String url) {
				
			}
			
			@Override
			public void onError(String url, Throwable e) {
				
			}
		});
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(long time) {
		Date dt = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}
	
	/**
	 * yyyy/MM/dd HH:mm
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(String reg) {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(reg);
		return sdf.format(dt);
	}
	
	/**
	 * yyyyMMddHHmmss
	 * 
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime() {
		Date dt = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(dt);
	}
	
	/**
	 * Date转String
	 * 
	 * @param dt
	 * @param reg
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String date2String(Date dt, String reg) {
		SimpleDateFormat sdf = new SimpleDateFormat(reg);
		return sdf.format(dt);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date string2Date(String date, String reg) {
		Date dt = new Date();
		try {
			dt = new SimpleDateFormat(reg).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt;
	}
	
	/**
	 * 比较两个日期大小
	 * 
	 * @param 如果date1大返回1
	 * @param 如果date2大返回-1
	 * @return 如果相等返回0
	 */
	public static int compareDate(String date1, String date2, String reg) {
		long time1 = string2Date(date1, reg).getTime();
		long time2 = string2Date(date2, reg).getTime();
		if (time1 > time2) {
			return 1;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * 获取网服务器时间(需要在子线程操作网络请求)
	 * 
	 * @param dt
	 * @param reg
	 * @return
	 */
	public static String getNetWorkDateString(String reg) {
		URL url;
		Date date = new Date();
		try {
			url = new URL(UrlConfig.SERVER);
			URLConnection uc = url.openConnection();
			uc.connect();
			long ld = uc.getDate(); //取得网站日期时间
			date = new Date(ld); //转换为标准时间对象
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return date2String(date,reg);
	}
	
	/**
	 * 根据身份证号码计算生日
	 * 
	 * @param IDCardNum
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static int calAgeByIDCard(String IDCardNum) {
		if(PatternUtils.is18ByteIdCardComplex(IDCardNum)){
			String birthday = IDCardNum.substring(6, 14);
			int age = getAge(birthday);
			return age;
		}
		return 0;
	}
	
	/**
	 * 根据身份证号码计算年龄上限
	 * 
	 * 申请人的年龄周岁应大于等于产品下限年龄
	 * 申请人年龄周岁 加 申请期限（期限需要转化为年（除以12））的和应小于产品上限。
	 * 
	 * @param IDCardNum
	 * @param periods
	 *            分期期数（单位：月）
	 * @return 年龄上限
	 */
	@SuppressLint("SimpleDateFormat")
	public static float calUpAgeByIDCard(String IDCardNum, String periods) {
		float m = 0;
		try {
			m = Float.parseFloat(periods);
		} catch (Exception e) {
			m = 0;
		}
		if(PatternUtils.is18ByteIdCardComplex(IDCardNum)){ // 452402 19920516
			String birthday = IDCardNum.substring(6, 14); // yyyyMMdd
			int age = getAge(birthday);
			return age + (m / 12);
		}
		return 0;
	}
	
	/**
	 * yyyyMMdd
	 * 周岁计算：
	 * 如果当前时间的月日小于出生时的月日
	 * 周岁=当前年份 - 出生年 - 1
	 * 当前时间的月日大于等于出生时的月日
	 * 周岁=当前年份 - 出生年
	 * @param dateOfBirth
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static int getAge(String birthday) {
		Date dateOfBirth = new Date();
		try {
			dateOfBirth = new SimpleDateFormat("yyyyMMdd").parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("生日不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            String nowMothDate = getCurrentDate("yyyyMMdd");
			int mmn = Integer.parseInt(nowMothDate.substring(4, 6)); // MM
			int ddn = Integer.parseInt(nowMothDate.substring(6, 8)); // dd
			int mmb = Integer.parseInt(birthday.substring(4, 6)); // MM
			int ddb = Integer.parseInt(birthday.substring(6, 8)); // dd
            if (mmn < mmb) { // 如果当前时间的月日小于出生时的月日
                age -= 1;
            } else if (mmn == mmb) {
            	if (ddn < ddb) {
                	age -= 1;
                }
            }
        }
        return age;
    }
	
	/**
	 * 根据身份证号码识别性别
	 * 
	 * @param IDCardNum
	 * @return
	 */
	public static String identifySexCodeByIDCard(String IDCardNum) {
		if(PatternUtils.is18ByteIdCardComplex(IDCardNum)){
			String sex = IDCardNum.substring(16, 17); // 身份证号倒数第二位，男--奇数，女--偶数
			int sexCode = Integer.parseInt(sex);
			if (sexCode % 2 == 00) {
				return "W"; // 女
			} else {
				return "M"; // 男
			}
		}
		return "X"; // 身份证号有错
	}
	
	/**
	 * 根据身份证号码识别性别
	 * 
	 * @param IDCardNum
	 * @return
	 */
	public static String identifySexByIDCard(String IDCardNum) {
		if(PatternUtils.is18ByteIdCardComplex(IDCardNum)){
			String sex = IDCardNum.substring(16, 17); // 身份证号倒数第二位，男--奇数，女--偶数
			int sexCode = Integer.parseInt(sex);
			if (sexCode % 2 == 00) {
				return "女";
			} else {
				return "男";
			}
		}
		return "x"; // 身份证号有错
	}
	
	/**
	 * 加减月份后的年月日
	 * 
	 * @param m
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String changeMonth(int m) {
		Date date = new Date();//取时间
		System.out.println(date2String(date,"yyyyMMdd"));
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MONTH, m);
		date = calendar.getTime();
		System.out.println(date2String(date, "yyyyMMdd"));
		return date2String(date, "yyyyMMdd");
	}
	
	@SuppressWarnings("static-access")
	public static String changeMonth(Date date, int m) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
//		calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//		calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//		calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动 
//		calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
		calendar.add(calendar.MONTH, m);
		date = calendar.getTime(); //这个时间就是日期往后推一天的结果 
		System.out.println(date2String(date, "yyyyMMdd"));
		return date2String(date, "yyyyMMdd");
	}
	
	/**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() { 
        boolean isCanUse = true;
		Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }
    
    public static String getFxModel(String fxModel) {
		switch (fxModel) {
		case "1":
		case "1,3":
		case "3,1":
			return "1";
		case "2":
		case "1,2":
		case "2,1":
		case "3,2":
		case "2,3":
			return "2";
		case "3":
			return "3";
		default:
			return "ALL";
		}
	}

	/**
	 * 异步10001跳转首页
	 */
	public static void show10001Error(Context context,Throwable e){
//		UrlConfig.showErrorTips(XfjrMain.mApp, e);
		Toast.makeText(XfjrMain.mApp.getApplicationContext(),"验证失败",Toast.LENGTH_SHORT).show();
		XfjrMain.mApp.startActivity(new Intent(XfjrMain.mApp,XfjrIndexActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
}
