package application;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@FXML
	Canvas canwas;
	@FXML
	Button start;

	Stage classStage = new Stage();
	private Timeline timeline;
	private AnimationTimer timer;

	@FXML
	private void startGame(javafx.event.ActionEvent event) throws IOException {
		new Game().start(new Stage());
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			classStage = primaryStage;
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("start.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			start = (Button) fxmlLoader.getNamespace().get("start");
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			canwas = (Canvas) fxmlLoader.getNamespace().get("canwas");

			GraphicsContext gc = canwas.getGraphicsContext2D();

			Image hare = new Image("assets\\zajaczek.png");
			gc.drawImage(hare, 0, 410);
			Image hare2 = new Image("assets\\zajaczekZ.png");
			Image bulldozer = new Image("assets\\bulldozer.png");
			Image backgroundStart = new Image("assets\\start2.png");
			Image blood = new Image("assets\\blood.png");
			Image blood2 = new Image("assets\\blood2.png");

			final long startNanoTime = System.nanoTime();
			new AnimationTimer() {
				public void handle(long currentNanoTime) {
					double t = (currentNanoTime - startNanoTime) / 1000000000.0;

					double x = 70 * t;
					double xx = 0;
					double xxx = 0;

					if (x > 160) {
						xx = 0.6 * (x - 150);
						gc.drawImage(bulldozer, -10, 400);
					}
					if (x > 200) {
						xxx = 2 * (xx - 530);
						gc.drawImage(hare2, -10, 410);
					}

					gc.clearRect(0, 0, 924, 519);
					gc.drawImage(backgroundStart, 0, 0);
					gc.drawImage(blood, 50, 100);
					gc.drawImage(blood2, 650, 230);
					gc.drawImage(hare, x, 410);

					if (x > 155)
						gc.drawImage(bulldozer, xx, 400);
					if (x > 200)
						gc.drawImage(hare2, xxx, 410);
				}
			}.start();

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
