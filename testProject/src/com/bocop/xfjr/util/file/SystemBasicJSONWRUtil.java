package com.bocop.xfjr.util.file;

import static android.content.Context.MODE_PRIVATE;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.alibaba.fastjson.JSON;
import com.bocop.xfjr.bean.SystemBasicInfo;
import com.mob.tools.network.BufferedByteArrayOutputStream;

import android.content.Context;

public class SystemBasicJSONWRUtil {
	public static SystemBasicInfo sInfo;

	/**
	 * 保存系统信息
	 * 
	 * @param context
	 * @param info
	 * @return
	 */
    public static boolean writeSystemBasicInfo(Context context, SystemBasicInfo info) {
        return write(context, systemBasicInfoJson, info);
    }

    /**
     * 读取系统信息
     * 
     * @param context
     * @return
     */
    public static SystemBasicInfo readSystemBasicInfo(Context context) {
        return read(context, systemBasicInfoJson);
    }

    public static boolean clearAllCache(Context context) {
        return deleteAll(context);
    }

    /**************************************以下私有属性和方法**************************************/

    private static String systemBasicInfoJson = "systemBasicInfo.json";

    private static boolean write(Context context, String fileName, SystemBasicInfo info) {
    	sInfo = info;
        boolean result;
        try {
            //context.deleteFile(fileName);
            FileOutputStream fout = context.openFileOutput(fileName, MODE_PRIVATE);//获得FileOutputStream
            //将要写入的字符串转换为byte数组
            byte[] bytes = JSON.toJSONString(info).getBytes();
            fout.write(bytes);//将byte数组写入文件
            fout.close();//关闭文件输出流
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private static SystemBasicInfo read(Context context, String fileName) {
    	SystemBasicInfo bSystemBasicInfo = new SystemBasicInfo();
        String result = "";
        FileInputStream inputStream;
        BufferedByteArrayOutputStream outputStream = new BufferedByteArrayOutputStream();
        try {
            inputStream = context.openFileInput(fileName);
            byte[] temp = new byte[1024];
//            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp)) != -1) {
//            	sb.append(new String(b,0,len));
                outputStream.write(temp, 0, len);
            }
//            result = sb.toString();
            result = outputStream.toString();
            bSystemBasicInfo = JSON.parseObject(result, SystemBasicInfo.class);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bSystemBasicInfo;
    }

    private static boolean deleteAll(Context context) {
        return context.deleteFile(systemBasicInfoJson);
    }

}
