package modeles.dao.communication.beanfifo;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

public class FifoMicro {
	private static LinkedList<ByteArrayOutputStream> list;

	private FifoMicro() {
	}
	
	public static LinkedList<ByteArrayOutputStream> getList(){
		if( list == null )
			list = new LinkedList<ByteArrayOutputStream>();
		
		return list;
	}
	
	public static void add( ByteArrayOutputStream baos ){
		LinkedList<ByteArrayOutputStream> list = getList();
		synchronized (list) {
			list.addLast(baos);
			
			list.notifyAll();
		}
	}	
	
	public static ByteArrayOutputStream pop(){
		LinkedList<ByteArrayOutputStream> list = getList();
		ByteArrayOutputStream baos;
		synchronized (list) {
			baos = list.pop();
		}
		return baos;
	}

}
