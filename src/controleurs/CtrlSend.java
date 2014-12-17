package controleurs;

import controleurs.fifodispatcher.ListenFifoSerialSend;
import controleurs.fifodispatcher.ListenFifoShellSend;


public class CtrlSend {

	public CtrlSend() {
		
	}
	
	public void start(){
		
		Thread fShell = new Thread( new ListenFifoShellSend());
		fShell.setDaemon(true);
		fShell.start();
		
		Thread fSerial = new Thread(new ListenFifoSerialSend());
		fSerial.setDaemon(true);
		fSerial.start();
	}

	
}
