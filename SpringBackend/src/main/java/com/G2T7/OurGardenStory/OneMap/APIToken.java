package com.G2T7.OurGardenStory.OneMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;



public class APIToken {
    private static void setAccessToken() throws UnsupportedEncodingException, IOException {
        String endpoint = "https://developers.onemap.sg/privateapi/auth/post/getToken";
        String email = "gohweehan02@gmail.com";
        String password = "Gohweihan123;";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(endpoint);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse httpresponse = httpclient.execute(httpPost);
        int statusCode = httpresponse.getStatusLine().getStatusCode();
        System.out.println("Success Status: " + statusCode);
        if (statusCode >= 200 && statusCode <= 299) {
            Scanner sc = new Scanner(httpresponse.getEntity().getContent());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            String jsonStrResult = sb.toString();
            JSONObject responseObj = new JSONObject(jsonStrResult);
            String access_token = responseObj.getString("access_token");
                }
        httpclient.close();
    }
}
