package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

import modeles.dao.communication.beanshell.ShellCmd;

public class FifoSenderShell {
	private static LinkedList<ShellCmd> fifo = null;

	private FifoSenderShell() {
		
	}
	public static LinkedList<ShellCmd> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<ShellCmd>();
		}
		return fifo;
	}
	public static ShellCmd get(){
		return getInstance().poll();
	}
	public static void put( ShellCmd ia){
		synchronized( getInstance() ){
			getInstance().addLast(ia);
			getInstance().notifyAll();
		}
	}
}
