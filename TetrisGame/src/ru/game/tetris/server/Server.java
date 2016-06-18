package ru.game.tetris.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.game.message.Message;
import ru.game.message.Message.Type;

public class Server {
	private Queue<Session> sessions = new LinkedList<>();
	private ExecutorService sessionPool = Executors.newFixedThreadPool(20);
	
	public void start(int port, int sessionPlayerSize) throws IOException{
		System.out.println("Server on " + port + " started. Required players at sessions = " + sessionPlayerSize);
		ServerSocket serverSocket = new ServerSocket(port);
		try {
			while(true){
				System.out.println("waiting for player");
				Socket client = serverSocket.accept();
				System.out.println("player was accepted");
				Player player = new Player(client);
				Message queryMessage = player.getMessage();
				Session session;
				if (queryMessage.getMessageType().equals(Message.Type.NEW_SESSION)){
					session = new Session(sessionPlayerSize);
					sessions.add(session);
				}
				else if (queryMessage.getMessageType().equals(Type.JOIN)) {
					if (!queryMessage.getMessage().isEmpty()){
						Optional<Session> sessionOpt = sessions.stream()
								.filter(s->s.getSessionId().toString().equals(queryMessage.getMessage()))
								.findAny(); 
						if (!sessionOpt.isPresent()){
							player.sendMessage(new Message(Message.Type.ERROR,"session doesn't exist"));
							player.closeConnection();
							continue;
						}
						session = sessionOpt.get();
					} else {
						if (sessions.isEmpty()){
							player.sendMessage(new Message(Message.Type.ERROR,"not any sessions"));
							player.closeConnection();
							continue;
						}
						session = sessions.peek();
					}
					
				}else
				{
					player.sendMessage(new Message(Message.Type.ERROR, "unknown command"));
					player.closeConnection();
					continue;
				}
				session.addPlayer(player);
				System.out.println("added player with query type - " + queryMessage.getMessageType());
				player.sendMessage(new Message(Message.Type.SUCCESS, session.getSessionId().toString()));
				if (session.readyToStart()){
					sessions.remove(session);
					sessionPool.submit(session);
					System.out.println("Ready to start ");
				}
			}
		} catch (Exception e) {
			serverSocket.close();
			e.printStackTrace();
		}
		sessionPool.shutdown();
	}
	
	public static void main(String[] args) throws IOException {
		//new Server().start(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
		new Server().start(5876, 2); // запускаем сессии для 2-х игроков
	}
}
