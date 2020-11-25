package org.openjfx.hellofx.interfaces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.openjfx.hellofx.Result;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import store.Store;

public interface IDrawUI extends IApp {
	public final static String BACKGROUND_COLOR = "#1A2026";

	public default FlowPane addSpinner() {
		ProgressIndicator progress = new ProgressIndicator();
		FlowPane pn = new FlowPane();
		pn.setAlignment(Pos.CENTER);
		pn.setMinWidth(400);
		pn.getChildren().add(progress);
		return pn;
	}

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

		Tooltip tooltip = new Tooltip("Please type valid text");

		TextField txt = new TextField();
		txt.setStyle("-fx-max-width:600px;" + "-fx-min-width:300px;" + "-fx-width:70%;" + "-fx-padding:10px;"
				+ "-fx-font-size:16px;");

		txt.setTooltip(tooltip);

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

	public default CompletableFuture<List<byte[]>> showImages(VBox box, FlowPane spinner, FlowPane pn) throws Throwable {
		pn.getChildren().clear();

		List<CompletableFuture<byte[]>> list = Store.list.stream().map(v -> {
			byte[] bytes_data={};

			try {
				bytes_data = this.getBodyBytes(v.url, "");
			} catch (URISyntaxException | IOException | InterruptedException e) {}
			
			return CompletableFuture.completedFuture(bytes_data);
		}).collect(Collectors.toList());

		CompletableFuture<Void> future = CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));

		CompletableFuture<List<byte[]>> data = future.thenApply(v -> {
		    return list.stream().map((v1)->v1.join()).collect(Collectors.toList());
        });
	
		this.setVisible(pn,true);
		this.setVisible(spinner, false);
		return data;
		}

		public default ImageView processImage(byte[] data) {
			ByteArrayInputStream stream = new ByteArrayInputStream(data);
			Image image = new Image(stream, 200, 200, false, true);
			ImageView imageView = new ImageView(image);
			return imageView;
		}

		public default void setVisible(Node node,Boolean isVisible){
			node.setVisible(isVisible);
			node.setManaged(isVisible);
			node.setStyle("-fx-min-height:200px;");
		}
}
