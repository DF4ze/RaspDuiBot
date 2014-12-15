package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beanfifo.FifoSenderSerial;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.DirectionAction;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.GetStateAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.TourelleAction;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.shell.ShellPattern;

public class ListenFifoSocket implements Runnable{
	public void run() {
		while( true ){
			synchronized( FifoReceiverSocket.getInstance() ){
				try {
					FifoReceiverSocket.getInstance().wait();
					
					while( FifoReceiverSocket.getInstance().size() != 0 ){
						IAction ia = FifoReceiverSocket.get();
						
						if( ia instanceof GetStateAction ){
							GetStateAction gsa = (GetStateAction) ia;
							
							if( gsa.getType() == IAction.typeAll){
								sendToSerial(gsa);
								
								sendToShell(new GetStateAction( IAction.typeAlim, IAction.stateAll ));
								sendToShell(new GetStateAction( IAction.typeWebcam, IAction.stateAll ));
								//...
								
							}else{
								if( gsa.getType() == IAction.typeAlim || gsa.getType() == IAction.typeWebcam )
									sendToShell(gsa);
								else
									sendToSerial(gsa);
							}
								
							
						}else if( ia instanceof ExtraAction ){
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
	
	private void sendToShell( IAction ea ){
				
		ShellCmd shellcmd = ShellPattern.actionToShell(ea);
		FifoSenderShell.put( shellcmd );
		
		if( Verbose.isEnable() ){
			String sCmd = "";
			for( String txt : shellcmd.getCommand() ){
				sCmd+= txt+" ";
			}
			System.out.println( "Shell Send : "+sCmd );
		}
	
	}
	
	private void sendToSerial( IAction ia ){
		synchronized( FifoSenderSerial.getInstance() ){
			FifoSenderSerial.put( ia );
			
			if( Verbose.isEnable() ){
				System.out.println( "Socket-Serial Send : "+ia.toString() );
			}
			
			FifoSenderSerial.getInstance().notifyAll();
		}		
	}
	
}


