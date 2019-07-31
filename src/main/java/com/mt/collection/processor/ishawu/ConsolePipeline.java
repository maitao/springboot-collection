package com.mt.collection.processor.ishawu;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ConsolePipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(ConsolePipeline.class);

	String filePath = "C:\\mt\\workspace_upload\\spider\\iShaWu\\pic";

	@Override
	public void process(ResultItems resultItems, Task task) {
//		System.out.println("get page: " + resultItems.getRequest().getUrl());
		// 遍历所有结果，输出到控制台，上面例子中的"author"、"name"、"readme"都是一个key，其结果则是对应的value
		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			//System.out.println("################  "+entry.getKey() + ":\t" + entry.getValue());
		}
		
		List<String> imgs = resultItems.get("imgs");
		if(imgs!=null&&imgs.size()!=0) {
			for (String string : imgs) {
				logger.info("=========imgUrl:"+string);
				String[] ss = string.split("\\.");
				String fileType = ss[ss.length - 1];
				String[] sss = string.split("/");
				String fileName = sss[sss.length - 1].split("\\.")[0];
				
				if (string.startsWith("upload")) {
					string = "http://www.ishawu.com/" + string;
				}
				try {
					ImageDownloadUtils.downLoadImage(string, filePath, fileName, fileType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}