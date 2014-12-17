package controleurs;

import controleurs.serial.SerialMgr;

public class ShutDownTreatement extends Thread {

	public ShutDownTreatement() {
		// TODO Auto-generated constructor stub
	}

	public void run(){
		SerialMgr.close();
	}
}
