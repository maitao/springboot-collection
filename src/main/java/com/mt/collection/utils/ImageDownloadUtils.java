package com.mt.collection.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageDownloadUtils {
	private static Logger logger = LoggerFactory.getLogger(ImageDownloadUtils.class);

    public static synchronized void downLoadImage(String url,String filePath,String fileName,String picType) throws Exception{
        logger.info("----------文件开始下载-----------url:"+url + "  fileName:" + fileName + " picType:"+picType);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if(url==null || url.equals(""))
            return;
        //创建目标才存储目录
        File desPathFile = new File(filePath);
        System.out.println("filePath:"+filePath);
        if(!desPathFile.exists()){
            desPathFile.mkdirs();
            //创建此抽象路径指定的目录，包括所有必须但不存在的父目录
        }
        //得到文件绝对路径
        String fullPath = filePath+File.separator+fileName+"."+picType;
        System.out.println("完整路径:"+fullPath);
        System.out.println("下载url:"+url);
        logger.info("文件路径："+filePath);
        logger.info("文件名："+fileName);
        logger.info("源文件url"+url);
        //从源网址下载图片
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        //设置下载地址
        File file = new File(fullPath);

        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp,0,l);
            }
            fout.flush();
            fout.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } finally {

            in.close();
        }
        logger.info("-------------------文件下载结束---------------------");
    }
    
    
    public static void downloadFile(String imgUrl, String filePathDir,
			String fileName) throws Exception {
		// 目标目录
		File desPathFile = new File(filePathDir);
		if (!desPathFile.exists()) {
			desPathFile.mkdirs();
		}
		//文件绝对路径
		String fullPath = filePathDir + File.separator + fileName;
		File file = new File(fullPath);
		if(file.exists()){
			return ;
		}
		URL url = new URL(imgUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		con.setConnectTimeout(5*1000);
		int statusCode = con.getResponseCode();
		if(statusCode != 200){
			return ;
		}
		InputStream in = con.getInputStream();

		if(in != null){
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int len = -1;
			byte[] tmp = new byte[1024 * 1024];
			while ((len = in.read(tmp)) != -1) {
				fout.write(tmp, 0, len);
			}
			fout.flush();
			fout.close();
		} finally {
			in.close();
		}
	}
	}
}
