package com.mt.collection.processor.csdn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author zsh
 * @site qqzsh.top
 * @company wlgzs
 * @create 2019-04-08 20:59
 * @Description
 */
public class CnBlogProcessor implements PageProcessor {

    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setCharset("utf-8").setRetryTimes(5).setSleepTime(1000);
    // 文章数量
    private static int size = 0;
    // 文章集合
    private static List<CnBlogs> cnBlogses = new ArrayList<>();

    // 抽取逻辑类
    public void process(Page page) {
        CnBlogs cnBlogs = new CnBlogs();
        //博客园博客
        if (page.getUrl().regex("https://www.cnblogs.com/.*").match()) {
            // 标题
            //暂时发现3个样式
            String title = page.getHtml().xpath("//div[@class='entrylistPosttitle']/a/text()").get();
            if (title != null){
                cnBlogs.setTitle(title);
            }else {
                title = page.getHtml().xpath("//div[@class='postTitle']/a/text()").get();
                if (title != null){
                    cnBlogs.setTitle(title);
                }else {
                    title =  page.getHtml().xpath("//div[@class='post']/h5/a/text()").get();
                    cnBlogs.setTitle(title);
                }
            }

            // 作者
            cnBlogs.setAuthor(page.getHtml().xpath("//a[@id='Header1_HeaderTitle']/text()").get());

            // 发布日期
            String datatime = page.getHtml().xpath("//div[@class='entrylistItemPostDesc']/a/text()").get();
            if (datatime != null){
                cnBlogs.setDateTime(datatime);
            }else {
                datatime = page.getHtml().xpath("//div[@class='postDesc']/text()").get();
                if (datatime != null){
                    int qian = datatime.indexOf("@");
                    int hou = datatime.indexOf(cnBlogs.getAuthor());
                    datatime = datatime.substring(qian+2,hou-1);
                    cnBlogs.setDateTime(datatime);
                }else {
                    datatime = page.getHtml().xpath("//p[@class='postfoot']/a/text()").get();
                    cnBlogs.setDateTime(datatime);
                }
            }
            // URL
            String url = page.getHtml().xpath("//div[@class='entrylistPosttitle']/a/@href").get();
            if (url != null){
                cnBlogs.setUrl(url);
            }else {
                url = page.getHtml().xpath("//div[@class='postTitle']/a/@href").get();
                if (url != null){
                    cnBlogs.setUrl(url);
                }else {
                    url =  page.getHtml().xpath("//div[@class='post']/h5/a/@href").get();
                    cnBlogs.setUrl(url);
                }
            }
            cnBlogses.add(cnBlogs);
        }else if (page.getUrl().regex("https://blog.csdn.net/.*").match()){  //csdn博客
            // 标题
            String title = page.getHtml().xpath("//div[@class='article-item-box csdn-tracking-statistics']/h4/a/text()").all().get(1);
            if (title != null){
                cnBlogs.setTitle(title);
            }
            // 作者
            cnBlogs.setAuthor(page.getHtml().xpath("//a[@id='uid']/text()").get());

            // 发布日期
            String datatime = page.getHtml().xpath("//span[@class='date']/text()").all().get(1);
            if (datatime != null){
                cnBlogs.setDateTime(datatime);
            }

            // URL
            String url = page.getHtml().xpath("//div[@class='article-item-box csdn-tracking-statistics']/h4/a/@href").all().get(1);
            if (url != null){
                cnBlogs.setUrl(url);
            }
            cnBlogses.add(cnBlogs);
        }else if (page.getUrl().regex("https://www.jianshu.com/.*").match()){ //简书
            // 标题
            String title = page.getHtml().xpath("//div[@class='content']/a/text()").get();
            if (title != null){
                cnBlogs.setTitle(title);
            }
            // 作者
            cnBlogs.setAuthor(page.getHtml().xpath("//a[@class='name']/text()").all().get(1));

            // 发布日期
            String datatime = page.getHtml().xpath("//span[@class='time']/@data-shared-at").get();
            if (datatime != null){
                cnBlogs.setDateTime(datatime);
            }

            // URL
            String url = "https://www.jianshu.com"+page.getHtml().xpath("//div[@class='content']/a/@href").get();
            if (url != null){
                cnBlogs.setUrl(url);
            }
            cnBlogses.add(cnBlogs);

        }else {
            cnBlogses.add(cnBlogs);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws IOException {
        //1.先去小组官网抓取人员名单
        Spider.create(new Test()).addUrl("http://wlgzs.org/blog.html").addPipeline(new JsonFilePipeline("C:\\mt\\workspace_upload\\spider")).thread(5).run();
        //2.根据抓取的名单来获取博客
        Map<String, String> map = POITest.readExcel();
        List<String> name = new ArrayList<>();
        List<String> url = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            name.add(entry.getKey());
            url.add(entry.getValue());
            if (!entry.getKey().equals("计科182 杨惠涵")){
                Spider.create(new CnBlogProcessor()).addUrl(entry.getValue()).thread(10).run();
            }else {
                CnBlogProcessor.cnBlogses.add(new CnBlogs());
            }
        }
        POITest.printExcel2(name,url,CnBlogProcessor.cnBlogses);
    }
}