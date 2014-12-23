package controleurs.socket.com;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import modeles.ServeurModele;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beansinfos.IInfo;

public class Emission implements Runnable {

	//private ArrayList<ObjectOutputStream> outs;
	private ServeurModele mod;
//	private String message = null;

	// private Scanner sc = null;

	public Emission( ServeurModele mod/*ObjectOutputStream out*/) {
		this.mod = mod;
		
		
	}

	public void run() {

		// sc = new Scanner(System.in);

		while (true) {
			synchronized (FifoSenderSocket.getInstance()) {
				try {
					FifoSenderSocket.getInstance().wait();

					while (FifoSenderSocket.getInstance().size() != 0) {
						HashMap<Integer, Socket> hmClients = mod.getClientsConnected();
						
						for( Entry<Integer, Socket> client : hmClients.entrySet() ){
							Socket sock = (Socket)(client.getValue());
							if( sock.isConnected() ){
								IInfo ii = FifoSenderSocket.get();
								try {
									ObjectOutputStream out = new ObjectOutputStream( sock.getOutputStream());
									out.writeObject( ii );
									out.flush();
								
								} catch (IOException e) {
									System.err.println("Erreur d'ecriture dans le Socket : objet : "+ii);
									break;
								}
							}
						}
					}

						

				} catch (InterruptedException e) {
					break;
				}

			}
		}
	}
}