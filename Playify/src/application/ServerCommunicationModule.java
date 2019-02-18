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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerCommunicationModule implements CommunicationModule {

	private DatagramSocket dSocket;
	private Dispatcher dispatcher;

	public ServerCommunicationModule() throws IOException {

		dSocket = new DatagramSocket();
		InetSocketAddress inetSocketAddr = new InetSocketAddress(InetAddress.getLocalHost(), 80);
		dSocket.bind(inetSocketAddr);

	}

	@Override
	public String send(JsonObject request) throws SocketException, UnknownHostException, IOException {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String StarServer(DispatcherInterface dispatcher) {
		// TODO Auto-generated method stub
		return null;
	}

	// Starts the server by firing the server communication module
	public void startServer() throws IOException {

		boolean waitForPacketArrival = false;
		byte[] sentMessage = new byte[1024];
		DatagramPacket dPacket = new DatagramPacket(sentMessage, sentMessage.length);

		while (!waitForPacketArrival) {
			try {
				dSocket.receive(dPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (dPacket.getData() != null) {
				byte[] packetData = dPacket.getData();
				String packetData64String = Base64.getEncoder().encodeToString(packetData);

				JsonParser parser = new JsonParser();
				JsonObject packetDataRequest = parser.parse(packetData64String).getAsJsonObject();
				String dispatchedItem = startDispatcher(packetDataRequest);
				
				
				byte[] bytesToBeDelivered = dispatchedItem.getBytes();
				
				
				DatagramPacket packetToBeReturned = new DatagramPacket(bytesToBeDelivered, bytesToBeDelivered.length);
				
				dSocket.send(packetToBeReturned);
			}
		}

	}

	

	// Fires up the dispatcher whenever a request comes in to the server
	// communication model
	public String startDispatcher(JsonObject request) {

		dispatcher = new Dispatcher();

		SongDispatcher songDispatch = new SongDispatcher();
		dispatcher.registerObject(songDispatch, request.get("objectName").getAsString());
		try {
			return dispatcher.dispatch(request.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		 return null;

	}

}
