package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSerial;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beansinfos.IInfo;

public class ListenFifoSerialReceive implements Runnable{

	public ListenFifoSerialReceive()  {
	}

	@Override
	public void run() {
		while( true ){
			synchronized( FifoReceiverSerial.getInstance() ) {
				try {
					FifoReceiverSerial.getInstance().wait();
					
					while( FifoReceiverSerial.getInstance().size() != 0 ){
						IInfo info = FifoReceiverSerial.get();
						
						FifoSenderSocket.put(info);
						
						if( Verbose.isEnable() )
							System.out.println("Serial-Socket : "+info.toString());
						
					}
					
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}

}
