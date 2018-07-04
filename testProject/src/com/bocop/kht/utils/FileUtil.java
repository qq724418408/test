package com.bocop.kht.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.boc.jx.tools.LogUtils;

public class FileUtil {

    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();

    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        // txt
        mFileTypes.put("41433130", "dwg"); // CAD
        mFileTypes.put("38425053", "psd");
        mFileTypes.put("7B5C727466", "rtf"); // diary
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        mFileTypes.put("44656C69766572792D646174653A", "eml"); // email
        mFileTypes.put("D0CF11E0", "doc");
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "zip");
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
        mFileTypes.put("", "");
        mFileTypes.put("", "");
    }

    private static String getFileHeader(String filePath, int headerLength) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[headerLength];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {

        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public static String getFileType(String filePath, int headerLength) {
        return mFileTypes.get(getFileHeader(filePath, headerLength));
    }

    public static String readLocal(Context context, String fileName) {
        String content;
        content = readFileDir(context, fileName);
        if (TextUtils.isEmpty(content)) {
            content = readAssetsFile(context, fileName);
        }
        return content;
    }

    public static String readAssetsFile(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                sb.append(info);
            }
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                LogUtils.e(e.getMessage());
            }
        }
        return sb.toString();
    }

    static String readFileDir(Context context, String fileName) {
        File fileDir = new File(context.getFilesDir().getAbsolutePath(), fileName);
        String content = null;
        if (fileDir.isFile() && fileDir.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(fileDir);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                content = new String(bytes, "UTF-8");
            } catch (IOException e) {
                LogUtils.e(e.getMessage());
            } finally {
                try {
                    if (null != fis) {
                        fis.close();
                    }
                } catch (Exception e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
        return content;
    }

    public static void writeFileDir(Context context, String fileName, String content) {
        if (TextUtils.isEmpty(content))
            return;
        File fileDir = new File(context.getFilesDir(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileDir);
            fos.write(content.getBytes("UTF-8"));
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    }

    public static long getFileSize(String filePath) throws Exception {
        long size = 0;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }
        return size;
    }

    public static boolean deleteFileByPath(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

}
