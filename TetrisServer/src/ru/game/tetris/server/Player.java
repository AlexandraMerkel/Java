package ru.game.tetris.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ru.game.message.Message;

public class Player {
	private Socket clientSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Player(Socket clientSocket) throws IOException{
		this.clientSocket = clientSocket;
		ois = new ObjectInputStream(clientSocket.getInputStream());
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	public boolean hasNewMessages() throws IOException{
		return clientSocket.getInputStream().available() > 0;
	}
	
	public Message getMessage() throws IOException, ClassNotFoundException{
		return (Message) ois.readObject();
	}
	
	public void closeConnection() throws IOException{
		clientSocket.close();
	}
	
	public void sendMessage(Message message) throws IOException{
		oos.writeObject(message);
		oos.flush();
	}
}
