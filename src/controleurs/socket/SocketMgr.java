package controleurs.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

import modeles.ServeurModele;
import modeles.Verbose;



public class SocketMgr implements Observer{
	private ServeurModele mod;
	private ServerSocket srvSocket;
	private Thread thread;

	public SocketMgr( ServeurModele m ) {
		mod = m;
		mod.delAllClient();
		
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
			System.out.println("INFO : Socket demarre" );
		} catch (IOException e) {
			
			if( Verbose.isEnable() ){
				System.err.println("Erreur lors du lancement du Thread Serveur : \n"+e.getMessage());
			}
			mod.setRunning(false);
		}			
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
