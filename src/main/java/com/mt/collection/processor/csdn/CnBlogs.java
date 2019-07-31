package com.mt.collection.processor.csdn;

/**
 * @author zsh
 * @site qqzsh.top
 * @company wlgzs
 * @create 2019-04-08 20:58
 * @Description 博客模型类
 */
public class CnBlogs {

    // 标题
    private String title;
    // 作者
    private String author;
    // 发布日期
    private String dateTime;
    // 地址
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CnBlogs [title=" + title + ", author=" + author + ", dateTime=" + dateTime + ", url=" + url + "]";
    }

}