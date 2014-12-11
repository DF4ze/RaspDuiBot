package controleurs;

import controleurs.fifodispatcher.ListenFifoSocket;


public class CtrlRecep {

	public CtrlRecep() {
		
	}
	
	public void start(){
		Thread fSocket = new Thread( new ListenFifoSocket());
		fSocket.setDaemon(true);
		fSocket.start();
		
	}

	
}
