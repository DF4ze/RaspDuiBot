package controleurs;

import controleurs.fifodispatcher.ListenFifoShellSend;


public class CtrlSend {

	public CtrlSend() {
		
	}
	
	public void start(){
		Thread fSocket = new Thread( new ListenFifoShellSend());
		fSocket.setDaemon(true);
		fSocket.start();
		
	}

	
}
