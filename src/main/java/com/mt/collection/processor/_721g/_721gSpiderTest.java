package com.mt.collection.processor._721g;

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
public class _721gSpiderTest implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(_721gSpiderTest.class);

	public _721gSpiderTest() {
	}

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
	// 先从浏览器中分析出隐藏请求可得出以下匹配规则
	private static final String URLRULE = "http://www\\.721g\\.com/guonei/index\\w*.html";

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {

		List<String> SpidertURL = new ArrayList<String>();

		for (int i = 1; i < 4; i++) {// 添加到目标url中
			if(i==1) {
				SpidertURL.add("http://www.721g.com/guonei/index.html");
			} else {
				SpidertURL.add("http://www.721g.com/guonei/index_" + i + ".html");
			}
		}
		page.addTargetRequests(SpidertURL);
		if (page.getUrl().regex(URLRULE).match()) {
			List<String> detailURL = page.getHtml().xpath("//div[@class='item_b clearfix']//a").links()
					.all();
			page.addTargetRequests(detailURL);
		} else {
			
			String title = page.getHtml().xpath("//div[@class='photo']//h1/text()").toString();
			logger.info("title:" + title);
			if (title != null) {
				String nowpage = page.getHtml().xpath("//div[@class='photo']//h1//span[@class='nowpage']/text()").toString();
				List<String> imgs = page.getHtml().xpath("//div[@id='big-pic']//img/@src").all();
				for (String string : imgs) {
					page.putField("title", title+"_"+nowpage);
					page.putField("imgUrl", string);
				}
				List<String> pageNums = page.getHtml().xpath("//div[@class='pages']//a/text()").all();
				if (pageNums != null && pageNums.size() != 0) {
					for (int j = 2; j < pageNums.size() - 2; j++) {
						String s = page.getUrl().toString().replace(".html", "_" + j + ".html");
						logger.info(page.getUrl() + " ppppp:" + s );

						page.addTargetRequest(s);
					}
				}
			}
		}
	}

	public void test() {
		Spider.create(new _721gSpiderTest()).addUrl("http://www.721g.com/guonei/").addPipeline(new ConsolePipeline())
				.run();
	}

	public static void main(String[] args) {
		new _721gSpiderTest().test();
	}

}