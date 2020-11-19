package org.openjfx.hellofx.interfaces;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openjfx.hellofx.Result;
import store.Store;


public interface IApp {
	
	 public default void processResponse(String body) throws JSONException {
		JSONArray json = (new JSONObject(body)).getJSONObject("photos").getJSONArray("results");
		ArrayList<Result> list = new ArrayList<>();
		
        for(int i=0;i<json.length();i++) {
        	if(i>20) {
        		break;
        	}
        	JSONObject obj = json.getJSONObject(i);
            list.add(new Result(obj));
        }
        
        Store.list=list;
	}
	
	public default String getBody(String url,String text) throws URISyntaxException,IOException,InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(new URI(url+URLEncoder.encode(text, "UTF-8")))
				  .GET()
				  .build();
		
		HttpResponse<String> response = HttpClient.newHttpClient()
				  .send(request, HttpResponse.BodyHandlers.ofString());
		
		String body = response.body();
			
		return body;
	}
}

/** 22 - 23 */