package ru.game.tetris.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import ru.game.message.Message;

public class Tester {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 9000);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(new Message(Message.Type.JOIN, "56917"));
		oos.flush();
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		Message message = (Message) ois.readObject();
		System.out.println(message.getMessage());
		socket.close();
	}
}

