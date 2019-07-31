package com.mt.collection.processor.zjsfgkw;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * @author tao_m
 * @dec 获取post数据
 *
 */
public class SpiderTest implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(SpiderTest.class);

	// 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
	// 先从浏览器中分析出隐藏请求可得出以下匹配规则
	private static final String URLRULE_1 = "http://www\\.zjsfgkw\\.cn/Notice/NoticeSD";

	private static final String URLRULE_2 = "http://www\\.zjsfgkw\\.cn/TrialProcess/NoticeSDInfo/\\w+";

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		// if (page.getUrl().regex(URLRULE).match()) {
		// // 通过jsonpath得到json数据中的id内容，之后再拼凑待爬取链接
		// List<String> endUrls = new
		// JsonPathSelector("$.data[*]._id").selectList(page.getRawText());
		// if (CollectionUtils.isNotEmpty(endUrls)) {
		// for (String endUrl : endUrls) {
		// page.addTargetRequest(firstUrl + endUrl);
		// }
		// }
		// } else {
		// page.putField("title", new
		// JsonPathSelector("$.list[*].CaseNo").select(page.getRawText()));
		// }
		if (page.getUrl().regex(URLRULE_1).match()) {
			List<String> Notice_SD_IDs = new JsonPathSelector("$.list[*].Notice_SD_ID").selectList(page.getRawText());
			for (String string : Notice_SD_IDs) {
				String path = "http://www.zjsfgkw.cn/TrialProcess/NoticeSDInfo/" + string;
				logger.info(path + "=====" + page.getUrl() + "  Notice_SD_ID:" + string);

				page.addTargetRequest(path);

			}
		}

		if (page.getUrl().regex(URLRULE_2).match()) {
			String content = page.getHtml().xpath("//div[@class='noticeContentDetail']/tidyText()").toString();
			logger.info(page.getUrl() + "content:" + content);
		}

	}

	public static void main(String[] args) {
		Spider spider = Spider.create(new SpiderTest());
		for (int i = 1; i < 2; i++) {
			Request request = new Request("http://www.zjsfgkw.cn/Notice/NoticeSD");
			request.setMethod(HttpConstant.Method.POST);
			request.setRequestBody(HttpRequestBody.json("{'pagesize':10,'cbfy':'','pageno':" + i + "}", "utf-8"));
			spider.addRequest(request);
		}
		spider.thread(1).run(); // 开多个线程会出现数据重复
	}
}