package controleurs.socket.comCommandes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import modeles.Verbose;
import modeles.dao.communication.beansocket.SocketNum;

public class ClientServeurCommandes implements Runnable {

	private SocketNum mySocket = null;
	private ObjectInputStream inObject = null;
//	private ObjectOutputStream outObject = null;
	//private PrintWriter out = null;
	
	
	public ClientServeurCommandes( SocketNum s ){
		mySocket = s;
		
	}
	public void run() {
		
		try {
			Socket s = mySocket.getSocket();
			inObject = new ObjectInputStream(s.getInputStream());
//			outObject = new ObjectOutputStream(s.getOutputStream());
			
			
			Thread t3 = new Thread(new ReceptionCommandes(inObject, mySocket));
			t3.start();
			if( Verbose.isEnable() )
				System.out.println("Socket Commandes Flux de reception ouvert ");
	
		
		
		} catch (IOException e) {

		}
	}
}