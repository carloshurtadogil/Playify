package application;

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

	
	
	public static void main(String []args) throws IOException, ParseException {
		ServerCommunicationModule test = new ServerCommunicationModule();
		test.StartServer();
		
		
	}
	
	private DatagramSocket dSocket;
	private boolean currentlyRunning;
	private byte[] sentMessage = new byte[6000];
	private Dispatcher dispatcher;

	public ServerCommunicationModule() throws IOException {

		System.out.println(InetAddress.getLocalHost());
		dSocket = new DatagramSocket(5000);

	}

	// Starts the server by firing the server communication module
	public void StartServer() throws IOException, ParseException {
		currentlyRunning = true;

		while (currentlyRunning) {
			DatagramPacket incomingRequest = new DatagramPacket(sentMessage, sentMessage.length);
			
			dSocket.receive(incomingRequest);
			
			String requestMessage = new String(sentMessage, 0, incomingRequest.getLength());
			
			System.out.println("OH YEAH : " + requestMessage);
			
			JsonObject requestAsJsonObject = new JsonParser().parse(requestMessage).getAsJsonObject();
			sentMessage = this.startDispatcher(requestAsJsonObject);
			
			String msgInBytes = new String(sentMessage);
			System.out.println("RETRIEVED MESSAGE " + msgInBytes);
			
			InetAddress clientAddress = incomingRequest.getAddress();
			int clientPort = incomingRequest.getPort();
			

			DatagramPacket response =  new DatagramPacket(msgInBytes.getBytes(), msgInBytes.getBytes().length, clientAddress, clientPort);

			dSocket.send(response);
		}

	}

	// Fires up the dispatcher whenever a request comes in to the server
	// communication model
	public byte[] startDispatcher(JsonObject request) throws ParseException {

		dispatcher = new Dispatcher();
		return (dispatcher.dispatch(request.toString())).getBytes();
	}
}
