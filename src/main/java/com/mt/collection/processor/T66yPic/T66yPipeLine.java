package com.mt.collection.processor.T66yPic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mt.collection.utils.ImageDownloadUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

/**
 * Created by Administrator on 2016/8/23.
 */
public class T66yPipeLine extends FilePersistentBase implements Pipeline {

	private static Logger logger = LoggerFactory.getLogger(T66yPipeLine.class);

	
    public T66yPipeLine() {
        setPath("/Volumes/Loading/temp/caoliu");
    }



    public T66yPipeLine(String path) {
        setPath(path);
    }

    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> fields = resultItems.getAll();
        List<String> postContent = (List<String>) fields.get("postContent");
        if(postContent != null){
            String postName = (String) fields.get("postName");
            for(String imgUrl: postContent){
                int indexOfSlash = imgUrl.lastIndexOf("/");
                String title = imgUrl.substring(indexOfSlash + 1);

                try {
                    title = URLEncoder.encode(title, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                imgUrl = imgUrl.substring(0, indexOfSlash + 1) + title;
                logger.info("imgUrl:"+imgUrl);
                postName = postName.replaceAll("<br>", "\\r\\n");
                String filePathDir = "/Volumes/Loading/temp/caoliu/" + postName;
                //logger.info(imgUrl);
                try {
                    ImageDownloadUtils.downloadFile(imgUrl, filePathDir, title);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

