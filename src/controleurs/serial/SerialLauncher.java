package controleurs.serial;

import java.io.InputStream;
import java.io.OutputStream;

import controleurs.serial.com.SerialRecept;
import controleurs.serial.com.SerialTransmit;

public class SerialLauncher implements Runnable{
	
	private InputStream in;
	private OutputStream out;


	public SerialLauncher( InputStream in, OutputStream out ){
		this.in = in;
		this.out = out;
	}


	@Override
	public void run() {
		SerialRecept sr = new SerialRecept( in );
		SerialTransmit.getInstance( out );
		
		Thread th1 = new Thread(sr);
		th1.setDaemon(true);
		th1.start();
	}

}
