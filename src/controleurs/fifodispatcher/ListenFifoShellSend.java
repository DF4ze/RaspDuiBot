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
						String cmd = FifoSenderShell.get();
				 
						String output = ExecuteShellComand.executeCommand(cmd);
				 
						synchronized (FifoReceiverShell.getInstance()) {
							FifoReceiverShell.put(output);
							if( Verbose.isEnable() )
								System.out.println( "Shell Received : "+output );

							
							FifoSenderShell.getInstance().notifyAll();
							
						}
						
				 
					}
				} catch (InterruptedException e) { break;}
			}
			
			
		}
		
	}

}
