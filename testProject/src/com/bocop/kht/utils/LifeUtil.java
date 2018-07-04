package com.bocop.kht.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

import com.bocop.kht.config.LifeBaseConfig;
import com.bocop.kht.constants.LifeConstants;

public class LifeUtil {

    private static LifeUtil lifeUtil;

    private AlertDialog dialog;

    public static LifeUtil getInstance() {
        if (lifeUtil == null) {
            synchronized (LifeUtil.class) {
                if (lifeUtil == null) {
                    lifeUtil = new LifeUtil();
                }
            }
        }
        return lifeUtil;
    }



//    public static String getVssToken(Context mContext, String certName, String seqNum) throws IOException {
//        return VSSSeqNumGenerator.getInstance().getVssToken(mContext, certName, seqNum);
//    }

    public static File getFile(String appFid) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".gif");

        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".jpg");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".jpeg");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".png");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".tif");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + "upload/" + appFid + ".tiff");
        }

        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + appFid + ".jpg");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + appFid + ".jpeg");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + appFid + ".png");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + appFid + ".tif");
        }
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + LifeConstants.APP_IMG_URL + appFid + ".tiff");
        }
        if (!file.exists()) {
            return null;
        }
        return file;
    }





    public static String getCallBackFilePath(Context context, int requestCode, int resultCode, Intent data, Uri fileUri) {
        String picturePath = "";
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            switch (requestCode) {
                case LifeConstants.REQUEST_CAMERA:
                    if (fileUri == null) {
                        return picturePath;
                    }
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
                    picturePath = fileUri.toString().replaceFirst("file:///", "/").trim();
                    return picturePath;

                case LifeConstants.REQUEST_PHOTO:
                    uri = data.getData();
                    picturePath = getPhotoPathFromContentUri(context, uri);
                    return picturePath;
                case LifeBaseConfig.CONTENT_REQUEST_CODE:
                    uri = data.getData();
                    picturePath = getPhotoPathFromContentUri(context, uri);
                    return picturePath;
                default:
                    return picturePath;
            }
        }
        return picturePath;
    }


    @SuppressLint("NewApi")
	public static String getPhotoPathFromContentUri(Context context, Uri uri) {
        String photoPath = "";
        if (context == null || uri == null) {
            return photoPath;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (isExternalStorageDocument(uri)) {
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                photoPath = getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if ("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        } else {
            photoPath = getDataColumn(context, uri, null, null);
        }

        return photoPath;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

}
