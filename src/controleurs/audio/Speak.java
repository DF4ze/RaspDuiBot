package controleurs.audio;

import java.util.LinkedList;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.shell.ShellPattern;

public class Speak extends Thread {

	private SpeakAction sa;
	
	public Speak( SpeakAction sa ) {
		this.sa = sa;
	}

	@Override
	public void run() {
		if( sa.getTts() == SpeakAction.ttsEspeak ){
			FifoSenderShell.put( ShellPattern.actionToShell(sa) );
		
		}else if( sa.getTts() == SpeakAction.ttsPico ){
			LinkedList<ShellCmd> alShells = new LinkedList<ShellCmd>();
			ShellPattern.actionToShell(sa, alShells);
			
			if( Verbose.isEnable() )
				System.out.println("Speaking, nb cmd : "+alShells.size());
			
			while( alShells.size() != 0 ){
				FifoSenderShell.put( alShells.poll() );
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			
		}

	}

}
