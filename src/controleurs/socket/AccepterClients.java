package controleurs.socket;

import java.io.IOException;
import java.net.ServerSocket;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beansocket.SocketNum;
import controleurs.socket.com.ClientServeur;

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
				System.out.println("Client "+socket.getNumber()+" connected!");
				
				ClientServeur cs = new ClientServeur(mod, socket);
				thread = new Thread(cs);
				thread.start();
				
			}

		} catch (IOException e) {
			onExit( e );
		} catch (InterruptedException e) {
			onExit( e );
		}
	}
	
	private void onExit( Exception e ){
		if( Verbose.isEnable() ){
			e.printStackTrace();
		}
		
		mod.delAllClient();
		mod.setRunning(false);
	}

}