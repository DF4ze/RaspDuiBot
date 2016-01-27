package controleurs.fifodispatcher;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverShell;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beanshell.ShellResult;
import modeles.dao.communication.beansinfos.StateInfo;
import modeles.dao.shell.ShellPattern;

public class ListenFifoShellReceive implements Runnable{

	public ListenFifoShellReceive()  {
		
	}

	@Override
	public void run() {
		if( Verbose.isEnable() )
			System.out.println("Thread ShellReceive Launch");
		
		while( true ){
			synchronized (FifoReceiverShell.getInstance()) {
				try {
					FifoReceiverShell.getInstance().wait();
					
					while( FifoReceiverShell.getInstance().size() != 0 ){
						ShellResult shellresult = FifoReceiverShell.get();
						
						if( shellresult != null )
						if( shellresult.getName().equals(ShellPattern.stateStreamingName) ){
							;
							// ici on devrait checker si le stream a bien démarré
							// parsing du resultat de la ligne de cmd
							
						}else if( shellresult.getName().equals( ShellPattern.pinAlimOffName) ){
							if( !shellresult.isError() ){
								ServeurModele sm = ServeurModele.getInstance();
								sm.setbPinAlimState(true);
								
								StateInfo si = new StateInfo(StateInfo.stateAlim, true);
								FifoSenderSocket.put(si);
							}else{
								StateInfo si = new StateInfo(StateInfo.stateAlim, false);
								FifoSenderSocket.put(si);								
							}
								
						}else if( shellresult.getName().equals( ShellPattern.pinAlimOnName) ){
							if( !shellresult.isError() ){
								ServeurModele sm = ServeurModele.getInstance();
								sm.setbPinAlimState(false);
								
								StateInfo si = new StateInfo(StateInfo.stateAlim, false);
								FifoSenderSocket.put(si);
							}else{
								StateInfo si = new StateInfo(StateInfo.stateAlim, true);
								FifoSenderSocket.put(si);								
							}
						
						}else if( shellresult.getName().equals( ShellPattern.pinAlimStateName) ){
							if( !shellresult.isError() ){
								
								int istate = -1;
								try{ 
									istate = Integer.valueOf(shellresult.getResult());	
								}catch( Exception e ){}
								
								boolean bstate = false;
								if( istate > 0 )
									bstate = true;
								StateInfo si = new StateInfo(StateInfo.stateAlim, bstate);
								FifoSenderSocket.put(si);
							}else{
								StateInfo si = new StateInfo(StateInfo.stateAlim, false);
								FifoSenderSocket.put(si);								
							}
						
						}else
						{
							//ShellInfo shellInfo = new ShellInfo(shellresult.getName(), shellresult.getCommand(), shellresult.getResult());
							//FifoSenderSocket.put(shellInfo);
						}
						
						// Temporaire
//						ShellInfo shellInfo = new ShellInfo(shellresult.getName(), shellresult.getCommand(), shellresult.getResult());
//						FifoSenderSocket.put(shellInfo);
					

					}
				} catch (InterruptedException e) { break;}
			}
			
			
		}
		if( Verbose.isEnable() )
			System.out.println("Thread ShellReceive STOPED");

	}

}
