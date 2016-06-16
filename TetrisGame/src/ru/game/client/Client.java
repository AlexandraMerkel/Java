package ru.game.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;


import ru.game.message.Message;
import ru.game.message.Message.Type;

public class Client {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket clientSocket;
	
	public Client(String serverAddress, int port) throws UnknownHostException, IOException{
		clientSocket = new Socket(serverAddress, port);
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		ois = new ObjectInputStream(clientSocket.getInputStream());
	}
	
	public Message createSession() throws IOException, ClassNotFoundException{
		Message message = new Message(Message.Type.NEW_SESSION, "");
		sendMessage(message);
		return readMessage();
	}
	
	public Message joinSession(int sessionNumber) throws ClassNotFoundException, IOException{
		Message message = new Message(Message.Type.JOIN, String.valueOf(sessionNumber));
		sendMessage(message);
		return readMessage();
	}
	
	public void closeSession() throws IOException{
		ois.close();
		oos.flush();
		oos.close();
		clientSocket.close();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getAvailableSessions() throws ClassNotFoundException, IOException{
		Message message = new Message(Type.GET_AVAILABLE_SESSIONS, "");
		sendMessage(message);
		return (Map<Integer, String>)readMessage().getAdditionalData();
	}
	
	public void sendMessage(Message message) throws IOException{
		oos.writeObject(message);
	}
	
	public Message readMessage() throws ClassNotFoundException, IOException{
		return (Message) ois.readObject();
	}
	
	public boolean hasMessage() throws IOException{
		return clientSocket.getInputStream().available() > 0;
	}
}
