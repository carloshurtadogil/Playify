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
	public void run() {
		currentlyRunning = true;

		while (currentlyRunning) {
			DatagramPacket dPacket = new DatagramPacket(sentMessage, sentMessage.length);

			try {
				dSocket.receive(dPacket);
				InetAddress address = dPacket.getAddress();
				int port = dPacket.getPort();
				System.out.println("Packet has arrived");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (dPacket.getData() != null) {
				try {
					byte[] packetData = dPacket.getData();
					System.out.println(packetData);
					String packetData64String = new String(packetData);

					JSONParser parser = new JSONParser();
					

					String packetDataString = packetData64String.toString();
					int lastBracket = packetDataString.lastIndexOf("}");
					
					packetDataString = packetDataString.substring(0,lastBracket+1);
					JSONObject packetDataRequest = new JSONObject();

					packetDataRequest = (JSONObject) parser.parse(packetDataString);


					String dispatchedItem = startDispatcher(packetDataRequest);


					sentMessage = dispatchedItem.getBytes();

					dPacket = new DatagramPacket(sentMessage, sentMessage.length, InetAddress.getLocalHost(), 80);

					dSocket.send(dPacket);

				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		}

	}

	// Fires up the dispatcher whenever a request comes in to the server
	// communication model
	public String startDispatcher(JSONObject request) {

		dispatcher = new Dispatcher();
		System.out.println("why");

		LoginDispatcher loginDispatch = new LoginDispatcher();
		System.out.println(request.get("objectName").toString() + " kool");
		

		System.out.println(request.get("remoteMethod").toString() + " kool");
		
		try {
			dispatcher.registerObject(loginDispatch, request.get("objectName").toString());
			return dispatcher.dispatch(request.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
