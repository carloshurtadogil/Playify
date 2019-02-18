package application;

import java.util.UUID;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ClientCommunicationModule implements CommunicationModule {
	
	
	public ClientCommunicationModule() throws SocketException {
		
		
		
	}
	
	//Adds a request ID to a client request before sending it to the server's communication module
	public JsonObject addRequestIdToRequest(JsonObject request) {
		try {
			//generate a random requestID using the UUID library
			request.addProperty("requestId", UUID.randomUUID().toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return request;
		
	}

	//Creates a socket then transfers a packet over using that newly created socket
	@Override
	public String send(JsonObject request) throws IOException {
		
		
		byte[] messageInBytes = request.toString().getBytes();
		
		//Create a new socket
		DatagramSocket ds = new DatagramSocket();
				
		InetAddress iAddress = InetAddress.getLocalHost();
				
		DatagramPacket dPacket = new DatagramPacket(messageInBytes, messageInBytes.length, 0, null);
		ds.send(dPacket);
		
		return null;
	}

	@Override
	public String StarServer(DispatcherInterface dispatcher) {
		// TODO Auto-generated method stub
		return null;
	}
}
