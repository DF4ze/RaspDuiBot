package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

import modeles.dao.communication.beansactions.IAction;

public class FifoSenderSerial {
	private static LinkedList<IAction> fifo = null;

	private FifoSenderSerial() {
		
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
		synchronized( getInstance() ){
			getInstance().addLast(ia);
			getInstance().notifyAll();
		}
	}
}
