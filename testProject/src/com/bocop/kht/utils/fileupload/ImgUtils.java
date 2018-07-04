package com.bocop.kht.utils.fileupload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.boc.jx.tools.LogUtils;
import com.bocop.kht.constants.LifeConstants;
import com.bocop.kht.utils.FileUtil;

/**
 * @author xinqing
 */
public class ImgUtils {

	private static final String TAG = "ImgUtils: ";
	// public static final String LOCAL_ROOT_PATH = "/LIFE_IMAGE";
	public static final String LOCAL_FILE_DIR = Environment.getExternalStorageDirectory() + LifeConstants.APP_IMG_URL
			+ "/upload/";
	private static boolean isScaled = false;
	private static int quality = 100;

	public static Object compressImage(File file, String targetFileName, int height, int width, long fileSize,
			String imgType, String source) throws Exception {
		quality = 100;
		if (file == null || !file.isFile() || !file.exists())
			return "File read error";
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		if (!source.equals("c") && !isTypeValid(file)) {
			LogUtils.d(TAG + "Image format error");
			return "Image format error";
		}
		if (!(file.getName().toLowerCase().endsWith(".gif") || file.getName().toLowerCase().endsWith(".tif")
				|| file.getName().toLowerCase().endsWith(".tiff"))) {
			Bitmap sourceBmp;
			try {
				byte[] bytes = getBytesFromFile(file);
				sourceBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			} catch (OutOfMemoryError e) {
				LogUtils.d(TAG + "out of memory");
				return ResultCode.IMAGE_OUT_OF_MEMORY;
			}

			try {
				int readPictureDegree = readPictureDegree(file.getAbsolutePath());

				Bitmap bitmapScaling = checkScaling(sourceBmp, height, width);

				Object outputStream = compressQuality(file, bitmapScaling, imgType, fileSize, targetFileName);
				recycleBitmap(bitmapScaling);

				Bitmap afterRotateBitmap;
				if (outputStream instanceof File) {
					File renameFile = (File) outputStream;

					Bitmap bitmapFromFile = BitmapFactory.decodeFile(renameFile.getAbsolutePath());
					if (readPictureDegree != 0) {
						afterRotateBitmap = toturn(bitmapFromFile, readPictureDegree);

					} else {
						afterRotateBitmap = bitmapFromFile;
					}
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(renameFile));
					try {
						if (getTypeFromName(renameFile).equalsIgnoreCase("png")) {
							afterRotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
						} else {
							afterRotateBitmap.compress(Bitmap.CompressFormat.JPEG, ImgUtils.quality, bos);
						}
						bos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						recycleBitmap(bitmapFromFile);
						recycleBitmap(afterRotateBitmap);
					}
					return renameFile;
				} else if (outputStream instanceof ByteArrayOutputStream) {
					if (((ByteArrayOutputStream) outputStream).size() > 0) {
						ByteArrayInputStream is = new ByteArrayInputStream(
								((ByteArrayOutputStream) outputStream).toByteArray());
						Bitmap bitmapFromBaos = BitmapFactory.decodeStream(is);
						if (readPictureDegree != 0) {
							afterRotateBitmap = toturn(bitmapFromBaos, readPictureDegree);

						} else {
							afterRotateBitmap = bitmapFromBaos;
						}
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						if (getTypeFromName(file).equalsIgnoreCase("png")) {
							afterRotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
						} else {
							afterRotateBitmap.compress(Bitmap.CompressFormat.JPEG, ImgUtils.quality, baos);
						}
						recycleBitmap(bitmapFromBaos);
						recycleBitmap(afterRotateBitmap);

						return writeFile(baos, targetFileName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtils.d(TAG + e.getMessage());
			}
			return "Image compress error";
		} else {// git/tif/tiff
			return renameFile(file, targetFileName, null);
		}
	}

	/**
	 * @param baos
	 * @param targetFileName
	 * @return
	 * @throws Exception
	 */
	private static Object writeFile(ByteArrayOutputStream baos, String targetFileName) throws Exception {
		baos.close();
		File photoStorageDir = new File(LOCAL_FILE_DIR);
		if (!photoStorageDir.exists()) {
			if (!photoStorageDir.mkdirs()) {
				return "Create Dir Fail";
			}
		}
		File targetFile = new File(LOCAL_FILE_DIR + targetFileName);
		if (targetFile.exists()) {
			boolean flag = targetFile.delete();
			if (!flag) {
				return "Image exist";
			}
		}
		FileOutputStream fos = new FileOutputStream(targetFile);
		fos.write(baos.toByteArray());
		fos.flush();
		fos.close();

		return targetFile;
	}

	/**
	 * compress the quality of image
	 *
	 * @param file
	 * @param bitmap
	 * @param targetFormat
	 * @param targetSize
	 * @param targetFileName
	 * @return
	 */
	private static Object compressQuality(File file, Bitmap bitmap, String targetFormat, long targetSize,
			String targetFileName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (file.length() > targetSize) {// The file must be compressed.
			ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
			for (int quality = 100; quality > 0; quality -= 10) {
				tmpBaos.reset();
				if (targetFormat.equalsIgnoreCase("png")) {
					bitmap.compress(Bitmap.CompressFormat.PNG, quality, tmpBaos);
				} else {
					bitmap.compress(Bitmap.CompressFormat.JPEG, quality, tmpBaos);
				}
				if (tmpBaos.toByteArray().length <= targetSize || quality == 10) {
					ImgUtils.quality = quality;
					return tmpBaos;
				}
			}
		} else if (isScaled) {// if the image is scaled, return baos
			String imgType;
			if (targetFormat.equalsIgnoreCase("default")) {
				imgType = getTypeFromName(file);
			} else {
				imgType = targetFormat;
			}
			if (imgType.equalsIgnoreCase("png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
		} else {
			return renameFile(file, targetFileName, bitmap);
		}
		return baos;
	}

	public static String renameAndDeleteFile(File oldFile, String newFileName) {
		Object object = renameFile(oldFile, newFileName, null);
		if (object instanceof File) {
			oldFile.delete();
			return ((File) object).getAbsolutePath();

		} else {
			return "Get Image From ";
		}
	}

	private static Object renameFile(File oldFile, String newFileName, Bitmap bitmap) {
		int degree = -1;
		if (bitmap != null) {
			bitmap.recycle();
		}
		if (!(oldFile.getName().toLowerCase().endsWith(".gif") || oldFile.getName().toLowerCase().endsWith(".tif")
				|| oldFile.getName().toLowerCase().endsWith(".tiff"))) {
			degree = readPictureDegree(oldFile.getAbsolutePath());
		}
		if (degree != 0 && degree != -1) {
			byte[] bytes;
			try {
				bytes = getBytesFromFile(oldFile);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				Bitmap tmpBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				Bitmap toturn = toturn(tmpBitmap, degree);
				String getTypeFromName = getTypeFromName(oldFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if (getTypeFromName.equalsIgnoreCase("png")) {
					toturn.compress(Bitmap.CompressFormat.PNG, 100, baos);
				} else {
					toturn.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				}
				baos.close();
				File photoStorageDir = new File(LOCAL_FILE_DIR);
				if (!photoStorageDir.exists()) {
					if (!photoStorageDir.mkdirs()) {
						return "Create Dir Fail";
					}
				}
				File targetFile = new File(LOCAL_FILE_DIR + newFileName);
				if (targetFile.exists()) {
					boolean flag = targetFile.delete();
					if (!flag) {
						return "Image exist";
					}
				}
				FileOutputStream fos = new FileOutputStream(targetFile);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
				return targetFile;
			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		File photoStorageDir = new File(LOCAL_FILE_DIR);
		if (!photoStorageDir.exists()) {
			if (!photoStorageDir.mkdirs()) {
				return "Create Dir Fail";
			}
		}
		File targetFile = new File(LOCAL_FILE_DIR + newFileName);
		if (targetFile.exists()) {
			boolean flag = targetFile.delete();
			if (!flag) {
				return "Image exist";
			}
		}
		InputStream fosfrom;
		try {
			fosfrom = new FileInputStream(oldFile.getAbsolutePath());
			OutputStream fosto = new FileOutputStream(LOCAL_FILE_DIR + newFileName);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return new File(LOCAL_FILE_DIR + newFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return "Image Write Error";
		}
	}

	private static boolean isTypeValid(File file) {
		String filePath = file.getAbsolutePath();
		String imageType = getTypeFromName(file);
		if (imageType.toLowerCase().equals("gif") || imageType.toLowerCase().equals("tif")
				|| imageType.toLowerCase().equals("tiff")) {
			String realType = FileUtil.getFileType(filePath, 4);
			LogUtils.d(TAG + "File realType -> " + realType);
			return ("gif".equalsIgnoreCase(realType) && "gif".equalsIgnoreCase(imageType))
					|| ("tif".equalsIgnoreCase(realType) && "tif".equalsIgnoreCase(imageType))
					|| ("tif".equalsIgnoreCase(realType) && "tiff".equalsIgnoreCase(imageType));
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		String type = options.outMimeType;
		if (TextUtils.isEmpty(type)) {
			return false;
		} else if (type.toLowerCase().contains("jpeg") && imageType.equalsIgnoreCase("jpg")
				|| type.toLowerCase().contains("jpeg") && imageType.equalsIgnoreCase("jpeg")
				|| type.toLowerCase().contains("png") && imageType.equalsIgnoreCase("png")) {
			return true;
		}
		return false;
	}

	/**
	 * @param bitmap
	 * @return
	 */
	public static Bitmap checkScaling(Bitmap bitmap, double targetH, double targetW) {
		int imgWidth = bitmap.getWidth();
		int imgHeight = bitmap.getHeight();
		float scaleHeight = 0f, scaleWidth = 0f, scale = 0f;
		if (targetW < imgWidth && targetH < imgHeight) {
			scaleWidth = (float) targetW / imgWidth;
			scaleHeight = (float) targetH / imgHeight;
			scale = Math.min(scaleWidth, scaleHeight);
		} else if (targetW < imgWidth) {
			scale = (float) targetW / imgWidth;
		} else if (targetH < imgHeight) {
			scale = (float) targetH / imgHeight;
		}
		if (scale > 0) {// 0.17094
			isScaled = true;
			scale = subFloat(scale);
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
					false);
			recycleBitmap(bitmap);
			return createBitmap;
		} else {
			isScaled = false;
		}
		return bitmap;
	}

	private static float subFloat(Float scale) {
		String valueOf = String.valueOf(scale);
		String substring = null;
		if (valueOf.length() > 7) {
			substring = valueOf.substring(0, 7);
		} else {
			return scale;
		}
		return Float.valueOf(substring);
	}

	/**
	 * @param file
	 * @return
	 */
	public static String getTypeFromName(File file) {
		String fileName = file.getName();
		int lastIndexOf = fileName.lastIndexOf(".");
		return fileName.substring(lastIndexOf + 1, fileName.length());
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytesFromFile(File file) throws Exception {
		if (file != null && !(file.length() > Integer.MAX_VALUE)) {
			try {
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[(int) file.length()];
				int offest = 0;
				int numRead = 0;
				while (offest < bytes.length && (numRead = is.read(bytes, offest, bytes.length - offest)) >= 0) {
					offest += numRead;
				}
				is.close();
				return bytes;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new Exception("File is null or file is to large");
	}

	/**
	 * @param path
	 * @return
	 */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * @param img
	 * @param exif
	 * @return
	 */
	private static Bitmap toturn(Bitmap img, int exif) {
		Matrix matrix = new Matrix();
		matrix.postRotate(exif);
		int width = img.getWidth();
		int height = img.getHeight();
		Bitmap newimg = Bitmap.createBitmap(img, 0, 0, width, height, matrix, false);
		img.recycle();
		return newimg;
	}

	public static File scalFile(File file, String newFileName) {
		long fileSize = file.length();
		final long fileMaxSize = 200 * 1024;
		if (fileSize > fileMaxSize) {
			try {
				String imgFormat = getTypeFromName(file);
				byte[] bytes = getBytesFromFile(file);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				int width = options.outWidth;
				int height = options.outHeight;
				int sampleSize = 1;
				if (width > height) {
					sampleSize = Math.max(width / 1280, height / 960);
				} else {
					sampleSize = Math.max(width / 960, height / 1280);
				}
				if (sampleSize < 1)
					sampleSize = 1;
				options.inJustDecodeBounds = false;
				options.inSampleSize = sampleSize;
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
				if (bitmap != null) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int quality = 100;
					if (imgFormat.equalsIgnoreCase("jpg") || imgFormat.equalsIgnoreCase("jpeg")) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					} else {
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					}
					while (baos.toByteArray().length > fileMaxSize) {
						baos.reset();
						quality -= 10;
						if (imgFormat.equalsIgnoreCase("jpg") || imgFormat.equalsIgnoreCase("jpeg")) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						} else {
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
						}
					}
					baos.close();
					File photoStorageDir = new File(LOCAL_FILE_DIR);
					if (!photoStorageDir.exists()) {
						if (!photoStorageDir.mkdirs()) {
							return null;
						}
					}
					String targetPath = LOCAL_FILE_DIR + newFileName;
					File targetFile = new File(targetPath);
					if (targetFile.exists()) {
						boolean flag = targetFile.delete();
					}
					FileOutputStream fos = new FileOutputStream(targetFile);
					fos.write(baos.toByteArray());
					fos.flush();
					fos.close();
					return targetFile;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return file;
		}
	}

	public static String imgToBase64(String filePath, Bitmap bitmap) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			if (!TextUtils.isEmpty(filePath)
					&& (filePath.toLowerCase().endsWith("jpg") || filePath.toLowerCase().endsWith("jpeg"))) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}
			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String imgToBase64(String imgPath) {
		Bitmap bitmap = null;
		if (imgPath != null && imgPath.length() > 0) {
			bitmap = readBitmap(imgPath);
		}
		if (bitmap == null) {
			// bitmap not found!!
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			if (imgPath.toLowerCase().endsWith("jpg") || imgPath.toLowerCase().endsWith("jpeg")) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			}

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static Bitmap readBitmap(String imgPath) {
		try {
			return BitmapFactory.decodeFile(imgPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static String fileToBase64(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			LogUtils.e("fileToBase64:-------" + fis.available());
			ByteArrayOutputStream bos = new ByteArrayOutputStream(fis.available());
			byte[] b = new byte[fis.available()];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			buffer = bos.toByteArray();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		if (buffer != null) {
			return Base64.encodeToString(buffer, Base64.DEFAULT);
		} else {
			return null;
		}
	}

	private static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
	}

}
