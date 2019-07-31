package com.mt.collection.processor.meizitu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

/**
 * Created by Administrator on 2016/8/27.
 */
public class ImgPipeline extends FilePersistentBase implements Pipeline {

	private Logger logger = LoggerFactory.getLogger(getClass());

	String filePath = "C:\\mt\\workspace_upload\\spider\\mzitu\\pic";

	public ImgPipeline() {
		setPath("/data/webmagic/");
	}

	public ImgPipeline(String path) {
		setPath(path);
	}

	public void process(ResultItems resultItems, Task task) {
		String url = resultItems.get("img");
		if (url != null) {

			try {
				logger.info("imgUrl===" + url);
				ImageDownloadUtils.downLoadImage(url, filePath, String.valueOf(System.currentTimeMillis()), "png");
			} catch (Exception e) {
				logger.info(""+e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
