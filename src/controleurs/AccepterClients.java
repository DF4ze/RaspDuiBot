package controleurs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import modeles.ServeurModele;
import controleurs.serveur.ClientServeur;

class AccepterClients implements Runnable {

	private ServeurModele mod;
	private ServerSocket socketserver;
	private Socket socket;
	private Thread thread;

	public AccepterClients(ServeurModele m, ServerSocket s) {
		mod = m;
		socketserver = s;
	}

	public void run() {

		try {
			while (true) {
				mod.acquireConnexion();
				socket = socketserver.accept(); // Un client se connecte on
												// l'accepte
				System.out.println("Le client est connecté !");
				ClientServeur cs = new ClientServeur(mod, socket);
				thread = new Thread(cs);
				thread.start();
				//socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}