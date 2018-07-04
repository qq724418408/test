package com.bocop.kht.bean;

import java.io.Serializable;

/**
 * 
 * @author xiaxq
 *
 */
public class CameraBean implements Serializable {

	private String sourceType;
	private String compressWidth;
	private String compressHeight;
	private String thumbnailWidth;
	private String thumbnailHeight;
	//private String imageName;
	private String imageFile;
	private String imageFormat;
	private String fileSize;
	private String args;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public CameraBean() {
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getCompressWidth() {
		return compressWidth;
	}

	public void setCompressWidth(String compressWidth) {
		this.compressWidth = compressWidth;
	}

	public String getCompressHeight() {
		return compressHeight;
	}

	public void setCompressHeight(String compressHeight) {
		this.compressHeight = compressHeight;
	}

	public String getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(String thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public String getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(String thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}


	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

}
