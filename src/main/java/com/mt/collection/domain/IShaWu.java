package com.mt.collection.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author maitao
 * @version 1.0.0
 * @date 19/4/16 下午10:04.
 * @blog
 */
@Document(collection = "IShaWu_1")
/*
 * 建立索引，提高查询速度
 */
//@CompoundIndexes({ @CompoundIndex(name = "publishDate_informationId", def = "{'publishDate':-1,'informationId':1}") })
public class IShaWu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6759809444045644627L;

	@Field("iShaWu_author")
	private String url;
	private String author;
	private String title;
	private String date;
	private Integer viewCount;
	private String content;
	private String imgs;

	public IShaWu(String url, String author, String title, String date, Integer viewCount, String content, String imgs) {
		this.url = url;
		this.author = author;
		this.title = title;
		this.date = date;
		this.viewCount = viewCount;
		this.content = content;
		this.imgs = imgs;
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

}
