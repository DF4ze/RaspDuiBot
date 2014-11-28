package controleurs.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import modeles.ServeurModele;

public class ClientServeur implements Runnable {

	private Socket socket = null;
	private BufferedReader in = null;
	private ObjectInputStream inObject = null;
	private PrintWriter out = null;
	private ServeurModele mod;
	
	
	public ClientServeur(ServeurModele m, Socket s ){
		mod = m;
		socket = s;
		
	}
	public void run() {
		
		try {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		inObject = new ObjectInputStream(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream());
		
		//while( in.read() != -1);
		
		Thread t3 = new Thread(new Reception(mod, inObject));
		t3.start();
		System.out.println("Flux de reception ouvert ");

		Thread t4 = new Thread(new Emission( out ));
		t4.start();
		System.out.println("Flux d'emission ouvert ");
		
		
		} catch (IOException e) {
			System.err.println("Le client s'est déconnecté ");
			try {
				mod.releaseConnexion();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}