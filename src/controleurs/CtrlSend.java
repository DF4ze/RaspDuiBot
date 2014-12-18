package controleurs;

import modeles.ServeurModele;
import controleurs.fifodispatcher.ListenFifoSerialSend;
import controleurs.fifodispatcher.ListenFifoShellSend;


public class CtrlSend {

	private ServeurModele mod;

	public CtrlSend( ServeurModele mod ) {
		this.mod = mod;
	}

	
	public void start(){
		
		Thread fShell = new Thread( new ListenFifoShellSend());
		fShell.setDaemon(true);
		fShell.start();
		
		if( !mod.isbNoSerial() ){
			Thread fSerial = new Thread(new ListenFifoSerialSend());
			fSerial.setDaemon(true);
			fSerial.start();
		}
	}

	
}
