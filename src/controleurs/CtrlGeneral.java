package controleurs;

import controleurs.socket.SocketMgr;
import modeles.ServeurModele;

public class CtrlGeneral {
	
	@SuppressWarnings("unused")
	private SocketMgr srv;
	private ServeurModele mod;

	public CtrlGeneral( int iPort, int iMaxCon, boolean isRunning ) {
		if( iPort == -1 )
			iPort = ServeurModele.DEFAUT_PORT;

		if( iMaxCon == -1 )
			iMaxCon = ServeurModele.DEFAUT_MAXCON;
		
		mod = new ServeurModele(iPort, iMaxCon, isRunning);
		srv = new SocketMgr(mod);
		
		CtrlRecep cr = new CtrlRecep();
		CtrlSend cs = new CtrlSend();
		
		cr.start();
		cs.start();
	}

}
