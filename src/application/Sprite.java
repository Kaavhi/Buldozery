package application;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite implements Serializable {

	private static final long serialVersionUID = 1L;
	private Image image;
	private double positionX;
	private double positionY;
	private double velocityX;
	private double velocityY;
	private double width;
	private double height;
	private String direction;
	public int score;
	public int playerId;
	public boolean isAlive;
	Vector2f networkPosition = new Vector2f(0, 0);

	public Sprite() {
		playerId = -1;
		isAlive = false;
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
		score = 0;
	}

	public Sprite(int id) {
		playerId = id;
		isAlive = false;
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
		score = 0;
		if (id == 0) {
			positionX = 30;
			positionY = 30;
			velocityX = 0;
			velocityY = 0;
		}
		if (id == 1) {
			positionX = 700;
			positionY = 500;
			velocityX = 0;
			velocityY = 0;
		}
	}

	public void setImage(Image i) {
		image = i;
		width = i.getWidth();
		height = i.getHeight();
	}

	public void setImage(String filename) {
		Image i = new Image(filename);
		setImage(i);
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}

	public void setDirection(String d) {
		direction = d;
	}

	public String getDirection() {
		return direction;
	}

	public void setScore(int s) {
		score = s;
	}

	public int getScore() {
		return score;
	}

	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}

	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
	}

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

	public boolean intersects(SpriteEnemy s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

}