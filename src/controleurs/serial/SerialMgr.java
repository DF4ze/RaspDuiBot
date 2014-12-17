package controleurs.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import modeles.ServeurModele;
import modeles.Verbose;

public class SerialMgr {
	
	private InputStream in;
	private OutputStream out;
	private ServeurModele mod;
	private static SerialPort serialPort = null;

	public SerialMgr( ServeurModele mod ) {
		this.mod = mod;
	}

	public void connect() throws PortInUseException, UnsupportedCommOperationException, IOException, NoSuchPortException {

		System.setProperty("gnu.io.rxtx.SerialPorts", mod.getsSerialPort());
//		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyAMA0");
	//	System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(mod.getsSerialPort());

		if (portIdentifier.isCurrentlyOwned()) {
			System.err.println("Error: Port is currently in use");

		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), mod.getiSerialTimeOut());

			if (commPort instanceof SerialPort) {
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(mod.getiSerialSpeed(), SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				
				SerialLauncher sl = new SerialLauncher(in, out);
				Thread th = new Thread( sl );
				th.setDaemon(true);
				th.start();
				
				if( Verbose.isEnable() )
					System.out.println("Port serie ("+ mod.getsSerialPort() +") started");
			} else {
				System.err.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public static void close(){
		serialPort.close();
		if( Verbose.isEnable() )
			System.out.println("Port serie ferme...\nWill exit now...");
		
	}
}
