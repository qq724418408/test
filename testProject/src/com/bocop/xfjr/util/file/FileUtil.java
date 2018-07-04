package com.bocop.xfjr.util.file;

import java.io.File;

import com.boc.jx.tools.FileUtils;
import com.boc.jx.tools.LogUtils;
import com.bocop.xfjr.activity.XfjrPreViewActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Environment;

public class FileUtil {

	public static String createSaveImagePath(Context context) {
		// 判断sdcard是否存在
		File dir = null;
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				// qwe为相片保存的文件夹名
				String eSDPath = Environment.getExternalStorageDirectory().getPath();
				String pkg = context.getPackageName();
				// qwe为相片保存的文件夹名
				dir = new File(eSDPath + File.separator + pkg + File.separator + "image");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				LogUtils.e(dir.getAbsolutePath());
				LogUtils.e(dir.getPath());
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
		return dir.getAbsolutePath();
	}
	
	/**
	 * 预览图片
	 * 
	 * @param path
	 * @param context
	 */
	public static void preview(String path, Context context) {
//		FileUtils.openFile(path, context);
		XfjrPreViewActivity.startActivity(context, path);
	}
	
	/**
	 * onDestroy的时候删除图片，不做本地保留
	 */
	public static void delAllImages(Context context) {
		String eSDPath = Environment.getExternalStorageDirectory().getPath();
		String pkg = context.getPackageName();
		String imgPath1 = eSDPath + File.separator + "image";
		String imgPath2 = eSDPath + File.separator + pkg + File.separator + "image";
		File file1 = new File(imgPath1);
		File file2 = new File(imgPath2);
		FileUtils.deleteFilesByDirectory(file1);
		FileUtils.deleteFilesByDirectory(file2);
	}

	@Deprecated
	public static void createTemPath() {
		// 判断sdcard是否存在
		File dir = null;
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				// qwe为相片保存的文件夹名
				String eSDPath = Environment.getExternalStorageDirectory().getPath();
				// qwe为相片保存的文件夹名
				dir = new File(eSDPath + File.separator + "image");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, "temp");
				if (!file.exists()) {
					file.createNewFile();
				}
				LogUtils.e(file.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
