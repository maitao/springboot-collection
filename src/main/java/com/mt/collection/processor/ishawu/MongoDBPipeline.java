package com.mt.collection.processor.ishawu;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mt.collection.domain.IShaWu;
import com.mt.collection.domain.IShaWuRepository;
import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class MongoDBPipeline implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(MongoDBPipeline.class);

	String filePath = "C:\\mt\\workspace_upload\\spider\\iShaWu\\pic";

	IShaWuRepository iShaWuRepository;

	public MongoDBPipeline(IShaWuRepository iShaWuRepository) {
		this.iShaWuRepository = iShaWuRepository;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		String url = resultItems.get("url");
		String author = resultItems.get("author");
		String title = resultItems.get("title");
		String date = resultItems.get("date");
		Integer viewCount = resultItems.get("viewCount");
		String content = resultItems.get("content");
		List<String> imgs = resultItems.get("imgs");
		String imgs_t = "";
		if (imgs != null && imgs.size() != 0) {
			for (String string : imgs) {
				logger.info("=========imgUrl:" + string);
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
				imgs_t = StringUtils.join(imgs.toArray(), ",");
			}
		}

		if (StringUtils.isNoneBlank(title)) {
			iShaWuRepository.save(new IShaWu(url, 
					author, title, date, viewCount, content, imgs_t));
		}

	}
}