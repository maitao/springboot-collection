package com.mt.collection.processor.ishawu;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.domain.IShaWuRepository;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
 */
/**
 * @author tao_m
 * @dec 网站大量使用的是百度的图片，单提取图片url，在本网打不开，百度有防图片外站打开
 *
 */
public class IShaWuSpiderTest implements PageProcessor, Runnable {

	private static Logger logger = LoggerFactory.getLogger(IShaWuSpiderTest.class);

	IShaWuRepository iShaWuRepository;

	public IShaWuSpiderTest(IShaWuRepository iShaWuRepository) {
		this.iShaWuRepository = iShaWuRepository;
	}

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(300);
	// 先从浏览器中分析出隐藏请求可得出以下匹配规则
	private static final String URLRULE = "http://www\\.ishawu\\.com/\\?index-\\w+.htm";

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {

		// TODO Auto-generated method stub
		List<String> SpidertURL = new ArrayList<String>();

		for (int i = 200; i < 216; i++) {// 添加到目标url中
			SpidertURL.add("http://www.ishawu.com/?index-" + i + ".htm");
		}
		// 添加url到请求中
		page.addTargetRequests(SpidertURL);

		/**
		 * 筛选出所有符合条件的url，手动添加到爬取队列。
		 */
		if (page.getUrl().regex(URLRULE).match()) {
			// 通过jsonpath得到json数据中的id内容，之后再拼凑待爬取链接
			List<String> detailURL = page.getHtml().xpath("//ul[@class='list-unstyled']/li/div/div/a").links().all();
			int x = 1;
			for (String str : detailURL) {// 输出所有连接
				//System.out.println(x + "----" + str);
				x++;
			}
			page.addTargetRequests(detailURL);
		} else {

			String title = page.getHtml().xpath("//div[@class='card-body'][1]//h4/text()").toString();
			String author = page.getHtml().xpath("//div[@class='card-body'][1]//span[@class='username']/a/text()")
					.toString();
			String date = page.getHtml()
					.xpath("//div[@class='card-body'][1]//span[@class='date text-grey ml-2']/text()").toString();
			String viewCount = page.getHtml()
					.xpath("//div[@class='card-body'][1]//span[@class='text-grey ml-2']/text()").toString();

			// String content = page.getHtml().xpath("//div[@class='message
			// break-all']/text()").toString();
			String content = page.getHtml().xpath("//div[@class='message break-all']/tidyText()").toString();

			List<String> imgs = page.getHtml().xpath("//div[@class='message break-all']//img/@src").all();

			// System.out.println("title----" + title);
			// System.out.println("content----" + content);

			// 通过jsonpath从爬取到的json数据中提取出id和content内容
			// page.putField("title", new
			// JsonPathSelector("$.data.title").select(page.getRawText()));
			// page.putField("content", new
			// JsonPathSelector("$.data.content").select(page.getRawText()));
			page.putField("url", page.getUrl().toString());
			page.putField("title", title);
			page.putField("author", author);
			page.putField("date", date);
			page.putField("viewCount", Integer.valueOf(viewCount.trim()));
			page.putField("content", content);
			page.putField("imgs", imgs);

		}

	}

	@Override
	public void run() {
		Spider.create(new IShaWuSpiderTest(iShaWuRepository)).addUrl("http://www.ishawu.com/?index-1.htm")
				.addPipeline(new ConsolePipeline()).addPipeline(new MongoDBPipeline(iShaWuRepository)).run();
	}

	public void test() {
		Spider.create(new IShaWuSpiderTest(iShaWuRepository)).addUrl("http://www.ishawu.com/?index-1.htm")
				.addPipeline(new ApiIShaWuSpiderPipeline()).run();
	}

	public static void main(String[] args) {
		new IShaWuSpiderTest(null).test();
	}

}