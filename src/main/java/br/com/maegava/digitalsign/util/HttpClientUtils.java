package br.com.maegava.digitalsign.util;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class HttpClientUtils {
	
	private final static int STATUS_OK = 200;
	private final static int STATUS_CREATED = 201;
	
	private static HttpClient http = HttpClientBuilder.create().build();
	
	private static boolean validStatusCode(int statusCode) {
		boolean status = false;
		
		switch(statusCode) {
		case STATUS_OK:
			status = true;
			break;
		case STATUS_CREATED:
			status = true;
			break;
		default:
			status = false;
		}
		
		return status;
	}
	
	private static JSONObject HttpRequest(HttpUriRequest req) throws Exception {
		HttpResponse response = http.execute(req);
		if(!validStatusCode(response.getStatusLine().getStatusCode()))
			throw new Exception("Error on Http Request: " + new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8")).getString("message"));
		return new JSONObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
	}
	
	public static JSONObject post(String url, Map<String,String> headers, StringEntity body) throws Exception {
		HttpPost post = null;
		JSONObject result = null;
		
		post = new HttpPost(url);
		for(Map.Entry<String, String> header : headers.entrySet()) {
			post.addHeader(header.getKey(), header.getValue());
		}
		
		post.setEntity(body);
		
		result = HttpRequest(post);
		
		return result;
	}
	
	public static JSONObject get(String url, Map<String,String> headers) throws Exception {
		HttpGet get = null;
		JSONObject result = null;
		
		get = new HttpGet(url);
		
		for(Map.Entry<String, String> header : headers.entrySet()) {
			get.addHeader(header.getKey(), header.getValue());
		}
			
		result = HttpRequest(get);
		
		return result;
	}
}
