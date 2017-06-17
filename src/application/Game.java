package application;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Game extends Application {

	public static ArrayList<SpriteEnemy> bulldozerList = new ArrayList<SpriteEnemy>();
	public static CopyOnWriteArrayList<Sprite> bulletList;
	public static CopyOnWriteArrayList<Sprite> mushroomRedList;
	public static CopyOnWriteArrayList<Sprite> mushroomBlueList;
	public static CopyOnWriteArrayList<Sprite> skarbList;
	static SpritePlayer hare;
	static Image background;
	static GraphicsContext gc;
	static Map<Integer, Player> players = new HashMap<Integer, Player>();
	static Network network = new Network();

	@FXML
	Canvas canwas;
	@FXML
	static TextField score1;
	@FXML
	static TextField score2;

	boolean bullet;
	static boolean goNorth;
	static boolean goSouth;
	static boolean goEast;
	static boolean goWest;
	static boolean cos = false;
	int score, scoreE;
	static long lastNanoTime;
	static long startNanoTime;
	static Socket socket;
	static double enemyX;
	static double enemyY;

	public static void main(String[] args) {
		network.connect();
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		network.connect();

		try {
			theStage.setTitle("Kill bulldozers!");
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\game.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Scene theScene = new Scene(root);
			theStage.setScene(theScene);

			canwas = (Canvas) fxmlLoader.getNamespace().get("canwas");
			gc = canwas.getGraphicsContext2D();

			score1 = (TextField) fxmlLoader.getNamespace().get("score1");
			score2 = (TextField) fxmlLoader.getNamespace().get("score2");

			background = new Image("assets\\grass.png");
			ArrayList<String> input = new ArrayList<String>();
			bulletList = new CopyOnWriteArrayList<Sprite>();
			mushroomRedList = new CopyOnWriteArrayList<Sprite>();
			mushroomBlueList = new CopyOnWriteArrayList<Sprite>();
			skarbList = new CopyOnWriteArrayList<Sprite>();

			for (int i = 0; i < 1; i++) {
				skarbList.add(new Sprite());
				skarbList.get(i).setPosition(Math.random() * 750, Math.random() * 550);
				skarbList.get(i).setImage("assets\\skarb.png");
			}

			for (int i = 0; i < 3; i++) {
				mushroomRedList.add(new Sprite());
				mushroomRedList.get(i).setPosition(Math.random() * 750, Math.random() * 550);
				mushroomRedList.get(i).setImage("assets\\grzybC.png");
			}

			for (int i = 0; i < 3; i++) {
				mushroomBlueList.add(new Sprite());
				mushroomBlueList.get(i).setPosition(Math.random() * 750, Math.random() * 550);
				mushroomBlueList.get(i).setImage("assets\\grzybN.png");
			}

			theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case UP:
						goNorth = true;
						break;
					case DOWN:
						goSouth = true;
						break;
					case LEFT:
						goWest = true;
						break;
					case RIGHT:
						goEast = true;
						break;
					case SPACE:
						bullet = true;
						cos = true;
						break;
					}
				}
			});

			theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					switch (event.getCode()) {
					case UP:
						goNorth = false;
						break;
					case DOWN:
						goSouth = false;
						break;
					case LEFT:
						goWest = false;
						break;
					case RIGHT:
						goEast = false;
						break;
					case SPACE:
						bullet = false;
						cos = false;
						break;
					}
				}
			});

			Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
			gc.setFont(theFont);
			gc.setFill(Color.GREEN);
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);

			hare = new SpritePlayer();
			hare.image = new Image("assets\\zajaczekR.png");
			hare.direction = "RIGHT";
			bulletList = new CopyOnWriteArrayList<Sprite>();
			lastNanoTime = System.nanoTime();
			score = 0;
			startNanoTime = System.nanoTime();

			new AnimationTimer() {
				public void handle(long currentNanoTime) {
					try {
						updateEnemy();
						double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
						lastNanoTime = currentNanoTime;
						hare.velocityX = 0;
						hare.velocityY = 0;
						if (goWest) {
							hare.direction = "LEFT";
							hare.image = new Image("assets\\zajaczekL.png");
							hare.velocityX += -70;
							update();

						}
						if (goEast) {
							hare.direction = "RIGHT";
							hare.image = new Image("assets\\zajaczekR.png");
							hare.velocityX += 70;
							update();

						}
						if (goNorth) {
							hare.direction = "UP";
							hare.image = new Image("assets\\zajaczekU.png");
							hare.velocityY += -70;
							update();

						}
						if (goSouth) {
							hare.direction = "DOWN";
							hare.image = new Image("assets\\zajaczekD.png");
							hare.velocityY += 70;
							update();

						}
						if (cos) {
							Sprite bullet = new Sprite();
							bullet.setImage("assets\\bull.png");
							bullet.setDirection(hare.direction);
							bullet.setPosition(hare.positionX + 30, hare.positionY + 30);
							bulletList.add(bullet);
							cos = false;
						}

						hare.positionX += hare.velocityX * elapsedTime;
						hare.positionY += hare.velocityY * elapsedTime;
						double t = (currentNanoTime - startNanoTime) / 1000000000.0;

						double x = Math.cos(t);
						double y = Math.sin(t);
						for (Sprite bullet : bulletList) {
							if (bullet.getDirection().equals("LEFT"))
								bullet.addVelocity(-3, 0);
							if (bullet.getDirection().equals("RIGHT"))
								bullet.addVelocity(3, 0);
							if (bullet.getDirection().equals("UP"))
								bullet.addVelocity(0, -3);
							if (bullet.getDirection().equals("DOWN"))
								bullet.addVelocity(0, 3);
							bullet.update(elapsedTime);
						}
						if (bulletList.size() > 0) {

							for (Sprite bullet : bulletList) {
								for (int i = 0; i < bulldozerList.size(); i++) {
									if (bullet.intersects(bulldozerList.get(i))) {
										bulldozerList.get(i).setAlive(false);
										bulldozerList.remove(bulldozerList.get(i));
										bulletList.remove(bullet);
										hare.score = hare.score + 100;
										System.out.println(hare.score);
										updateScore();
										updateEnemyAlive();
									}
								}
							}
						}

						for (SpriteEnemy bulldozer : bulldozerList) {

							double playerX = hare.positionX;
							double playerY = hare.positionY;

							double xx = bulldozer.getPositionX();
							double yy = bulldozer.getPositionY();

							double diffX = playerX - xx;
							double diffY = playerY - yy;

							float angle = (float) Math.atan2(diffY, diffX);

							xx += 0.9 * Math.cos(angle);
							yy += 0.9 * Math.sin(angle);

							bulldozer.setPosition(xx, yy);
							bulldozer.update(elapsedTime);
							if (hare.intersects(bulldozer)) {
								hare.positionX = 30;
								hare.positionY = 30;
								hare.score = hare.score - 100;
								System.out.println(hare.score);
								updateScore();

							}
						}

						for (Sprite mushroomBlue : mushroomBlueList) {
							if (hare.intersects(mushroomBlue)) {
								mushroomBlueList.remove(mushroomBlue);
								hare.score = hare.score + 1000;
								hare.velocityX += 20;
								hare.velocityY += 20;
								updateScore();
							}

						}

						for (Sprite mushroomRed : mushroomRedList) {

							if (hare.intersects(mushroomRed)) {
								mushroomRedList.remove(mushroomRed);
								hare.score = hare.score - 500;
								hare.velocityX -= 20;
								hare.velocityY -= 20;
								updateScore();
							}
						}

						for (Sprite skarb : skarbList) {
							if (hare.intersects(skarb)) {
								skarbList.remove(skarb);
								hare.score = hare.score + 5000;
								updateScore();

							}
						}

						gc.clearRect(0, 0, 802, 589);
						gc.drawImage(background, 0, 0);
						gc.drawImage(hare.image, hare.positionX, hare.positionY);

						for (Player sp : players.values()) {
							Image i = new Image("assets\\zajaczekR.png");
							gc.drawImage(i, sp.x, sp.y);
							score2.setText(String.valueOf(sp.score));
						}
						updateEnemy();

						for (SpriteEnemy bulldozer : bulldozerList) {
							bulldozer.render(gc);
						}

						for (Sprite bullet : bulletList) {
							bullet.render(gc);
						}

						for (SpriteEnemy bulldozer : bulldozerList) {
							updateEnemyAlive();
							if (bulldozer.isAlive)
								bulldozer.render(gc);
						}

						for (Sprite mushroomBlue : mushroomBlueList) {
							mushroomBlue.render(gc);
						}

						for (Sprite mushroomRed : mushroomRedList) {
							mushroomRed.render(gc);
						}

						for (Sprite skarb : skarbList) {
							skarb.render(gc);
						}

						score1.setText(String.valueOf(hare.score));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();

			theStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void update() {

		if (hare.networkPosition.x != hare.positionX) {
			PacketUpdateX packet = new PacketUpdateX();
			packet.x = hare.positionX;
			network.client.sendUDP(packet);
			hare.networkPosition.x = (float) hare.positionX;
		}
		if (hare.networkPosition.y != hare.positionY) {
			PacketUpdateY packet = new PacketUpdateY();
			packet.y = hare.positionY;
			network.client.sendUDP(packet);
			hare.networkPosition.y = (float) hare.positionY;
		}
	}

	public static void updateEnemy() {
		for (SpriteEnemy bulldozer : bulldozerList) {

			if (bulldozer.networkPosition.x != bulldozer.positionX) {
				PacketUpdateEnemiesX packet = new PacketUpdateEnemiesX();
				packet.x = bulldozer.positionX;
				network.client.sendUDP(packet);
				bulldozer.networkPosition.x = (float) bulldozer.positionX;
			}
			if (bulldozer.networkPosition.y != bulldozer.positionY) {
				PacketUpdateEnemiesY packet = new PacketUpdateEnemiesY();
				packet.y = bulldozer.positionY;
				network.client.sendUDP(packet);
				bulldozer.networkPosition.y = (float) bulldozer.positionY;
			}
		}
	}

	public static void updateScore() {

		if (hare.networkScore != hare.score) {
			PacketScore packet = new PacketScore();
			packet.score = hare.score;
			network.client.sendUDP(packet);
			hare.networkScore = (float) hare.score;
		}
	}

	public static void updateEnemyAlive() {
		for (SpriteEnemy bulldozer : bulldozerList) {
			if (bulldozer.networkIsAlive != bulldozer.isAlive) {
				PacketUpdateEnemiesA packet = new PacketUpdateEnemiesA();
				packet.isAlive = bulldozer.isAlive;
				network.client.sendUDP(packet);
				bulldozer.networkIsAlive = bulldozer.isAlive;
			}
		}
	}

}
