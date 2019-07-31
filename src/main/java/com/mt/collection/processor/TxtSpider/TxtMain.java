package com.mt.collection.processor.TxtSpider;


import org.apache.http.HttpHost;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016/8/28.
 */
public class TxtMain {
    public static void main(String[] args) {

        System.out.println(new Date()+"开始吧》》》》》》.");
        Spider.create(new T66yTxtProcessor())
                .addUrl( "http://cl.vtcjd.com/htm_data/20/1609/1863480.html")
                .addPipeline(new T66yTxtPipeLine())
                .thread(10)
                .run();
    }
}

class  T66yTxtProcessor implements PageProcessor{

    private static Map<Integer,List<String>> resultMap = new TreeMap<Integer,List<String>>();
    private static String postName="";

    private Site site = Site.me().setDomain("cl.vtcjd.com").setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.36 Safari/537.36")
            .setCharset("gb2312");


    @Override
    public void process(Page page) {


        //获取所有待爬取的链接
        List<String> fondUrls=page.getHtml().xpath("//tr[@class='tr1 do_not_catch']/th/table/tbody/tr/td/div[@class='tpc_content do_not_catch']/a/text()").all();

        List<String> newList = new ArrayList<String>();//替换无法连接的url
        for (String url:fondUrls) {
            String newUrl;
            if (url.contains("t66y.com")){
                 newUrl = url.replace("http://t66y.com/read.php?tid=1863480&page=188&fpage=1", "http://cl.vtcjd.com/read.php?tid=1863480&page=188");
            }else if(url.contains("162")){
                newUrl="http://cl.vtcjd.com/read.php?tid=1863480&page=162";
            }else{
                 newUrl = url.replace("http://cl.deocool.pw/read.php?tid=1863480&fpage=0&toread=&page", "http://cl.vtcjd.com/read.php?tid=1863480&page");
            }
            newList.add(newUrl);
        }
        if(newList.size()!=0){

            page.addTargetRequests(newList);//将爬到的链接加入带爬队列
        }

        List<String> contentHome;
        Integer index=0;
        //其他页的html格式和当前页不同，需另外配置抓取策略
        if (page.getRequest().getUrl().contains("page=")){
            String substring = page.getRequest().getUrl().substring(page.getRequest().getUrl().lastIndexOf("=") + 1, page.getRequest().getUrl().length());
            index = Integer.valueOf(substring);
            contentHome = page.getHtml().xpath("//tr[@class=tr1]/th/div[@class=tpc_content]/text()").regex(".*我.*").all();
        }else {
             index = 1;
            postName= page.getHtml().xpath("//tr[@class='tr1 do_not_catch']/th/table/tbody/tr/td/h4/text()").toString();//标题
            //获取当前页面上的内容
            contentHome = page.getHtml().xpath("//tr[@class='tr1 do_not_catch']/th/table/tbody/tr/td/div[@class='tpc_content do_not_catch']/text()").regex(".*我.*").all();

        }


        //存取到map中排序
        resultMap.put(index,contentHome);

        if (resultMap.size()>=11){
            page.putField("postContent",resultMap);
            page.putField("postName",postName);

        }


    }

    @Override
    public Site getSite() {
        return site;
    }
}


class T66yTxtPipeLine extends FilePersistentBase implements Pipeline{

    public T66yTxtPipeLine() {
        setPath("D:\\temp\\caoliu");
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> fields = resultItems.getAll();
        Map<Integer,List<String>>  postContent = (Map<Integer,List<String>> ) fields.get("postContent");


        if(postContent != null){
            String postName = (String) fields.get("postName");
            Set<Integer> keys = postContent.keySet();
            String dataContent="";
            for(Integer key:keys){
                List<String> contents = postContent.get(key);
                for (String content:contents) {
                    dataContent=dataContent+content+"\r\n"+">>>>>>>>>>>>>>>>>>>分割线"+"\r\n";
                }
            }
            witeToTxt(postName,dataContent);

         }
        }

    private void witeToTxt(String postName,String content) {
        String filePathDir = "D:\\temp\\caoliu\\" + postName+".txt";

        try {

            File mar = new File("D:\\temp\\caoliu");
            if (!mar.exists()) {
                mar.mkdirs();
            }


            File file = new File(filePathDir);

            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file,false);
            BufferedWriter bw = new BufferedWriter(fw);


            bw.write(content);
            bw.newLine();
            bw.flush();
            bw.close();

            System.out.println(new Date()+"采集完毕》》》》》》.");


        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}







