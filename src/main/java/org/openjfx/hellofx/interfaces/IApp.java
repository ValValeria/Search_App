package org.openjfx.hellofx.interfaces;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openjfx.hellofx.Result;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import store.Store;

public interface IApp {
	
	 public default void processResponse(String body) throws Throwable {
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
	
	public default String getBody(String url,String text) throws Throwable {
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