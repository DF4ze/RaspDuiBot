package controleurs.socket;

import java.io.IOException;
import java.net.ServerSocket;

import modeles.ServeurModele;
import modeles.Verbose;



public class SocketMgr{
	private ServeurModele mod;
	private ServerSocket srvSocket;
	private ServerSocket srvSocketCommandes;
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
		try {
			
			srvSocket = new ServerSocket( mod.getiPort());
			thread = new Thread(new AccepterClients( mod, srvSocket));
			thread.start();

			//InetAddress thisIp = InetAddress.getLocalHost();

			//System.out.println("INFO : Socket demarree - IP : "+thisIp.getHostAddress()+" Port : "+mod.getiPort()+" - NbMaxCon : "+mod.getiMaxConnexion());
			System.out.println("INFO : Socket demarre, port "+mod.getiPort() );
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur : \n"+e.getMessage());
			}
			mod.setRunning(false);
		}	
		

		try {
			
			srvSocketCommandes = new ServerSocket( 2010 );
			thread = new Thread(new AccepterCommandes( srvSocketCommandes ));
			thread.start();

			//InetAddress thisIp = InetAddress.getLocalHost();

			//System.out.println("INFO : Socket demarree - IP : "+thisIp.getHostAddress()+" Port : "+mod.getiPort()+" - NbMaxCon : "+mod.getiMaxConnexion());
			System.out.println("INFO : Socket Commandes demarre port 2010" );
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur Commandes : \n"+e.getMessage());
			}
			mod.setRunning(false);
		}	

	}
	
}
