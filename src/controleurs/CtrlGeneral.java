package controleurs;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import modeles.ServeurModele;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.shell.ShellPattern;
import controleurs.serial.SerialMgr;
import controleurs.socket.SocketMgr;

public class CtrlGeneral {
	
	@SuppressWarnings("unused")
	private SocketMgr srv;
	private ServeurModele mod;
	private SerialMgr serial;

	public CtrlGeneral( int iPort, int iMaxCon, boolean isRunning, String sSerialPort, int iSerialSpeed, int iSerialTimeOut, boolean bNoSerial ) {
		if( iPort == -1 ) 
			iPort = ServeurModele.DEFAUT_PORT;

		if( iMaxCon == -1 )
			iMaxCon = ServeurModele.DEFAUT_MAXCON;
		
		mod = new ServeurModele(iPort, iMaxCon, isRunning, sSerialPort, iSerialSpeed, iSerialTimeOut, bNoSerial);
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
		
		// init des pins
		ExtraAction ea = new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.Restart);
		FifoSenderShell.put(ShellPattern.actionToShell(ea));
		
		ea = new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.Off);
		FifoSenderShell.put(ShellPattern.actionToShell(ea));
		
		
		
		//Runtime.getRuntime().addShutdownHook(new ShutDownTreatement());
	}

}
