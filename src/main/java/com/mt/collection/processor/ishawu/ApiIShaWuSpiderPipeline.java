package com.mt.collection.processor.ishawu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ApiIShaWuSpiderPipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(ApiIShaWuSpiderPipeline.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void process(ResultItems resultItems, Task task) {

		String url = resultItems.get("url");
		String title = resultItems.get("title");
		String author = resultItems.get("author");
		String date = resultItems.get("date");
		String content = resultItems.get("content");
		List<String> imgUrls = resultItems.get("imgs");
		if (title != null) {
			if (imgUrls != null) {
				String s = StringUtils.join(imgUrls.toArray(), ",");
				try {
					// 格式化日期
					Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
					ca.setTime(new Date()); // 设置时间为当前时间
					if (date.contains("天前")) {
						ca.add(Calendar.DATE, -Integer.valueOf(date.replace("天前", "").trim())); // 年份减1
						date = sdf.format(ca.getTime()); // 结果
					} else if (date.contains("月前")) {
						// 格式化日期
						ca.add(Calendar.MONTH, -Integer.valueOf(date.replace("月前", "").trim())); // 年份减1
						date = sdf.format(ca.getTime()); // 结果
					}
					logger.info("date:" + date);
					httpclientPost(url, title, author, date, content, s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void httpclientPost(String url, String title, String author, String date, String content, String imgUrl) throws Exception {

		// 创建HttpClient对象
		HttpClient client = HttpClients.createDefault();

		// 创建POST请求
		HttpPost post = new HttpPost("http://localhost:8099/api/posts/spider/save");

		// 创建一个List容器，用于存放基本键值对（基本键值对即：参数名-参数值）
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		post.setHeader("token", "9d16d947cd02919edeb73d090959115c");
		parameters.add(new BasicNameValuePair("postUrl", url));
		parameters.add(new BasicNameValuePair("postType", "iShaWu"));
		parameters.add(new BasicNameValuePair("postTitle", title));
		parameters.add(new BasicNameValuePair("postAuthor", author));
		parameters.add(new BasicNameValuePair("postDate", date));
		parameters.add(new BasicNameValuePair("postContent", content));
		parameters.add(new BasicNameValuePair("postThumbnail", imgUrl));

		// 向POST请求中添加消息实体
		post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));

		// 得到响应并转化成字符串
		HttpResponse response = client.execute(post);
		HttpEntity httpEntity = response.getEntity();
		String result = EntityUtils.toString(httpEntity, "utf-8");
		System.out.println(result);
	}
}