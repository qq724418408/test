package com.bocop.xfjr.helper.carsh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Description : 报损错误信息
 * Created : TIAN FENG
 * Date : 2017/3/31
 * Email : 27674569@qq.com
 * Version : 1.0
 */
class SaveCarshUtils {

    /**
     * 保存到txt文本
     */
    public static void saveToFile(String message) throws IOException {
        // 文件夹路径
        String rootDir = CarshExceptionHandler.CARSH_ROOT_DIR;

        File fileError = new File(rootDir);
        if (fileError.exists()){
            // 删除该目录下的所有子文件
            deleteDir(fileError);
        }
        // 再从新创建文件夹
        if (!fileError.exists()) {
            fileError.mkdir();
        }
        String fileName = fileError.toString() + File.separator + getFileName() + ".txt";
        // 字节流存储
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(message.getBytes());
        fos.flush();
        fos.close();

    }

    /**
     * 获取Carsh文件夹下的文件
     */
    public static File[] getFile(){
        String rootDir = CarshExceptionHandler.CARSH_ROOT_DIR;
        File fileError = new File(rootDir);
        return fileError.listFiles();
    }

    /**
     * 保存到数据库
     */
    public static void saveToDB(String message){

    }


    /**
     * 获取文件名
     */
    private static String getFileName() {
        DateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful. If a
     * deletion fails, the method stops attempting to delete and returns
     * "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            // 递归删除目录中的子目录下
            for (File child : children) {
                child.delete();
            }
        }
        // 目录此时为空，可以删除
        return true;
    }
}
