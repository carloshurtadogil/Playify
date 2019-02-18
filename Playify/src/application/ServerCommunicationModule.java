package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerCommunicationModule implements CommunicationModule {

	private Dispatcher dispatcher;
	
	
	public ServerCommunicationModule() throws IOException {
		
		
		dispatcher = new Dispatcher();
		
		
		//Create a socket for receiving requests from the client's communication module
		DatagramSocket dSocket = new DatagramSocket();
		
		boolean waitForPacketArrival = false;
		DatagramPacket dPacket = null;
		
		while(!waitForPacketArrival) {
			dSocket.receive(dPacket);
			
			if(dPacket.getData() !=null) {
				byte[] packetData = dPacket.getData();
				String packetData64String = Base64.getEncoder().encodeToString(packetData);
				
				JsonParser parser = new JsonParser();
				JsonObject packetDataRequest = parser.parse(packetData64String).getAsJsonObject();
				this.startDispatcher(packetDataRequest);
			}
		}
//		
//		if(waitForPacketArrival == true) {
//			this.startDispatcher();
//		}
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
	
	//Fires up the dispatcher whenever a request comes in to the server communication model
	public String startDispatcher(JsonObject request) {
		
		dispatcher = new Dispatcher();
		
		SongDispatcher songDispatch = new SongDispatcher();
		dispatcher.registerObject(songDispatch, request.get("objectName").getAsString());
		try {
			dispatcher.dispatch(request.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return null;
		
	}

}
