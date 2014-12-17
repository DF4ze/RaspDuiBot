package controleurs.fifodispatcher;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverShell;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.communication.beanshell.ShellResult;
import controleurs.shell.ExecuteShellComand;

public class ListenFifoShellSend implements Runnable{

	public ListenFifoShellSend()  {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if( Verbose.isEnable() )
			System.out.println("Thread ShellSend Launch");
		while( true ){
			synchronized (FifoSenderShell.getInstance()) {
				try {
					FifoSenderShell.getInstance().wait();
					
					while( FifoSenderShell.getInstance().size() != 0 ){
						final ShellCmd shellcmd = FifoSenderShell.get();
				 
						final ExecuteShellComand shell = new ExecuteShellComand(shellcmd.getCommand());
						
						Thread th = new Thread(){
							public void run(){
								String output = shell.executeCommand();
								ShellResult shellresult = new ShellResult(shellcmd.getName(), shellcmd.getCommand(), output);
								
								if( shell.isCommandEnded() )
									System.out.println("Fin de la commande");
								else
									System.out.println("Commande n'est pas finie");
								
								if( shell.isError() )
									System.out.println("Commande en erreur");
								else
									System.out.println("Commande sans erreur");
								
								FifoReceiverShell.put(shellresult);
								if( Verbose.isEnable() )
									System.out.println( "Shell Received suite a "+shellresult.getName()+" : "+shellresult.getResult() );

							}
						};
						
						th.setDaemon(true);
						th.start();
					}
				} catch (InterruptedException e) { break;}
			}
			
			
		}
		if( Verbose.isEnable() )
			System.out.println("Thread ShellSend STOPED");
		
	}

}
