package controleurs.fifodispatcher;

import controleurs.shell.ExecuteShellComand;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverShell;
import modeles.dao.communication.beanfifo.FifoSenderShell;

public class ListenFifoShellSend implements Runnable{

	public ListenFifoShellSend()  {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while( true ){
			synchronized (FifoSenderShell.getInstance()) {
				try {
					FifoSenderShell.getInstance().wait();
					
					while( FifoSenderShell.getInstance().size() != 0 ){
						String[] cmd = FifoSenderShell.get();
				 
						final ExecuteShellComand shell = new ExecuteShellComand(cmd);
						Thread th = new Thread(){
							public void run(){
								String output = shell.executeCommand();
								
								if( shell.isCommandEnded() )
									System.out.println("Fin de la commande");
								else
									System.out.println("Commande n'est pas finie");
								if( shell.isError() )
									System.out.println("Commande en erreur");
								else
									System.out.println("Commande sans erreur");
								
								//synchronized (FifoReceiverShell.getInstance()) {
									FifoReceiverShell.put(output);
									if( Verbose.isEnable() )
										System.out.println( "Shell Received : "+output );

								//	FifoReceiverShell.getInstance().notifyAll();
								//}
							}
						};
						
						th.setDaemon(true);
						th.start();
					}
				} catch (InterruptedException e) { break;}
			}
			
			
		}
		
	}

}
