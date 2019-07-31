package com.mt.collection.processor._7160;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 */
/**
 * @author tao_m
 *
 */
public class _7160SpiderTest implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(_7160SpiderTest.class);

	public _7160SpiderTest() {
	}

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	// 先从浏览器中分析出隐藏请求可得出以下匹配规则
	private static final String URLRULE = "https://www\\.7160\\.com/rentiyishu/list_1_\\w+.html";
	private static final String URLRULE_1 = "https://www\\.7160\\.com/rentiyishu/\\w+/";

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {

		List<String> SpidertURL = new ArrayList<String>();

		for (int i = 20; i < 90; i++) {// 添加到目标url中
			SpidertURL.add("https://www.7160.com/rentiyishu/list_1_" + i + ".html");
		}
		page.addTargetRequests(SpidertURL);
		if (page.getUrl().regex(URLRULE).match()) {
			List<String> detailURL = page.getHtml().xpath("//div[@class='new-img']//a").links().all();
			page.addTargetRequests(detailURL);
		} else {

			String title = page.getHtml().xpath("//div[@class='picmainer']//h1/text()").toString();
			String date = page.getHtml().xpath("//div[@class='pictopline']//span[1]/text()").toString();
			String author = page.getHtml().xpath("//div[@class='author_right']/span/text()").toString();
			List<String> imgs = page.getHtml().xpath("//div[@class='picsbox picsboxcenter']//img/@src").all();
			logger.info("title:" + title);
			if (title != null) {
				for (String string : imgs) {
					logger.info(page.getUrl() + " imgUrl:" + string);
					page.putField("page", page.getUrl().toString());
					page.putField("date", date.trim());
					page.putField("author", author);
					page.putField("title", title);
					page.putField("imgUrl", string);
				}
				if (page.getUrl().regex(URLRULE_1).match()) {
					List<String> pageNums = page.getHtml().xpath("//div[@class='itempage']//a/text()").all();
					if (!page.getUrl().toString().endsWith(".html")) {
						if (pageNums != null && pageNums.size() != 0) {
							for (int j = 2; j < pageNums.size() - 2; j++) {
								String s = page.getUrl() + "index_" + j + ".html";
								page.addTargetRequest(s);
							}
						}
					}
				}

			}
		}
	}

	public void test() {
		Spider.create(new _7160SpiderTest()).addUrl("https://www.7160.com/rentiyishu/")
				.addPipeline(new ApiGallerySpiderPipeline()).run();
	}

	public static void main(String[] args) {
		new _7160SpiderTest().test();
	}

}