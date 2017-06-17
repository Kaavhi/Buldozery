package application;

import org.lwjgl.util.vector.Vector2f;

import com.esotericsoftware.kryonet.Connection;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class SpritePlayer {

	public Connection c;
	public Image image;
	public double positionX;
	public double positionY;
	public double velocityX;
	public double velocityY;
	public double width = 60;
	public double height = 60;
	public String direction;
	public float score;
	public int id;
	public boolean isAlive;
	float networkScore;
	Vector2f networkPosition = new Vector2f(0, 0);

	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	public boolean intersects(SpriteEnemy s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

	public boolean intersects(Sprite s) {
		return s.getBoundary().intersects(this.getBoundary());
	}

}