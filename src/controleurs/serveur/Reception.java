package controleurs.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import modeles.ServeurModele;
import modeles.dao.communication.beansactions.IAction;

public class Reception implements Runnable {

	private BufferedReader in;
	private ObjectInputStream inObject;
	private String message = null;
	private ServeurModele mod;
	
	public Reception(ServeurModele m, ObjectInputStream in){
		mod = m;
		this.inObject = in;
	}
	
	public void run() {
		
		while(true){
	        try {
	        	
	//			message = in.readLine();
	//			System.out.println("message : "+message);
				
		        Object Obj = inObject.readObject();
		        
		        if( Obj instanceof IAction ){
		        	System.out.println("Objet récup : "+(IAction)Obj);
		        }else
		        	System.out.println("ORNI : Objet Recu Non Identifié ! ");
	        
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
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}