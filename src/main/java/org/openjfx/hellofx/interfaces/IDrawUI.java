package org.openjfx.hellofx.interfaces;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
	public final static String BACKGROUND_COLOR = "#1A2026";

	public default void addLabel(VBox box) {
		Label label = new Label("Find and download your image".toUpperCase());
		label.setStyle("-fx-font-size:30px;-fx-text-fill:white;");
		box.getChildren().add(label);
		VBox.setMargin(label, new Insets(10, 10, 30, 10));
	}

	public default FlowPane addResultBar(VBox box2, FlowPane fl) {
		String styles = "-fx-min-width:300px;" + "-fx-width:90%;" + "-fx-height:300px;"
				+ String.format("-fx-background-color:%s;", IDrawUI.BACKGROUND_COLOR);
		fl.setStyle(styles);
		return fl;
	}

	public default void addTextField(VBox box2, EventHandler<ActionEvent> handler) {

		FlowPane fl = new FlowPane(20, 20);
		fl.setAlignment(Pos.CENTER);

		TextField txt = new TextField();
		txt.setStyle("-fx-max-width:600px;" + "-fx-min-width:300px;" + "-fx-width:70%;" + "-fx-padding:10px;"
				+ "-fx-font-size:16px;");

		txt.textProperty().addListener((observable, oldVal, newVal) -> {
			Store.text = newVal;
		});

		Button btn = new Button("search");
		btn.setStyle("-fx-text-fill:white;" + "-fx-background-color:#f7b731;" + "-fx-font-size:20px;"
				+ "-fx-padding:5 25px;");

		btn.setOnAction(handler);

		fl.getChildren().addAll(txt, btn);
		box2.getChildren().add(fl);
	}

	public default FlowPane showImages(VBox box) throws Throwable {
			FlowPane pn = new FlowPane();
			pn.setAlignment(Pos.CENTER);

			ExecutorService service = Executors.newFixedThreadPool(PER_PAGE);
			List <Callable<byte[]>> tasks = new ArrayList<>();
			
			Callable<byte[]> callableTask = ()->{
				 String url = Store.list.pollFirst().url;
				 return this.getBodyBytes(url, "");
			};

			for (int i = 0; i < Store.list.size() && i<PER_PAGE; i++) {
				 tasks.add(callableTask);
			}
					
			for (Future<byte[]> future : service.invokeAll(tasks)) {
				ImageView view = processImage(future.get());
				pn.getChildren().add(view);
				FlowPane.setMargin(view, new Insets(5, 5, 5, 5));
			}
			
			service.shutdown();
						
	    	return this.addResultBar(box,pn);
		}

		public default ImageView processImage(byte[] data) {
			ByteArrayInputStream stream = new ByteArrayInputStream(data);
			Image image = new Image(stream, 200, 200, false, true);
			ImageView imageView = new ImageView(image);
			return imageView;
		}
}
