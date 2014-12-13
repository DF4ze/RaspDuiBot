package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beanfifo.FifoSenderSerial;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.DirectionAction;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.TourelleAction;
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
							
							extraMgr( ea );
							
						}else if( ia instanceof TourelleAction || ia instanceof DirectionAction ){
							sendToSerial(ia);
						}
					}
				} catch (InterruptedException e) { break; }
			}
		}
		
	}
	
	
	private void extraMgr( ExtraAction ea ){
		if( ea.getType() == IAction.typeAlim || ea.getType() == IAction.typeWebcam ){
			sendToShell(ea);
		}
	}
	
	private void sendToShell( ExtraAction ea ){
		synchronized( FifoSenderShell.getInstance() ){
			String[] cmd = ShellPattern.extraToShell(ea);
			FifoSenderShell.put( cmd );
			
			if( Verbose.isEnable() ){
				String sCmd = "";
				for( String txt : cmd ){
					sCmd+= txt+" ";
				}
				System.out.println( "Shell Send : "+sCmd );
			}

			FifoSenderShell.getInstance().notifyAll();
		}		
	}
	
	private void sendToSerial( IAction ia ){
		synchronized( FifoSenderSerial.getInstance() ){
			FifoSenderSerial.put( ia );
			
			if( Verbose.isEnable() ){
				System.out.println( "Serial Send : "+ia.toString() );
			}
			
			FifoSenderSerial.getInstance().notifyAll();
		}		
	}
	
}


