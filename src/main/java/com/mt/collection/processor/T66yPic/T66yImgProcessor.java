package com.mt.collection.processor.T66yPic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by Administrator on 2016/8/23.
 */
public class T66yImgProcessor implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(T66yImgProcessor.class);

	private Site site = Site.me().setRetryTimes(5).setSleepTime(500).setTimeOut(1 * 60 * 1000)
			.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
			.setCharset("gb2312");

	@Override
	public void process(Page page) {
		// 获取当前页面上的帖子地址
		List<String> postUrls = page.getHtml().xpath("//tr[@class='tr3 t_one']//h3/a/@href").all();
		List<String> postUrls1 = new ArrayList<String>();
		for (String string : postUrls) {
			postUrls1.add("http://x1.rsncjdnr.top/" + string);
		}
		for (String string : postUrls1) {
			logger.info("postUrls:" + string);
		}

		// 将帖子地址加入要抓取的url队列
		page.addTargetRequests(postUrls1);
		// 抓取其他页
		List<String> otherPageUrls = page.getHtml().links()
				.regex(" http://x1\\.rsncjdnr\\.top/pw/thread\\.php\\?fid=14\\&page=\\d+").all();
		page.addTargetRequests(otherPageUrls);

		// 进入帖子后，获取帖子标题
		String postName = page.getHtml().xpath("//span[@class='subject_tpc']/text()").toString();
		if (postName != null) {
			logger.info(postName + " " + page.getUrl());
			// 进入帖子之后，帖子内容，即要抓取的图片url
			List<String> postContent = page.getHtml().xpath("//div[@class='tpc_content']//img/@src").all();
			page.putField("postContent", postContent);
			page.putField("postName", postName);
			for (String string : postContent) {

				logger.info("imgurl:" + string);
			}
		}
	}

	@Override
	public Site getSite() {
		return site;
	}
}
