package controleurs.serveur;

import java.io.BufferedReader;
import java.io.IOException;

import modeles.ServeurModele;

public class Reception implements Runnable {

	private BufferedReader in;
	private String message = null;
	private ServeurModele mod;
	
	public Reception(ServeurModele m, BufferedReader in){
		mod = m;
		this.in = in;
	}
	
	public void run() {
		
		while(true){
	        try {
	        	
			message = in.readLine();
			System.out.println("message : "+message);
			
		    } catch (IOException e) {
				System.out.println("Le client s'est déco");
				//e.printStackTrace();
				try {
					mod.releaseConnexion();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
		}
	}

}