package controleurs.socket.comCommandes;

import java.io.IOException;
import java.net.ServerSocket;

import modeles.dao.communication.beansocket.SocketNum;

public class AccepterCommandes implements Runnable {

	private ServerSocket srvSocket;
	private Thread thread;

	public AccepterCommandes( ServerSocket s) {
		srvSocket = s;
	}

	public void run() {

		try {
			while (true) {
				
				SocketNum socket = new SocketNum( srvSocket.accept() ); 
				
				//socket.setNumber( mod.addClient(socket.getSocket()) );
				
				ClientServeurCommandes cs = new ClientServeurCommandes(socket);

				thread = new Thread(cs);
				thread.start();
				
				// on attend brievement que les thread Socket soient bien lancés
				Thread.sleep(100);
				// on demande l'état du materiel
				//FifoSenderSerial.put(new GetStateAction());
				//FifoReceiverSocket.put(new GetStateAction());
				
			}

		} catch (IOException e) {
			onExit( e );
		} catch (InterruptedException e) {
			onExit( e );
		}
	}
	
	private void onExit( Exception e ){

	}

}