package com.mt.collection.processor._4chan;

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
public class _4chanSpiderTest implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(_4chanSpiderTest.class);

	public _4chanSpiderTest() {
	}

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
	// 先从浏览器中分析出隐藏请求可得出以下匹配规则
	private static final String URLRULE = "http://boards\\.4chan\\.org/s/archive";

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		if (page.getUrl().regex(URLRULE).match()) {
			List<String> detailURL = page.getHtml().xpath("//table[@class='flashListing']//a").links().all();
			page.addTargetRequests(detailURL);
		} else {

			List<String> imgs = page.getHtml().xpath("//div[@class='thread']//img/@src").all();
			if (imgs != null) {
				for (String string : imgs) {
					if(!string.equals("//s.4cdn.org/image/archived.gif")) {
						String[] ss = string.split("\\.");
						String newPath = string.replace("s."+ss[ss.length-1], "."+ss[ss.length-1]);
						page.putField("imgUrl", "http:"+newPath);
					}
				}
			}
		}
	}

	public void test() {
		Spider.create(new _4chanSpiderTest()).addUrl("http://boards.4chan.org/s/archive")
				.addPipeline(new ConsolePipeline()).run();
	}

	public static void main(String[] args) {
		new _4chanSpiderTest().test();
	}

}