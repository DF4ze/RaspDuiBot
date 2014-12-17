package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderSerial;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.serial.SerialPattern;
import controleurs.serial.com.SerialTransmit;

public class ListenFifoSerialSend implements Runnable{

	public ListenFifoSerialSend()  {
	}

	@Override
	public void run() {
		if( Verbose.isEnable() )
			System.out.println("Thread SerialSend Launch");
		while( true ){
			synchronized( FifoSenderSerial.getInstance() ) {
				try {
					FifoSenderSerial.getInstance().wait();
					
					while( FifoSenderSerial.getInstance().size() != 0 ){
						IAction action = FifoSenderSerial.get();
												
						SerialTransmit.send(SerialPattern.actionToSerial(action));
						
						if( Verbose.isEnable() )
							System.out.println("FIFO-Serial out : "+action.toString());
						
					}
					
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		if( Verbose.isEnable() )
			System.out.println("Thread SerialSend STOPED");

	}

}
