package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.shell.ShellPattern;

public class ListenFifoSocket implements Runnable{
	public void run() {
		while( true ){
			synchronized( FifoReceiverSocket.getInstance() ){
				try {
					FifoReceiverSocket.getInstance().wait();
					
					while( FifoReceiverSocket.getInstance().size() != 0 ){
						IAction ia = FifoReceiverSocket.get();
						
						if( ia instanceof ExtraAction ){
							ExtraAction ea = (ExtraAction)ia;
							
							if( ea.getType() == IAction.typeAlim || ea.getType() == IAction.typeWebcam ){
								synchronized (FifoSenderShell.getInstance()) {
									String cmd = ShellPattern.extraToShell(ea);
									FifoSenderShell.put( cmd );
									
									if( Verbose.isEnable() )
										System.out.println( "Shell Send : "+cmd );

									FifoSenderShell.getInstance().notifyAll();
									

								}
							}
						}
					}
				} catch (InterruptedException e) { break; }
			}
		}
		
	}
	
}
