package controleurs.serial.com;

import java.io.IOException;
import java.io.OutputStream;

import modeles.Verbose;

public class SerialTransmit {

	private static OutputStream out = null;
	private static SerialTransmit me = null;

	protected SerialTransmit(OutputStream os) {
		out = os;
	}
	
	public static SerialTransmit getInstance(){
		if( me == null ){
			if( out != null )
			me = new SerialTransmit(out);
		}
		return me;
	}
	public static SerialTransmit getInstance( OutputStream os ){
		if( me == null ){
			me = new SerialTransmit(os);
		}else if( !os.equals(out) ){
			me = new SerialTransmit(os);
		}
		return me;
	}
	
	public static void setOutputStream( OutputStream os ){
		out = os;
	}

	public static boolean send( String toSend ) {
		boolean bOk = true;
		try {
			byte[] bChars = toSend.getBytes();
			for( int i=0; i<bChars.length; i++ ) {
				out.write(bChars[i]);
			}
		} catch (IOException e) {
			bOk = false;
			if( Verbose.isEnable() )
				System.err.println("Erreur de transmission : "+e.getMessage());
		}
		return bOk;
	}
}
