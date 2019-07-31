package com.mt.collection.processor.bilibili;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;

public class BiliBili implements PageProcessor {

	List<String> pagelist = new ArrayList<String>();
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

	// https://book.douban.com/tag/
	// 分页链接https://search.bilibili.com/all?keyword=%E4%BC%9A%E5%A3%B0%E4%BC%9A%E5%BD%B1&from_source=banner_search&page=4
	public void process(Page page) {

		for (int i = 1; i <= 50; i++) {
			String url = "https://search.bilibili.com/all?keyword=%E4%BC%9A%E5%A3%B0%E4%BC%9A%E5%BD%B1&single_column=0&page="
					+ i;
			pagelist.add(url);
		}
		page.addTargetRequests(pagelist);
		page.putField("videoname", new JsonPathSelector("$.result.video[*].tag").selectList(page.getRawText()));
		page.putField("videolink", new JsonPathSelector("$.result.video[*].arcurl").selectList(page.getRawText()));
		// page.putField("videolink",page.getHtml().xpath("//li[@class='video
		// matrix']/a/@href").all());

	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] argv) {
		Spider.create(new BiliBili()).addUrl(

				"https://search.bilibili.com/all?keyword=%E4%BC%9A%E5%A3%B0%E4%BC%9A%E5%BD%B1&single_column=0&page=1")
				.addPipeline(new BLSpider()).thread(2).run();
	}
}
