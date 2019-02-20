package application;

import java.util.UUID;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ClientCommunicationModule implements CommunicationModule {
	
	private byte [] messageBuffer;
	
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
	//then proceeds to wait till the client sends back a response
	@Override
	public String send(JsonObject request) throws IOException {
		
		
		messageBuffer = request.toString().getBytes();
		
		//Create a new socket
		DatagramSocket ds = new DatagramSocket();
		InetAddress iAddress = InetAddress.getLocalHost();
				
		//Get ready to send the packet from the client communication module to the server communication module
		DatagramPacket thePacket = new DatagramPacket(messageBuffer, messageBuffer.length, iAddress, 80);
		ds.send(thePacket);
		System.out.println("Packet has been sent");
		
		
		//Get ready to receive the response from the server communication module
		thePacket = new DatagramPacket(messageBuffer, messageBuffer.length); 
		ds.receive(thePacket);
		
		System.out.println("Reply has been received");
		String receivedMessage = new String(thePacket.getData(), 0, thePacket.getLength());
		return receivedMessage;
	}

	@Override
	public String StarServer(DispatcherInterface dispatcher) {
		// TODO Auto-generated method stub
		return null;
	}
}
