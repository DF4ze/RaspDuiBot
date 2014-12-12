package controleurs.fifodispatcher;

import modeles.dao.communication.beanfifo.FifoSenderSerial;

public class ListenFifoSerialSend implements Runnable{

	public ListenFifoSerialSend()  {
	}

	@Override
	public void run() {
		while( true ){
			synchronized( FifoSenderSerial.getInstance() ) {
				try {
					FifoSenderSerial.getInstance().wait();
					
					while( FifoSenderSerial.getInstance().size() != 0 ){
						
						
						
					}
					
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}

}
