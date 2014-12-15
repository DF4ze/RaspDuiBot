package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

import modeles.dao.communication.beanshell.ShellResult;

public class FifoReceiverShell {
	private static LinkedList<ShellResult> fifo = null;

	private FifoReceiverShell() {
		
	}
	public static LinkedList<ShellResult> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<ShellResult>();
		}
		return fifo;
	}
	public static ShellResult get(){
		return getInstance().poll();
	}
	public static void put( ShellResult ia){
		synchronized( getInstance() ){
			getInstance().addLast(ia);
			getInstance().notifyAll();
		}
	}
}
