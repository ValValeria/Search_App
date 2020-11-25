package org.openjfx.hellofx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import store.Store;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.openjfx.hellofx.interfaces.IDrawUI;


public class App extends Application implements IDrawUI {

	public final String TITLE = "App";
	public final int HEIGHT = 600;
	public final int WIDTH = 800;
	private VBox box;
	final String URL_SEARCH = "https://unsplash.com/napi/search?xp=feedback-loop-v2%3Acontrol&per_page=20&query=";
	private FlowPane pane;
	private FlowPane spinner;

	@SuppressWarnings("exports")
	@Override
	public void start(Stage stage) throws IOException {
		this.box = new VBox(30);
		this.box.setStyle("-fx-background-color:"+ BACKGROUND_COLOR+";");
		this.box.setAlignment(Pos.CENTER);
   
		Scene scene = new Scene(this.box);

		this.addLabel(box);

		this.addTextField(box, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
					sendHttpRequest();
		   }
		});
		
		this.pane = new FlowPane();
		this.addResultBar(box, this.pane);
		this.setVisible(this.pane, false);
		this.box.getChildren().add(this.pane);
		
		this.spinner = this.addSpinner();
		this.box.getChildren().add(spinner);
		this.setVisible(spinner, false);
		
		stage.setTitle(this.TITLE);
		stage.setMaxWidth(this.WIDTH + 100);
		stage.setMinWidth(this.WIDTH);
		stage.setMinHeight(this.HEIGHT);
		stage.setMaxHeight(this.HEIGHT+200);
		stage.setScene(scene);
		stage.show();
	}

	protected void sendHttpRequest() {
		String text = Store.text;

		if (!text.isEmpty()) {
			this.setVisible(spinner, true);	

			Platform.runLater(new Runnable(){
				@Override
				public void run(){
			          CompletableFuture.supplyAsync(() -> {
					    CompletableFuture<List<byte[]>> data = new CompletableFuture<List<byte[]>>();		
						try {
							processResponse(getBody(URL_SEARCH, text));
							data=showImages(box, spinner, pane);
						} catch (Throwable exp) {
							System.out.println(String.format("%s has occured. The message: %s",
									exp.getClass().getName(), exp.getMessage()));
						};
						return data;
					 }).thenAccept((v)->{
						 try {
							 v.get().forEach(item -> {
								ImageView image = processImage(item);
								pane.getChildren().add(image);
							});
						 } catch (Exception e) {						 }
					   });  
				}
			});
		}		
	}	

    public static void main(String[] args) {
        launch();
    }
}

