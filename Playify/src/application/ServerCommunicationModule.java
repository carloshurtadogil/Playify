package application;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerCommunicationModule extends Thread {

	private DatagramSocket dSocket;
	private DatagramPacket packetToBeReceived;
	private DatagramPacket packetToBeSent;
	private boolean currentlyRunning;
	private byte[] messageBuffer =null;
	private Dispatcher dispatcher;
	
	
	public static void main(String []args) throws IOException, ParseException {
		File f = new File("SOEKGLO12A8C1363DE.mp3");
		System.out.println(f.length());
		ServerCommunicationModule test = new ServerCommunicationModule();
		test.StartServer();
	}

	public ServerCommunicationModule() throws IOException {

		System.out.println(InetAddress.getLocalHost());
		dSocket = new DatagramSocket(5000);

	}

	// Starts the server by firing the server communication module
	public void StartServer() throws IOException, ParseException {
		currentlyRunning = true;

		while (currentlyRunning) {
			messageBuffer = new byte[65535];
			packetToBeReceived = new DatagramPacket(messageBuffer, messageBuffer.length);
			dSocket.receive(packetToBeReceived);
			
			
			String receivedMessage = new String(packetToBeReceived.getData(), 0, packetToBeReceived.getLength());

			JsonObject requestAsJsonObject = new JsonParser().parse(receivedMessage).getAsJsonObject();
			messageBuffer = this.startDispatcher(requestAsJsonObject);
			
			InetAddress clientAddress = packetToBeReceived.getAddress();
			int clientPort = packetToBeReceived.getPort();
			
			String msgInBytes = new String(messageBuffer);
			System.out.println("RETRIEVED MESSAGE " + msgInBytes);
			
			packetToBeSent =  new DatagramPacket(messageBuffer, messageBuffer.length, clientAddress, clientPort);

			dSocket.send(packetToBeSent);
		}

	}

	// Fires up the dispatcher whenever a request comes in to the server
	// communication model
	public byte[] startDispatcher(JsonObject request) throws ParseException {

		dispatcher = new Dispatcher();
		return (dispatcher.dispatch(request.toString())).getBytes();
	}
}
