package org.openjfx.hellofx.interfaces;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openjfx.hellofx.Result;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import store.Store;

public interface IDrawUI extends IApp {
	  
		public default void addLabel(VBox box) {
			Label label = new Label("Find and download your image".toUpperCase());	
			label.setStyle("-fx-font-size:30px;");
			box.getChildren().add(label);
			VBox.setMargin(label, new Insets(10,10,30,10));
		}  
		
		public default ScrollPane addResultBar(VBox box2,FlowPane fl) {
			ScrollPane panel = new ScrollPane(fl);
			panel.setStyle("-fx-max-width:600px;"
				    + "-fx-min-width:300px;"
				    + "-fx-width:90%;"
				    + "-fx-height:300px;"
				    + "-fx-background-color:white;");
			panel.setHbarPolicy(ScrollBarPolicy.ALWAYS);
			return panel;
		}

		public default void addTextField(VBox box2, EventHandler<ActionEvent> handler) {
			
			FlowPane fl = new FlowPane(20,20);
			fl.setAlignment(Pos.CENTER);
			
			TextField txt = new TextField();
			txt.setStyle("-fx-max-width:600px;"
					    + "-fx-min-width:300px;"
					    + "-fx-width:70%;"
					    + "-fx-padding:10px;"
					    + "-fx-font-size:16px;");
			
			txt.textProperty().addListener((observable,oldVal,newVal)->{
				Store.text = newVal;
			});
			
			Button btn = new Button("Search");
			btn.setStyle("-fx-text-fill:white;"
					+ "-fx-background-color:#2980b9;"
					+ "-fx-font-size:20px;"
					+ "-fx-background-radius: 20px;"
					+ "-fx-padding:5 25px;"
					);
			
			btn.setOnAction(handler);
			
			fl.getChildren().addAll(txt,btn);
		    box2.getChildren().add(fl);
		}
		
		public default void showImages(VBox box,FlowPane pn) throws Throwable {
	    	ExecutorService service = Executors.newFixedThreadPool(5);
	    	ArrayList <Future<String>> tasks = new ArrayList<>();
	    			
			for(Result result:Store.list) {
				tasks.add(service.submit(()->{
					try {
						return getBody(result.url,"");
					} catch (Throwable e) {
						e.printStackTrace();
					}
					return null;
				}));
			}   	
			
			for(Future<String> future:tasks) {
				pn.getChildren().add(processImage((String)future.get()));
			}
									
			service.shutdown();
			
	    	this.addResultBar(box,pn);
		}

		public default ImageView processImage(String data) {
			byte [] bytes = data.getBytes();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			Image image = new Image(inputStream,100, 200, false, true);
			ImageView imageView = new ImageView(image);
			return imageView;
		}
}
