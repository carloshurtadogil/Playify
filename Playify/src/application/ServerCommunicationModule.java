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

	private DatagramSocket dSocket;
	private boolean currentlyRunning;
	private byte[] sentMessage = new byte[1024];
	private Dispatcher dispatcher;

	public ServerCommunicationModule() throws IOException {

		dSocket = new DatagramSocket(80);

	}

	// Starts the server by firing the server communication module
	public void StartServer() throws IOException {
		currentlyRunning = true;

		while (currentlyRunning) {
			DatagramPacket incomingRequest = new DatagramPacket(sentMessage, sentMessage.length);
			
			
			
			
			dSocket.receive(incomingRequest);
			
			String requestMessage = new String(sentMessage, 0, incomingRequest.getLength());
			
			System.out.println(requestMessage);
			
			InetAddress clientAddress = incomingRequest.getAddress();
			int clientPort = incomingRequest.getPort();
			

			DatagramPacket response =  new DatagramPacket(sentMessage, sentMessage.length, clientAddress, 80);

			dSocket.send(response);
//			
//			if (dPacket.getData() != null) {
//				try {
//					byte[] packetData = dPacket.getData();
//					System.out.println(packetData);
//					String packetData64String = new String(packetData);
//
//					JSONParser parser = new JSONParser();
//					
//
//					String packetDataString = packetData64String.toString();
//					int lastBracket = packetDataString.lastIndexOf("}");
//					
//					packetDataString = packetDataString.substring(0,lastBracket+1);
//					JSONObject packetDataRequest = new JSONObject();
//
//					packetDataRequest = (JSONObject) parser.parse(packetDataString);
//
//
//					String dispatchedItem = startDispatcher(packetDataRequest);
//
//
//					sentMessage = dispatchedItem.getBytes();
//
//
//				}
//				catch(Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
		}

	}

	// Fires up the dispatcher whenever a request comes in to the server
	// communication model
	public byte[] startDispatcher(JSONObject request) throws ParseException {

		dispatcher = new Dispatcher();
		
		LoginDispatcher loginDispatcher = new LoginDispatcher();
		SongDispatcher songDispatcher = new SongDispatcher();
		RegisterDispatcher registerDispatcher = new RegisterDispatcher();

		RemoteRefInterface remoteRefInterface = new RemoteRef();
		
		
		String remoteReferenceDetails = remoteRefInterface.getRemoteReference(request.get("remoteMethod").toString());
		
		if(remoteReferenceDetails !=null) {
			JSONParser parser = new JSONParser();
			JSONObject remoteRefJson = (JSONObject)parser.parse(remoteReferenceDetails);
			
			String nameOfDispatcher = remoteRefJson.get("object").toString();
			switch(nameOfDispatcher) {
				case "LoginDispatcher":
					loginDispatcher = new LoginDispatcher();
					break;
				case "SongDispatcher":
					songDispatcher = new SongDispatcher();
					break;
				case "RegisterDispatcher":
					registerDispatcher = new RegisterDispatcher();
					break;
			}
			
			try {
				dispatcher.registerObject(loginDispatcher, request.get("objectName").toString());
				return (dispatcher.dispatch(request.toString())).getBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
		
		
		return null;

	}

}
