package controleurs.fifodispatcher;

import java.util.ArrayList;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beanfifo.FifoSenderSerial;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beansactions.DirectionAction;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.GetStateAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.TourelleAction;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.communication.beansinfos.IInfo;
import modeles.dao.communication.beansinfos.StateInfo;
import modeles.dao.shell.ShellPattern;

public class ListenFifoSocketReceive implements Runnable{
	public void run() {
		if( Verbose.isEnable() )
			System.out.println("Thread SocketReceive Launch");
		while( true ){
			synchronized( FifoReceiverSocket.getInstance() ){
				try {
					FifoReceiverSocket.getInstance().wait();
					
					while( FifoReceiverSocket.getInstance().size() != 0 ){
						IAction ia = FifoReceiverSocket.get();
						
						 if( ia instanceof TourelleAction || ia instanceof DirectionAction ){
							sendToSerial(ia);
							
						}else if( ia instanceof ExtraAction ){
							ExtraAction ea = (ExtraAction)ia;
							extraMgr( ea );
							
						}else if( ia instanceof GetStateAction ){
							GetStateAction gsa = (GetStateAction) ia;
							
							if( gsa.getType() == IAction.typeAll){
								sendToSerial(gsa);
								
								sendToShell(new GetStateAction( IAction.typeAlim, IAction.stateAll ));
								sendToShell(new GetStateAction( IAction.typeWebcam, IAction.stateAll ));
								
								ServeurModele sm = ServeurModele.getInstance();
								StateInfo si = new StateInfo(IInfo.stateAlim, sm.isbPinAlimState());
								FifoSenderSocket.put(si);
								//...
								
							}else{
								if( gsa.getType() == IAction.typeAlim || gsa.getType() == IAction.typeWebcam )
									sendToShell(gsa);
								else
									sendToSerial(gsa);
							}
								
							
						}
					}
				} catch (InterruptedException e) { break; }
			}
		}
		if( Verbose.isEnable() )
			System.out.println("Thread SocketReceive STOPED");
		
	}
	
	
	private void extraMgr( ExtraAction ea ){
		if( ea.getType() == IAction.typeAlim || ea.getType() == IAction.typeWebcam ){
			sendToShell(ea);
		}else
			FifoSenderSerial.put(ea);
	}
	
	private void sendToShell( IAction ea ){
				
		if( ea instanceof GetStateAction ){
			
			ArrayList<ShellCmd> alCmd = new ArrayList<ShellCmd>();
			ShellPattern.actionToShell(ea, alCmd);
			
			for( ShellCmd sh : alCmd ){
				FifoSenderShell.put( sh );
				if( Verbose.isEnable() ){
					String sCmd = "";
					for( String txt : sh.getCommand() ){
						sCmd+= txt+" ";
					}
					System.out.println( "Socket-Shell Send : "+sCmd );
				}
			}
		}else{
			ShellCmd shellcmd = ShellPattern.actionToShell(ea);
			FifoSenderShell.put( shellcmd );
			if( Verbose.isEnable() ){
				String sCmd = "";
				for( String txt : shellcmd.getCommand() ){
					sCmd+= txt+" ";
				}
				System.out.println( "Socket-Shell Send : "+sCmd );
			}
		}

	
	}
	
	private void sendToSerial( IAction ia ){
		FifoSenderSerial.put( ia );
		
		if( Verbose.isEnable() ){
			System.out.println( "Socket-Serial Send : "+ia.toString() );
		}	
	}
	
}


