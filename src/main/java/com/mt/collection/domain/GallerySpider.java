package com.mt.collection.domain;

import java.io.Serializable;

/**
 * <pre>
 * 爬虫相册
 * </pre>
 *
 * @author : maitao
 * @date : 2019/2/21
 */
public class GallerySpider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6789847605020645715L;

	private Long galleryId;

	/**
	 * 图片名称
	 */
	private String galleryName;

	/**
	 * 图片描述
	 */
	private String galleryDesc;

	/**
	 * 图片日期/拍摄日期
	 */
	private String galleryDate;

	/**
	 * 图片拍摄地点
	 */
	private String galleryLocation;

	/**
	 * 图片缩略图地址
	 */
	private String galleryThumbnailUrl;

	/**
	 * 图片地址
	 */
	private String galleryUrl;

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
	}

	public String getGalleryName() {
		return galleryName;
	}

	public void setGalleryName(String galleryName) {
		this.galleryName = galleryName;
	}

	public String getGalleryDesc() {
		return galleryDesc;
	}

	public void setGalleryDesc(String galleryDesc) {
		this.galleryDesc = galleryDesc;
	}

	public String getGalleryDate() {
		return galleryDate;
	}

	public void setGalleryDate(String galleryDate) {
		this.galleryDate = galleryDate;
	}

	public String getGalleryLocation() {
		return galleryLocation;
	}

	public void setGalleryLocation(String galleryLocation) {
		this.galleryLocation = galleryLocation;
	}

	public String getGalleryThumbnailUrl() {
		return galleryThumbnailUrl;
	}

	public void setGalleryThumbnailUrl(String galleryThumbnailUrl) {
		this.galleryThumbnailUrl = galleryThumbnailUrl;
	}

	public String getGalleryUrl() {
		return galleryUrl;
	}

	public void setGalleryUrl(String galleryUrl) {
		this.galleryUrl = galleryUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
