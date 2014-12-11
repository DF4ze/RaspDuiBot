package controleurs.socket.com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beansocket.SocketNum;

public class ClientServeur implements Runnable {

	private SocketNum mySocket = null;
	private ObjectInputStream inObject = null;
	private PrintWriter out = null;
	private ServeurModele mod;
	
	
	public ClientServeur(ServeurModele m, SocketNum s ){
		mod = m;
		mySocket = s;
		
	}
	public void run() {
		
		try {
			Socket s = mySocket.getSocket();
			inObject = new ObjectInputStream(s.getInputStream());
			out = new PrintWriter(s.getOutputStream());
			
			
			Thread t3 = new Thread(new Reception(mod, inObject, mySocket));
			t3.start();
			if( Verbose.isEnable() )
				System.out.println("Socket "+mySocket.getNumber()+" Flux de reception ouvert ");
	
			Thread t4 = new Thread(new Emission( out ));
			t4.start();
			if( Verbose.isEnable() )
				System.out.println("Socket "+mySocket.getNumber()+" Flux d'emission ouvert ");
		
		
		} catch (IOException e) {
			if( mySocket != null ){
				if( Verbose.isEnable() )
					System.err.println("Le client "+mySocket.getNumber()+" s'est déconnecté ");
				
				try {
					mod.releaseConnexion(mySocket.getSocket());
				} catch (InterruptedException e1) {}
				
				try {
					mySocket.getSocket().close();
				} catch (IOException e1) {}
			}

		}
	}
}