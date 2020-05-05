package com.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpController  {
    public static final String BASE_URL  = "http://localhost:4000/";

    public static String Post(String url , String body) throws ClientProtocolException, IOException {
        String final_url = BASE_URL + url;
        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpPost post          = new HttpPost(final_url);
        StringEntity postingString = new StringEntity(body);

        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");

        HttpResponse response = httpClient.execute(post);

        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

    public static String Get(String url ) throws ClientProtocolException, IOException {
        String final_url = BASE_URL + url;
        HttpClient httpClient    = HttpClientBuilder.create().build();
        HttpGet get          = new HttpGet(final_url);
        get.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(get);

        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
