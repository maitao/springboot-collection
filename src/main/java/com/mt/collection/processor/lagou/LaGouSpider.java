package com.mt.collection.processor.lagou;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.processor.ishawu.IShaWuSpiderTest;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

public class LaGouSpider implements PageProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(LaGouSpider.class);

	int flag = 0;
	int mark = 0;
	int sun = 0;
	int sub = 0;
	int ty = 0;
	int tr = 0;
	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
			.addHeader("Accept-Encoding", "gzip, deflate, br")
			.addHeader("Accept-Language", "zh-CN,zh;q=0.9")
			.addHeader("Connection", "keep-alive")
			.addHeader("Content-Length", "23")
			.addHeader("Content-Type", "application/json;charset=UTF-8")
			.addHeader("Cookie", "JSESSIONID=ABAAABAABEEAAJAF2E8053E36967867DC30A9430DB8A271; user_trace_token=20190417114653-50b37e5f-b273-4875-9e6b-b0d440493f71; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1555472816; _ga=GA1.2.682334480.1555472816; _gid=GA1.2.377737097.1555472816; LGSID=20190417114654-7159558b-60c3-11e9-8820-525400f775ce; PRE_UTM=; PRE_HOST=; PRE_SITE=; PRE_LAND=https%3A%2F%2Fwww.lagou.com%2Fjobs%2Flist_Java%3Fpx%3Ddefault%26city%3D%25E5%258C%2597%25E4%25BA%25AC; LGUID=20190417114654-71595817-60c3-11e9-8820-525400f775ce; index_location_city=%E5%85%A8%E5%9B%BD; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%2216a2980eef85a-0bfa23ef48cb05-8383268-921600-16a2980eef926f%22%2C%22%24device_id%22%3A%2216a2980eef85a-0bfa23ef48cb05-8383268-921600-16a2980eef926f%22%7D; sajssdk_2015_cross_new_user=1; _putrc=D3DB137BB890D49B123F89F2B170EADC; login=true; unick=%E9%BA%A6%E9%9F%AC; gate_login_token=fba4d8e924642b2216956a7e6ed292ca2457ad2d8400e6f0eb20a45e59e04ac1; _gat=1; showExpriedIndex=1; showExpriedCompanyHome=1; showExpriedMyPublish=1; hasDeliver=0; TG-TRACK-CODE=index_hotsearch; X_HTTP_TOKEN=8c35908e58780d723554745551cd820eed37f2777f; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1555474555; LGRID=20190417121553-7da73560-60c7-11e9-8823-525400f775ce; SEARCH_ID=35db2556f78c4915b0e25ed5034f094d")
			.addHeader("Host", "www.lagou.com")
			.addHeader("Origin", "https://www.lagou.com")
			.addHeader("Referer", "https://www.lagou.com/jobs/list_Java?px=default&city=北京")
			.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
			.addHeader("X-Anit-Forge-Code", "0")
			.addHeader("X-Anit-Forge-Token", "None")
			.addHeader("X-Requested-With", "XMLHttpRequest");

	public void process(Page page) {

		this.processBeiJing(page);
		this.processTianJin(page);
		this.processChengDu(page);
		this.processDaLian(page);
		this.processShenYang(page);
		this.processXiAn(page);
		logger.info("pageUrl:" + page.getUrl());
		page.putField("positionname",
				new JsonPathSelector("$.content.positionResult.result[*].positionName").selectList(page.getRawText()));
		page.putField("workYear",
				new JsonPathSelector("$.content.positionResult.result[*].workYear").selectList(page.getRawText()));
		page.putField("salary",
				new JsonPathSelector("$.content.positionResult.result[*].salary").selectList(page.getRawText()));
		page.putField("address",
				new JsonPathSelector("$.content.positionResult.result[*].city").selectList(page.getRawText()));
		page.putField("district",
				new JsonPathSelector("$.content.positionResult.result[*].district").selectList(page.getRawText()));
		page.putField("createTime",
				new JsonPathSelector("$.content.positionResult.result[*].createTime").selectList(page.getRawText()));
		page.putField("companyName", new JsonPathSelector("$.content.positionResult.result[*].companyFullName")
				.selectList(page.getRawText()));
		page.putField("discription",
				new JsonPathSelector("$.content.positionResult.result[*].secondType").selectList(page.getRawText()));

	}

	public static void main(String[] args) {
		Spider.create(new LaGouSpider()).addPipeline(new LaGouPipe())
				.addUrl("https://www.lagou.com/jobs/positionAjax.json?px=default&city=北京&needAddtionalResult=false")
				.thread(2).run();
		// renderText("爬取完成");
	}

	// 爬取北京的java职位信息
	public void processBeiJing(Page page) {
		if (flag == 0) {

			Request[] requests = new Request[30];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=北京&needAddtionalResult=false");
				requests[i].setMethod(HttpConstant.Method.POST);
				if (i == 0) {
					map.put("first", "true");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				} else {
					map.put("first", "false");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}

			flag++;
		}
	}

	// 爬取天津的java职位信息
	public void processTianJin(Page page) {
		if (mark == 0) {
			Request[] requests = new Request[9];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=天津&needAddtionalResult=false&isSchoolJob=0");
				if (mark == 0) {
					map.put("first", true);
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setMethod(HttpConstant.Method.POST);
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);

				} else {
					map.put("first", false);
					map.put("kd", "java");
					map.put("pn", i + 1);
					requests[i].setMethod(HttpConstant.Method.POST);
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}
			mark++;
		}
	}

	public void processChengDu(Page page) {
		if (sun == 0) {

			Request[] requests = new Request[1];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=成都&needAddtionalResult=false&isSchoolJob=0");
				requests[i].setMethod(HttpConstant.Method.POST);
				if (i == 0) {
					map.put("first", "true");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				} else {
					map.put("first", "false");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}

			sun++;
		}

	}

	public void processXiAn(Page page) {
		if (sub == 0) {

			Request[] requests = new Request[1];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=西安&needAddtionalResult=false&isSchoolJob=0");
				requests[i].setMethod(HttpConstant.Method.POST);
				if (i == 0) {
					map.put("first", "true");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				} else {
					map.put("first", "false");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}

			sub++;
		}

	}

	public void processDaLian(Page page) {
		if (tr == 0) {

			Request[] requests = new Request[1];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=大连&needAddtionalResult=false&isSchoolJob=0");
				requests[i].setMethod(HttpConstant.Method.POST);
				if (i == 0) {
					map.put("first", "true");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				} else {
					map.put("first", "false");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}

			tr++;
		}

	}

	public void processShenYang(Page page) {
		if (ty == 0) {

			Request[] requests = new Request[1];
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < requests.length; i++) {
				requests[i] = new Request(
						"https://www.lagou.com/jobs/positionAjax.json?px=default&city=沈阳&needAddtionalResult=false&isSchoolJob=0");
				requests[i].setMethod(HttpConstant.Method.POST);
				if (i == 0) {
					map.put("first", "true");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				} else {
					map.put("first", "false");
					map.put("pn", i + 1);
					map.put("kd", "java");
					requests[i].setRequestBody(HttpRequestBody.form(map, "utf-8"));
					page.addTargetRequest(requests[i]);
				}
			}

			ty++;
		}

	}

	public Site getSite() {
		return site;
	}

}
