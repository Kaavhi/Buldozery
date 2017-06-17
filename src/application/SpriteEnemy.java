package application;

import java.io.Serializable;

import org.lwjgl.util.vector.Vector2f;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpriteEnemy implements Serializable {
	private static final long serialVersionUID = 1L;
	private Image image;
	public double positionX;
	public double positionY;
	private double velocityX;
	private double velocityY;
	private double width;
	private double height;
	private String direction;
	public int score;
	public int enemyId;
	public boolean isAlive;
	public boolean networkIsAlive;
	Vector2f networkPosition = new Vector2f(0, 0);

	public SpriteEnemy() {
		isAlive = true;
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}

	public SpriteEnemy(int id) {
		enemyId = id;
		isAlive = true;
		positionX = Math.random() * 700;
		positionY = Math.random() * 500;
		velocityX = 0;
		velocityY = 0;
	}

	public void setAlive(boolean alive) {
		isAlive = alive;
	}

	public boolean getAlive() {
		return isAlive;
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

}