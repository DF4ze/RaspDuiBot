package controleurs;

import controleurs.serial.SerialMgr;
import controleurs.socket.SocketMgr;
import modeles.ServeurModele;

public class CtrlGeneral {
	
	@SuppressWarnings("unused")
	private SocketMgr srv;
	private ServeurModele mod;
	private SerialMgr serial;

	public CtrlGeneral( int iPort, int iMaxCon, boolean isRunning, String sSerialPort, int iSerialSpeed, int iSerialTimeOut ) {
		if( iPort == -1 )
			iPort = ServeurModele.DEFAUT_PORT;

		if( iMaxCon == -1 )
			iMaxCon = ServeurModele.DEFAUT_MAXCON;
		
		mod = new ServeurModele(iPort, iMaxCon, isRunning, sSerialPort, iSerialSpeed, iSerialTimeOut);
		new SocketMgr(mod);
		serial = new SerialMgr( mod );
		try {
			serial.connect();
		} catch (Exception e) {
			System.err.println("Erreur lors de l'ouverture du port serie\n"+e.getMessage());
		}
		
		CtrlRecep cr = new CtrlRecep();
		CtrlSend cs = new CtrlSend();
		
		cr.start();
		cs.start();
	}

}
