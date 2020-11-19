package org.openjfx.hellofx.interfaces;
import org.openjfx.hellofx.Result;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import store.Store;

public interface IDrawUI extends IApp {
	    public final Integer PER_PAGE = 3;

		public default void addLabel(VBox box) {
			Label label = new Label("Find and download your image".toUpperCase());	
			label.setStyle("-fx-font-size:30px;");
			box.getChildren().add(label);
			VBox.setMargin(label, new Insets(10,10,30,10));
		}  
		
		public default FlowPane addResultBar(VBox box2,FlowPane fl) {
			String styles = "-fx-min-width:300px;"
				    + "-fx-width:90%;"
				    + "-fx-height:300px;"
				    + "-fx-background-color:white;";
			fl.setStyle(styles);
			return fl;
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
		
		public default FlowPane showImages(VBox box) throws Throwable {
			FlowPane pn = new FlowPane();
			pn.setAlignment(Pos.CENTER);
	    			
			for(Result result:Store.list) {
				int index = Store.list.indexOf(result);
				if(index+1>PER_PAGE){
                    break;
				}
				ImageView view = processImage(result.url);
				pn.getChildren().add(view);
				FlowPane.setMargin(view, new Insets(5, 5, 5, 5));
			}   	
						
	    	return this.addResultBar(box,pn);
		}

		public default ImageView processImage(String url) {
			Image image = new Image(url, 200, 200, false, true);
			ImageView imageView = new ImageView(image);
			return imageView;
		}
}
