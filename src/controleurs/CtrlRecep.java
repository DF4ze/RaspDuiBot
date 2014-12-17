package controleurs;

import controleurs.fifodispatcher.ListenFifoSerialReceive;
import controleurs.fifodispatcher.ListenFifoShellReceive;
import controleurs.fifodispatcher.ListenFifoSocketReceive;


public class CtrlRecep {

	public CtrlRecep() {
		
	}
	
	public void start(){
		Thread fSocket = new Thread( new ListenFifoSocketReceive());
		fSocket.setDaemon(true);
		fSocket.start();
		
		
		Thread fSerial = new Thread( new ListenFifoSerialReceive());
		fSerial.setDaemon(true);
		fSerial.start();
		
		Thread fShell = new Thread( new ListenFifoShellReceive());
		fShell.setDaemon(true);
		fShell.start();
		
		
	}

	
}
