package controleurs;

import modeles.ServeurModele;
import controleurs.fifodispatcher.ListenFifoSerialReceive;
import controleurs.fifodispatcher.ListenFifoShellReceive;
import controleurs.fifodispatcher.ListenFifoSocketReceive;


public class CtrlRecep {
	
	private ServeurModele mod;

	public CtrlRecep( ServeurModele mod ) {
		this.mod = mod;
	}
	
	public void start(){
		Thread fSocket = new Thread( new ListenFifoSocketReceive());
		fSocket.setDaemon(true);
		fSocket.start();
		
		if( !mod.isbNoSerial() ){
			Thread fSerial = new Thread( new ListenFifoSerialReceive());
			fSerial.setDaemon(true);
			fSerial.start();
		}
		Thread fShell = new Thread( new ListenFifoShellReceive());
		fShell.setDaemon(true);
		fShell.start();
		
		
	}

	
}
