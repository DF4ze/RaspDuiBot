package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderSerial;
import modeles.dao.communication.beansactions.IAction;

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
						IAction action = FifoSenderSerial.get();
						
						// Action a réaliser sur le IAction
						
						// mécanisme de send sur le serial
						
						if( Verbose.isEnable() )
							System.out.println("FIFO-Serial out : "+action.toString());
						
					}
					
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}

}
