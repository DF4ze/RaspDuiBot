package controleurs.socket;

import java.io.IOException;
import java.net.ServerSocket;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beansactions.GetStateAction;
import modeles.dao.communication.beansocket.SocketNum;
import controleurs.socket.comClients.ClientServeur;

public class AccepterClients implements Runnable {

	private ServeurModele mod;
	private ServerSocket srvSocket;
	private Thread thread;

	public AccepterClients(ServeurModele m, ServerSocket s) {
		mod = m;
		srvSocket = s;
	}

	public void run() {

		try {
			while (true) {
				mod.acquireConnexion();
				
				SocketNum socket = new SocketNum( srvSocket.accept() ); 
				
				socket.setNumber( mod.addClient(socket.getSocket()) );
				
				ClientServeur cs = new ClientServeur(mod, socket);
				System.out.println("Client "+socket.getNumber()+" connected! IP : "+socket.getSocket().getInetAddress().toString());

				thread = new Thread(cs);
				thread.start();
				
				// on attend brievement que les thread Socket soient bien lancés
				Thread.sleep(100);
				// on demande l'état du materiel
				//FifoSenderSerial.put(new GetStateAction());
				FifoReceiverSocket.put(new GetStateAction());
				
			}

		} catch (IOException e) {
			onExit( e );
		} catch (InterruptedException e) {
			onExit( e );
		}
	}
	
	private void onExit( Exception e ){
		if( Verbose.isEnable() ){
			System.err.println( "Fermeture du thread AccepterClient : "+e.getMessage() );
		}
		
		mod.delAllClient();
		try {
			mod.releaseAllConnexion();
		} catch (InterruptedException e1) {}
		mod.setRunning(false);
	}

}