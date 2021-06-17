/**
 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package cn.iscas;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author wuheng@iscas.ac.cn
 *
 */
public class HttpClient {

	private static Integer socketTime = null;
    private static Integer connectTime = null;
    private static Integer connectionRequestTime = null;
    private static Logger logger = Logger.getLogger(HttpClient.class);
    private static String token = null;

    public static JSONObject doPost(JSONObject params, String url) {
        if (socketTime == null) {
            Properties properties = null;
            socketTime = Integer.valueOf(properties.getProperty("socketTime"));
            connectTime = Integer.valueOf(properties.getProperty("connectTime"));
            connectionRequestTime = Integer.valueOf(properties.getProperty("connectionRequestTime"));

        }
        org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Connection", "Close");
            StringEntity entity = new StringEntity(params.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            String resultString = EntityUtils.toString(response.getEntity());
            JSONObject rempResult = JSON.parseObject(resultString);
            return rempResult;
        } catch (Exception e) {
            logger.error("error", e);
            return null;
        }
    }

    public static JSONObject postFrom(JSONObject params, List<String> paths, String url) {
        org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringBody comment = new StringBody(params.toString(), ContentType.TEXT_PLAIN);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (String filePath : paths) {
                multipartEntityBuilder.addPart("file", new FileBody(new File(filePath)));
            }
            HttpEntity reqEntity = multipartEntityBuilder.addPart("desc", comment).build();
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            String resultString = EntityUtils.toString(response.getEntity());
            JSONObject result = JSON.parseObject(resultString);
            return result;
        } catch (Exception e) {
            logger.error("error", e);
            return null;
        }
    }

      public static JSONObject postFromListRate(JSONObject params, List<String> paths, String url) {
        org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringBody comment = new StringBody(params.toString(), ContentType.TEXT_PLAIN);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (String filePath : paths) {
                multipartEntityBuilder.addPart("file", new LimitRateFileBody(new File(filePath), params.getInteger("1024")));
            }
            HttpEntity reqEntity = multipartEntityBuilder.addPart("desc", comment).build();
            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);
            String resultString = EntityUtils.toString(response.getEntity());
            JSONObject result = JSON.parseObject(resultString);
            return result;
        } catch (Exception e) {
            logger.error("error", e);
            return null;
        }
    }
}
