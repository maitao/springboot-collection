package com.mt.collection.processor._7160;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ConsolePipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(ConsolePipeline.class);

	String filePath = "C:\\mt\\workspace_upload\\spider\\7160\\pic";

	@Override
	public void process(ResultItems resultItems, Task task) {
		
		String title = resultItems.get("title");
		String imgUrl = resultItems.get("imgUrl");
		logger.info("下载：："+title+"=========imgUrl:"+imgUrl);
		String[] ss = imgUrl.split("\\.");
		String fileType = ss[ss.length - 1];
		
		try {
			ImageDownloadUtils.downLoadImage(imgUrl, filePath, title, fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}