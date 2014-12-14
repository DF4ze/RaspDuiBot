package controleurs.serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import modeles.ServeurModele;

public class SerialMgr {
	
	private InputStream in;
	private OutputStream out;
	private ServeurModele mod;

	public SerialMgr( ServeurModele mod ) {
		this.mod = mod;
	}

	public void connect() throws Exception {

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(mod.getsSerialPort());

		if (portIdentifier.isCurrentlyOwned()) {
			System.err.println("Error: Port is currently in use");

		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(), mod.getiSerialTimeOut());

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(mod.getiSerialSpeed(), SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				
				SerialLauncher sl = new SerialLauncher(in, out);
				Thread th = new Thread( sl );
				th.setDaemon(true);
				th.start();

				//(new Thread(new SerialReader(in))).start();
				//(new Thread(new SerialWriter(out))).start();

			} else {
				System.err.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

}
