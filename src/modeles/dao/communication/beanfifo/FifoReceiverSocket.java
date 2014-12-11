package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

import modeles.dao.communication.beansactions.IAction;

public class FifoReceiverSocket {
	private static LinkedList<IAction> fifo = null;

	private FifoReceiverSocket() {
		
	}
	public static LinkedList<IAction> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<IAction>();
		}
		return fifo;
	}
	public static IAction get(){
		return getInstance().poll();
	}
	public static void put( IAction ia){
		getInstance().addLast(ia);
	}
	
}
