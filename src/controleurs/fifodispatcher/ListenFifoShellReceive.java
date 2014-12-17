package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverShell;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beanshell.ShellResult;
import modeles.dao.communication.beansinfos.ShellInfo;
import modeles.dao.shell.ShellPattern;

public class ListenFifoShellReceive implements Runnable{

	public ListenFifoShellReceive()  {
		// TODO Auto-generated constructor stub
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
						
						if( shellresult.getName().equals(ShellPattern.stateStreamingName) ){
							;
							// ici on devrait checker si le stream a bien démarré
							// parsing du resultat de la ligne de cmd
							
						}else if( shellresult.getName().equals( ShellPattern.stateAlimName ) ){
							;
							// vérification d'un pin du Rasp.
							// parsing du resultat de la ligne de cmd
						
						}else{
//							ShellInfo shellInfo = new ShellInfo(shellresult.getName(), shellresult.getCommand(), shellresult.getResult());
//							FifoSenderSocket.put(shellInfo);
						}
						
						// Temporaire
						ShellInfo shellInfo = new ShellInfo(shellresult.getName(), shellresult.getCommand(), shellresult.getResult());
						FifoSenderSocket.put(shellInfo);
					

					}
				} catch (InterruptedException e) { break;}
			}
			
			
		}
		if( Verbose.isEnable() )
			System.out.println("Thread ShellReceive STOPED");

	}

}
