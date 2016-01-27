package controleurs.audio;

import java.util.LinkedList;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.shell.ShellPattern;

public class Speak extends Thread {

	private SpeakAction sa;
	private boolean finish = false;
	
	public Speak( SpeakAction sa ) {
		this.sa = sa;
	}

	@Override
	public void run() {
		synchronized (this) {

//			synchronized (sa) {
	
				if( sa.getTts() == SpeakAction.ttsEspeak ){
					FifoSenderShell.put( ShellPattern.actionToShell(sa) );
				
				}else if( sa.getTts() == SpeakAction.ttsPico ){
					LinkedList<ShellCmd> alShells = new LinkedList<ShellCmd>();
					ShellPattern.actionToShell(sa, alShells);
					
					if( Verbose.isEnable() )
						System.out.println("Speaking, nb cmd : "+alShells.size());
					
					while( alShells.size() != 0 ){
						ShellCmd scmd = alShells.poll();
						synchronized (scmd) {
							FifoSenderShell.put( scmd );
							try {
								scmd.wait(10000);
							} catch (InterruptedException e) {}
						}
					}
				}
				
//				sa.notifyAll();
//			}
			this.notifyAll();
			setFinish(true);
		}
	}

	public boolean isFinished() {
		return finish;
	}

	private void setFinish(boolean finish) {
		this.finish = finish;
	}

}
