package com.bocop.jxplatform.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * 图片保存工具类
 * 
 * @author HaoZai
 * 
 */
public class SaveImageToSD {

	/** 保存方法 */
	public static void saveBitmap(Bitmap bm) {
		File f = new File("mnt/sdcard/yhtqrcode.png");
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
