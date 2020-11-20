package org.openjfx.hellofx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import store.Store;
import java.io.IOException;
import org.openjfx.hellofx.interfaces.IDrawUI;


public class App extends Application implements IDrawUI {

	public final String TITLE = "App";
	public final int HEIGHT = 600;
	public final int WIDTH = 800;
	private VBox box;
	final String URL_SEARCH = "https://unsplash.com/napi/search?xp=feedback-loop-v2%3Acontrol&per_page=20&query=";
	private Object pane;

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
				try {
					sendHttpRequest();
				} catch (Throwable exception) {
					System.out.println(String.format("%s has occured. The message: %s", exception.getClass().getName(),
							exception.getMessage()));
				}
			}
		});

		stage.setTitle(this.TITLE);
		stage.setMaxWidth(this.WIDTH + 100);
		stage.setMinWidth(this.WIDTH);
		stage.setMinHeight(this.HEIGHT);
		stage.setMaxHeight(this.HEIGHT+200);
		stage.setScene(scene);
		stage.show();
	}

	protected void sendHttpRequest() throws Throwable {
		String text = Store.text;

		if (!text.isEmpty()) {

			if (this.box.getChildren().contains(this.pane)) {
				this.box.getChildren().remove(this.pane);
			}

			ProgressIndicator progress = new ProgressIndicator();
			this.box.getChildren().add(progress);

			this.processResponse(this.getBody(this.URL_SEARCH, text));
			this.pane = this.showImages(box);

			this.box.getChildren().remove(progress);
			this.box.getChildren().add((Node) this.pane);
		}		
	}	
	
    public static void main(String[] args) {
        launch();
    }
}

