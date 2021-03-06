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
	private Thread th;
	private SerialPort serialPort = null;
	private static SerialMgr me = null;

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
				th = new Thread( sl );
				th.setDaemon(true);
				th.start();
				
				if( Verbose.isEnable() )
					System.out.println("Port serie ("+ mod.getsSerialPort() +") started");
			} else {
				System.err.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public static boolean close(){
		boolean isOk = true;
		if( me != null && me.serialPort != null){
			me.serialPort.close();
			me.th.interrupt();
			
			if( Verbose.isEnable() )
				System.out.println("Port serie ferme...");
		}else
			isOk = false;

		
		return isOk;
	}	
	public static boolean restart() throws PortInUseException, UnsupportedCommOperationException, IOException, NoSuchPortException{
		boolean isOk = true;
		if( me != null ){
			me.connect();
			
			if( Verbose.isEnable() )
				System.out.println("Port serie redemarre...");
		}else
			isOk = false;
		
		
		return isOk;
	}
}
