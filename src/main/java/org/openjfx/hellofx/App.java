package org.openjfx.hellofx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import store.Store;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.openjfx.hellofx.interfaces.IApp;
import org.openjfx.hellofx.interfaces.IDrawUI;



public class App extends Application implements IDrawUI{

    public final String TITLE = "App";
	public final int HEIGHT = 400;
	public final int WIDTH = 800;
	private VBox box;
	final String URL_SEARCH="https://unsplash.com/napi/search?xp=feedback-loop-v2%3Acontrol&per_page=20&query=";
	final String FONT_COLOR="#2f3640";
	private FlowPane pn;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage stage) throws IOException {
    	this.box = new VBox(30);
    	this.box.setStyle("-fx-background-color:white;");
    	this.box.setAlignment(Pos.CENTER);
    	
    	Scene scene = new Scene(this.box);
    	
    	this.addLabel(box);
    	
    	this.addTextField(box,new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				  try {
					  sendHttpRequest();	
				  }catch(Throwable exception) {
					  System.out.println(String.format("%s has occured. The message: %s", exception.getClass().getName(),exception.getMessage()));
				  }
			}	
		});
    	
    	stage.setTitle(this.TITLE);
		stage.setMaxWidth(this.WIDTH+100);
		stage.setMinWidth(this.WIDTH);
		stage.setMinHeight(this.HEIGHT);
		stage.setScene(scene);
		stage.show();
    }	

	protected void sendHttpRequest() throws Throwable {
		String text = Store.text;

		if(!text.isEmpty()) {
			
			if(this.box.getChildren().contains(pn)) {
				this.box.getChildren().remove(pn);
			} 
			
			pn = new FlowPane();
	    	pn.setStyle("-fx-background-color:black;"
	    			+ "-fx-min-width:100%;");
	    	pn.setPadding(new Insets(20));
	    	
			this.processResponse(this.getBody(this.URL_SEARCH, text));
			this.showImages(box,pn);
		}		
	}	
	
	public static void main(String[] args) {
        launch();
    }
}

