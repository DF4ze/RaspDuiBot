package modeles.dao.serial;

import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansinfos.IInfo;

public class SerialPattern {
	private static final String codeWork = "[data]";

	private SerialPattern() {
	}

	public static String actionToSerial( IAction ia ){
		return ia.toString();
	}
	
	public static IInfo serialToIInfo( String sSerial ){
		IInfo info = null;
		
		if( sSerial.indexOf(codeWork) != -1 ){
			
			
		}
		
		return info;
	}
}
