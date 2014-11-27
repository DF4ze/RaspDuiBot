package controleurs;

import modeles.ServeurModele;

public class Controleur {
	
	@SuppressWarnings("unused")
	private Serveur srv;
	private ServeurModele mod;

	public Controleur( int iPort, int iMaxCon, boolean isRunning ) {
		if( iPort == -1 )
			iPort = ServeurModele.DEFAUT_PORT;

		if( iMaxCon == -1 )
			iMaxCon = ServeurModele.DEFAUT_MAXCON;
		
		mod = new ServeurModele(iPort, iMaxCon, isRunning);
		srv = new Serveur(mod);
	}

}
