package com.mt.collection.processor.meizitu;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/8/27.
 */
public class ImgProcessor implements PageProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Site site;

	public ImgProcessor(String startUrl) {

		this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl)).addHeader(":authority", "www.mzitu.com")
				.addHeader(":method", "www.mzitu.com").addHeader(":path", "/ajax.php?views_id=177079&action=v")
				.addHeader(":scheme", "https").addHeader("accept", "*/*")
				.addHeader("accept-encoding", "gzip, deflate, br").addHeader("accept-language", "zh-CN,zh;q=0.9")
				.addHeader("cookie",
						"Hm_lvt_dbc355aef238b6c32b43eacbbf161c3c=1555475041; Hm_lpvt_dbc355aef238b6c32b43eacbbf161c3c=1555478974")
				.addHeader("referer", "http://i.meizitu.net")
				.addHeader("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
				.addHeader("x-requested-with", "XMLHttpRequest");
		
		
	}

	@Override
	public void process(Page page) {
		String urlPattern = "https://www.mzitu.com/[0-9]{3,9}";
		List<String> requests = page.getHtml().links().regex(urlPattern).all();

		for (String string : requests) {
			logger.info("requests:" + string);
		}

		page.addTargetRequests(requests);
		if (page.getUrl().regex(urlPattern).match()) {

			Map<String, List<String>> map = page.getHeaders();
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				// Map.entry<Integer,String> 映射项（键-值对） 有几个方法：用上面的名字entry
				// entry.getKey() ;entry.getValue(); entry.setValue();
				// map.entrySet() 返回此映射中包含的映射关系的 Set视图。
				System.out.println("key= " + entry.getKey());
				for (String string : entry.getValue()) {
					System.out.println("value= " + string);
				}
			}
			List<String> s = new ArrayList<>();
			s.add(page.getUrl().toString());
			map.put("referer", s);
			page.setHeaders(map);
			
			Html html = page.getHtml();
			String imgHostFileName = html.xpath("//div[@class='content']/h2/text()").toString();
			String picURL = html.xpath("//div[@class='main-image']/p/a").css("img", "src").toString();

			logger.info("imgHostFileName:" + imgHostFileName);
			logger.info("picURL:" + picURL);

			String imgRegex = "https://i.meizitu.net/20[0-9]{2}[a-z]/[0-9]{1,2}/[0-9]{1,9}.jpg";
			List<String> listProcess = html.$("div#picture").regex(imgRegex).all();
			// 此处将标题一并抓取，之后提取出来作为文件名
			listProcess.add(0, imgHostFileName);
			page.putField("img", picURL);
		}

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		// webmagic采集图片代码演示，相关网站仅做代码测试之用,请勿过量采集
		Spider.create(new ImgProcessor("https://www.mzitu.com/")).addUrl("https://www.mzitu.com/")
				.addPipeline(new ImgPipeline("C:\\mt\\workspace_upload\\spider\\mzitu")).thread(5) // 此处线程数可调节
				.run();
	}
}
