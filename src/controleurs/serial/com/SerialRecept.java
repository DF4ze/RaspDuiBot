package controleurs.serial.com;

import java.io.IOException;
import java.io.InputStream;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSerial;
import modeles.dao.serial.SerialPattern;

public class SerialRecept implements Runnable {

	private InputStream in;
	private String inputString = "";
	private boolean cmdEnded = false;
	private byte cmdDelim = '\n';

	public SerialRecept(InputStream is) {
		in = is;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		while( true ){
			int len = -1;
			try {
				while( !cmdEnded && (len = this.in.read(buffer, 0, 1)) > -1 ) {
					if( buffer[0] == cmdDelim ){
						cmdEnded = true;
					}else
						if( buffer[0] >= 32 ) // filtre les char non visibles
							inputString += new String(buffer, 0, len);
				}
				
				FifoReceiverSerial.put( SerialPattern.serialToIInfo( inputString ) );
				
				if( Verbose.isEnable() )
						System.out.println( "Serial receive Brut : "+ inputString);
				inputString = "";
				cmdEnded = false;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
