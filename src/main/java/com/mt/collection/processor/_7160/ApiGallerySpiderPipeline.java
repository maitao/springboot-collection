package com.mt.collection.processor._7160;

import java.util.ArrayList;
import java.util.List;

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

public class ApiGallerySpiderPipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(ApiGallerySpiderPipeline.class);

	@Override
	public void process(ResultItems resultItems, Task task) {

		String page = resultItems.get("page");
		String title = resultItems.get("title");
		String date = resultItems.get("date");
		String author = resultItems.get("author");
		String imgUrl = resultItems.get("imgUrl");
		System.out.println("imgUrl:" + imgUrl);
		if (title != null && imgUrl != null) {
			try {
				httpclientPost(page, title, date, author, imgUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void httpclientPost(String page, String title, String date, String author, String imgUrl) throws Exception {

		// 创建HttpClient对象
		HttpClient client = HttpClients.createDefault();

		// 创建POST请求
		HttpPost post = new HttpPost("http://localhost:8099/api/galleries/spider/save");
		post.setHeader("token", "9d16d947cd02919edeb73d090959115c");
		// 创建一个List容器，用于存放基本键值对（基本键值对即：参数名-参数值）
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("galleryLocation", page));
		parameters.add(new BasicNameValuePair("galleryName", title));
		parameters.add(new BasicNameValuePair("galleryDate", date));
		parameters.add(new BasicNameValuePair("galleryDesc", author));
		parameters.add(new BasicNameValuePair("galleryThumbnailUrl", imgUrl));
		parameters.add(new BasicNameValuePair("galleryUrl", imgUrl));

		// 向POST请求中添加消息实体
		post.setEntity(new UrlEncodedFormEntity(parameters, "utf-8"));

		// 得到响应并转化成字符串
		HttpResponse response = client.execute(post);
		HttpEntity httpEntity = response.getEntity();
		String result = EntityUtils.toString(httpEntity, "utf-8");
		System.out.println(result);
	}
}