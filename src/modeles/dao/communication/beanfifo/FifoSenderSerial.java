package modeles.dao.communication.beanfifo;

import java.util.LinkedList;

public class FifoSenderSerial {
	private static LinkedList<String> fifo = null;

	private FifoSenderSerial() {
		
	}
	public static LinkedList<String> getInstance(){
		if( fifo == null ){
			fifo = new LinkedList<String>();
		}
		return fifo;
	}
	public static String get(){
		return getInstance().poll();
	}
	public static void put( String ia){
		getInstance().addLast(ia);
	}
}
