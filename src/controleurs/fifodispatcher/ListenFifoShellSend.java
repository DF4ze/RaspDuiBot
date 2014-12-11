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
								
								synchronized (FifoReceiverShell.getInstance()) {
									FifoReceiverShell.put(output);
									if( Verbose.isEnable() )
										System.out.println( "Shell Received : "+output );

									FifoReceiverShell.getInstance().notifyAll();
								}
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
