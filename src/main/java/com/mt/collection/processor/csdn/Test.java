package com.mt.collection.processor.csdn;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zsh
 * @site qqzsh.top
 * @company wlgzs
 * @create 2019-04-08 22:23
 * @Description 抓取小组人员名单
 */
public class Test implements PageProcessor {
    // 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    private static Map<String,String> map = new HashMap<>();

    // 抽取逻辑类
    public void process(Page page) {
        if (page.getUrl().regex("http://wlgzs.org/.*").match()) {
            // 姓名
            List<String> allname = page.getHtml().xpath("//span[@class='title']/a/text()").all();
            // URL
            List<String> allurl = page.getHtml().xpath("//span[@class='title']/a/@href").all();

            System.out.println(allname.size());

            for (int i = 0; i < allname.size(); i++) {
                map.put(allname.get(i),allurl.get(i));
            }

            POITest.printExcel(map);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
                //entry.getKey() ;entry.getValue(); entry.setValue();
               //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
               System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            }
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new Test()).addUrl("http://wlgzs.org/blog.html").thread(5).run();
    }
}
