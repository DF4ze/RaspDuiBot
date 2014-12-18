package controleurs;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

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
		
		if( !mod.isbNoSerial() ){
			serial = new SerialMgr( mod );
			try {
				serial.connect();
			} catch (PortInUseException e) {
				System.err.println("Port serie deja utilise");
				//e.printStackTrace();
			} catch (UnsupportedCommOperationException e) {
				System.err.println("Port serie : Operation non supportee");
				
			} catch (IOException e) {
				System.err.println("Port serie : Erreur sur le stream");
				
			} catch (NoSuchPortException e) {
				System.err.println("Port serie n'existe pas");
				
			}
		}
		
		CtrlRecep cr = new CtrlRecep( mod );
		CtrlSend cs = new CtrlSend( mod );
		
		cr.start();
		cs.start();
		
		//Runtime.getRuntime().addShutdownHook(new ShutDownTreatement());
	}

}
