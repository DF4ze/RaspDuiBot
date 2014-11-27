package controleurs;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

import modeles.ServeurModele;
import modeles.Verbose;



public class Serveur implements Observer{
	private ServeurModele mod;
	private ServerSocket socket;
	private Thread thread;

	public Serveur( ServeurModele m ) {
		this.mod = m;
		
		if( mod.isRunning() )
			demarrerServeur();
	}

	private void demarrerServeur(){
		try {
			
			socket = new ServerSocket( mod.getiPort());
			thread = new Thread(new AccepterClients( mod, socket));
			thread.start();
			System.out.println("INFO : Socket demarree - Port : "+mod.getiPort()+" - NbMaxCon : "+mod.getiMaxConnexion());
		
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.out.println("Erreur lors du lancement du Thread Serveur : \n"+e.getMessage());
				//e.printStackTrace();
			}
		}			
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
