package org.openjfx.hellofx.interfaces;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayDeque;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openjfx.hellofx.Result;
import store.Store;


public interface IApp {

	 public final Integer PER_PAGE = 3;

	/**
	 * Adds urls to collection, located in the class, named `Store`
	 * @param body
	 * @throws JSONException
	 */
	 public default Collection<Result> processResponse(String body) throws JSONException {
		JSONArray json = (new JSONObject(body)).getJSONObject("photos").getJSONArray("results");
		Collection<Result> list = new ArrayDeque<>();
		
        for(int i=0;i<json.length() && i<PER_PAGE;i++) {
        	JSONObject obj = json.getJSONObject(i);
            list.add(new Result(obj));
        }
        
		Store.list.clear();
		Store.list.addAll(list);
		return Store.list;
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

	public default byte[] getBodyBytes(String url, String text) throws URISyntaxException, IOException, InterruptedException {
		HttpRequest request = HttpRequest
							 .newBuilder()
							 .uri(new URI(url + URLEncoder.encode(text, "UTF-8"))).GET()
				             .build();

		HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());

		byte[] body = response.body();

		return body;
	}	
}
