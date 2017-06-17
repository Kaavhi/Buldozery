package application;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import javafx.scene.image.Image;

public class Network extends Listener {

	Client client;
	String ip = "localhost";
	int port = 2345;

	public void connect() {
		client = new Client();
		client.getKryo().register(PacketUpdateX.class);
		client.getKryo().register(PacketUpdateY.class);
		client.getKryo().register(PacketAddPlayer.class);
		client.getKryo().register(PacketRemovePlayer.class);
		client.getKryo().register(PacketScore.class);
		client.getKryo().register(PacketUpdateEnemiesA.class);
		client.getKryo().register(PacketUpdateEnemiesX.class);
		client.getKryo().register(PacketUpdateEnemiesY.class);
		client.getKryo().register(PacketAddEnemies.class);
		client.addListener(this);
		client.start();
		try {
			client.connect(5000, ip, port, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void received(Connection c, Object o) {
		if (o instanceof PacketAddPlayer) {
			PacketAddPlayer packet = (PacketAddPlayer) o;
			Player newPlayer = new Player();
			Game.players.put(packet.id, newPlayer);
		} else if (o instanceof PacketAddEnemies) {
			PacketAddEnemies packet = (PacketAddEnemies) o;
			SpriteEnemy bulldozer = new SpriteEnemy();
			bulldozer.enemyId = packet.id;
			bulldozer.positionX = packet.x;
			bulldozer.positionY = packet.y;
			bulldozer.setImage(new Image("assets\\buldozer.png"));
			Game.bulldozerList.add(bulldozer);
			System.out.println(Game.bulldozerList.size());

		} else if (o instanceof PacketRemovePlayer) {
			PacketRemovePlayer packet = (PacketRemovePlayer) o;
			Game.players.remove(packet.id);

		} else if (o instanceof PacketUpdateX) {
			PacketUpdateX packet = (PacketUpdateX) o;
			Game.players.get(packet.id).x = (float) packet.x;

		} else if (o instanceof PacketUpdateY) {
			PacketUpdateY packet = (PacketUpdateY) o;
			Game.players.get(packet.id).y = (float) packet.y;
		} else if (o instanceof PacketScore) {
			PacketScore packet = (PacketScore) o;
			Game.players.get(packet.id).score = (float) packet.score;
		} else if (o instanceof PacketUpdateEnemiesA) {
			PacketUpdateEnemiesA packet = (PacketUpdateEnemiesA) o;
			Game.bulldozerList.get(packet.id).isAlive = packet.isAlive;
		} else if (o instanceof PacketUpdateEnemiesX) {
			PacketUpdateEnemiesX packet = (PacketUpdateEnemiesX) o;
			Game.bulldozerList.get(packet.id).positionX = packet.x;
		} else if (o instanceof PacketUpdateEnemiesY) {
			PacketUpdateEnemiesY packet = (PacketUpdateEnemiesY) o;
			Game.bulldozerList.get(packet.id).positionY = packet.y;
		}

	}
}
