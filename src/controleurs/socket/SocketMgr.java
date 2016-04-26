package controleurs.socket;

import java.io.IOException;
import java.net.ServerSocket;

import controleurs.socket.comClients.AccepterClients;
import controleurs.socket.comCommandes.AccepterCommandes;
import modeles.ServeurModele;
import modeles.Verbose;
import modeles.MicrophoneModel;
import controleurs.socket.comMicro.AccepterMicro;
import controleurs.audio.micro.MicroRetreiver;


public class SocketMgr{
	private ServeurModele mod;
	private ServerSocket srvSocket;
	private ServerSocket srvSocketCommandes;
	private ServerSocket srvSocketMicro;
	private Thread thread;

	public SocketMgr( ServeurModele m ) {
		mod = m;
		mod.delAllClient();
		try {
			mod.releaseAllConnexion();
		} catch (InterruptedException e) {}
		
		if( mod.isRunning() )
			demarrerServeur();
	}

	private void demarrerServeur(){
		// SOcket pour la communication client/serveur
		try {
			srvSocket = new ServerSocket( mod.getiPort());
			thread = new Thread(new AccepterClients( mod, srvSocket));
			thread.start();
			if( Verbose.isEnable() ){
				System.out.println("INFO : Socket demarre, port "+mod.getiPort() );
			}
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur : \n"+e.getMessage());
			}
			mod.setRunning(false);
		}	
		

		// Socket pour la reception de commandes externes
		try {
			srvSocketCommandes = new ServerSocket( 2010 );
			thread = new Thread(new AccepterCommandes( srvSocketCommandes ));
			thread.start();

			if( Verbose.isEnable() ){
				System.out.println("INFO : Socket Commandes demarre port 2010" );
			}
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur Commandes : \n"+e.getMessage());
			}
		}	

		// Socket pour le stream audio
		try {
			srvSocketMicro = new ServerSocket( MicrophoneModel.PORT );
			AccepterMicro am = new AccepterMicro( srvSocketMicro );
			am.start();

			MicroRetreiver mr = new MicroRetreiver();
			mr.start();
			
			if( Verbose.isEnable() ){
				System.out.println("INFO : Socket Micro demarre port "+MicrophoneModel.PORT );
			}
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur Micro : \n"+e.getMessage());
			}
		}	

	}
	
}
