package com.mt.collection.processor.T66yPic;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

/**
 * Created by Administrator on 2016/8/24.
 */
public class T66yMain {

    public static void main(String[] args) {
        Spider.create(new T66yImgProcessor())
                .addUrl( "http://x1.rsncjdnr.top/pw/thread.php?fid=14&page=1")
                .addPipeline(new T66yPipeLine())
                .setScheduler(new FileCacheQueueScheduler("C:\\mt\\workspace_upload\\spider\\olumes"))
                .thread(10)
                .run();
    }

}
