package controleurs.serial.com;

import java.io.IOException;
import java.io.InputStream;

import modeles.dao.communication.beanfifo.FifoReceiverSerial;
import modeles.dao.serial.SerialPattern;

public class SerialRecept implements Runnable {

	private InputStream in;

	public SerialRecept(InputStream is) {
		in = is;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = this.in.read(buffer)) > -1) {
				FifoReceiverSerial.put( SerialPattern.serialToIInfo(new String(buffer, 0, len)) );
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
