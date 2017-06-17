package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class Serwer extends Listener {
	static Server server;
	static final int port = 2345;
	static Map<Integer, SpritePlayer> players = new HashMap<Integer, SpritePlayer>();
	static List<SpriteEnemy> bulldozerList = new ArrayList<SpriteEnemy>();

	public static void main(String[] args) throws IOException {
		server = new Server();
		server.getKryo().register(PacketUpdateX.class);
		server.getKryo().register(PacketUpdateY.class);
		server.getKryo().register(PacketAddPlayer.class);
		server.getKryo().register(PacketRemovePlayer.class);
		server.getKryo().register(PacketScore.class);
		server.getKryo().register(PacketUpdateEnemiesA.class);
		server.getKryo().register(PacketUpdateEnemiesX.class);
		server.getKryo().register(PacketUpdateEnemiesY.class);
		server.getKryo().register(PacketAddEnemies.class);
		server.bind(port, port);
		for (int i = 0; i < 10; i++) {
			bulldozerList.add(new SpriteEnemy(i));
		}
		server.start();
		server.addListener(new Serwer());
		System.out.println("The server is ready");
	}

	public void connected(Connection c) {
		SpritePlayer player = new SpritePlayer();
		player.positionX = 30;
		player.positionY = 30;
		player.c = c;

		PacketAddPlayer packet = new PacketAddPlayer();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);

		for (SpritePlayer p : players.values()) {
			PacketAddPlayer packet2 = new PacketAddPlayer();
			packet2.id = p.c.getID();
			c.sendTCP(packet2);
		}
		for (SpriteEnemy bulldozer : bulldozerList) {
			PacketAddEnemies packetE = new PacketAddEnemies();
			packetE.id = bulldozer.enemyId;
			packetE.x = bulldozer.positionX;
			packetE.y = bulldozer.positionY;
			c.sendTCP(packetE);
		}

		players.put(c.getID(), player);
		System.out.println("Connection received.");
	}

	public void received(Connection c, Object o) {
		if (o instanceof PacketUpdateX) {
			PacketUpdateX packet = (PacketUpdateX) o;
			players.get(c.getID()).positionX = packet.x;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		} else if (o instanceof PacketUpdateY) {
			PacketUpdateY packet = (PacketUpdateY) o;
			players.get(c.getID()).positionY = packet.y;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		} else if (o instanceof PacketScore) {
			PacketScore packet = (PacketScore) o;
			players.get(c.getID()).score = packet.score;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		} else if (o instanceof PacketUpdateEnemiesA) {
			PacketUpdateEnemiesA packet = (PacketUpdateEnemiesA) o;
			bulldozerList.get(packet.id).isAlive = packet.isAlive;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		} else if (o instanceof PacketUpdateEnemiesX) {
			PacketUpdateEnemiesX packet = (PacketUpdateEnemiesX) o;
			bulldozerList.get(packet.id).positionX = packet.x;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		} else if (o instanceof PacketUpdateEnemiesY) {
			PacketUpdateEnemiesY packet = (PacketUpdateEnemiesY) o;
			bulldozerList.get(packet.id).positionY = packet.y;
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);
		}
	}

	public void disconnected(Connection c) {
		players.remove(c.getID());
		PacketRemovePlayer packet = new PacketRemovePlayer();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);
		System.out.println("Connection dropped.");
	}
}
