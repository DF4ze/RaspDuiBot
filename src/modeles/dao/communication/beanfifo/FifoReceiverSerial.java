package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

import modeles.dao.communication.beansinfos.IInfo;

public class FifoReceiverSerial {
	private static LinkedList<IInfo> fifo = null;

	private FifoReceiverSerial() {
		
	}
	public static LinkedList<IInfo> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<IInfo>();
		}
		return fifo;
	}
	public static IInfo get(){
		return getInstance().poll();
	}
	public static void put( IInfo ia){
		synchronized( getInstance() ){
			getInstance().addLast(ia);
			getInstance().notifyAll();
		}
	}
}
