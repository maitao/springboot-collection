package com.mt.collection.processor._4chan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ConsolePipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(ConsolePipeline.class);

	String filePath = "C:\\mt\\workspace_upload\\spider\\4chan\\pic\\s";

	@Override
	public void process(ResultItems resultItems, Task task) {

		String imgUrl = resultItems.get("imgUrl");
		if (imgUrl != null) {
			String name = String.valueOf(System.currentTimeMillis());
			logger.info("下载imgUrl:" + imgUrl + " name " + name);
			String[] ss = imgUrl.split("\\.");
			String fileType = ss[ss.length - 1];
			try {
				ImageDownloadUtils.downLoadImage(imgUrl, filePath, name, fileType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}