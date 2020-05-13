package com.jason.bootswagger2.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具类
 */
public class HttpUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 创建CloseableHttpClient
     * @return
     */
    private static CloseableHttpClient createCloseableHttpClient(){
        CloseableHttpClient closeableHttpClient = null;
        try {
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, (arg0, arg1) -> true ).build();
            LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1", "TLSv1.2"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // 创建CloseableHttpClient
            closeableHttpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            logger.error("创建CloseableHttpClient错误: " + e);
            e.printStackTrace();
        }
        return closeableHttpClient;
    }

    /**
     * post请求
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static String postString(String url, Map<String, Object> map) throws Exception {
        try {
            //创建httpPost
            HttpPost httpPost = new HttpPost(url);
            //设置请求和传输超时时间
            httpPost.setConfig(RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(5000).build());
            // 创建参数队列
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            //拼装请求参数
            map.forEach((k,v)->{
                if (null != v && !v.toString().equalsIgnoreCase(""))
                    formparams.add(new BasicNameValuePair(k, String.valueOf(v)));
            });
            httpPost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
            logger.debug("发送HttpPost请求...... [ url地址：[{}] 请求参数：[{}] ]",url,JSON.toJSONString(map));
            //获取CloseableHttpClient 执行 post请求
            HttpResponse httpResponse = createCloseableHttpClient().execute(httpPost);
            // getEntity()
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String resStr = EntityUtils.toString(httpEntity, "UTF-8");
                logger.debug("响应的原数据:>>>[{}]",resStr);
                return resStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("响应错误: [{}]",e);
            throw e;
        }
        return null;
    }

    /**
     * get请求
     * @param url
     * @return
     * @throws Exception
     */
    public static String getString(String url) throws Exception {
        try {
            HttpGet httpGet = new HttpGet(url);
            //httpGet.setConfig(RequestConfig.DEFAULT);
            //设置请求和传输超时时间
            httpGet.setConfig(RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(5000).build());
            logger.debug("发送HttpGet请求...... [ url地址：[{}] ]",url);

            //获取CloseableHttpClient 执行 get请求
            HttpResponse httpResponse = createCloseableHttpClient().execute(httpGet);
            // getEntity()
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String resStr = EntityUtils.toString(httpEntity, "UTF-8");
                logger.debug("响应的原数据:>>>[{}]",resStr);
                return resStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("响应错误: [{}]",e);
            throw e;
        }
        return null;
    }

    /**
     * 相应Json
     * @param url
     * @param map
     * @return
     */
    public static JSONObject postJson(String url, Map<String, Object> map) {
        try {
            String resultStr = postString(url,map);
            JSONObject jsonObj = JSON.parseObject(resultStr);
            logger.debug("响应: " + JSON.toJSONString(jsonObj));
            return jsonObj;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("请求错误: [{}]",e);
        }
        return null;
    }

}
