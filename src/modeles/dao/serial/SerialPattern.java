package modeles.dao.serial;

import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansinfos.IInfo;
import modeles.dao.communication.beansinfos.SensorInfo;
import modeles.dao.communication.beansinfos.StateInfo;
import modeles.dao.communication.beansinfos.TextInfo;

public class SerialPattern {
	private static final String codeWork		= "[data]";
	
	private static final String codeSensor 		= "[sensor]";
	private static final String codeSensDist 	= "[distance]";
	private static final String codeSensLum 	= "[lumiere]";
	private static final String codeSensTemp 	= "[temperature]";

	private static final String codeLight		= "[light]";
	private static final String codeLightSpot 	= "[spot]";
	private static final String codeLightLazer 	= "[lazer]";
	private static final String codeLightStrob 	= "[strob]";


	private SerialPattern() {
	}

	public static String actionToSerial( IAction ia ){
		return ia.toString();
	}
	
	
	/**
	 * Sserial doit etre de la forme :
	 * [data]	[sensor]	[distance]		int
	 * 						[lumiere]		int
	 * 						[temperature]	int
	 * 
	 * 			[light]		[spot]		boolean
	 * 						[lazer]		boolean
	 * 						[strob]		boolean
	 * 			
	 * @param sSerial
	 * @return
	 */
	public static IInfo serialToIInfo( String sSerial ){
		IInfo info = null;
		
		if( sSerial.indexOf(codeWork) != -1 ){
			sSerial.replace(codeWork, "");
			
			
			if( sSerial.indexOf(codeSensor) != -1 ){
				int sensor = -1;
				sSerial.replace(codeSensor, "");
				if( sSerial.indexOf(codeSensDist) != -1 ){
					sSerial.replace(codeSensDist, "");
					sensor = IInfo.sensorDistance;
					
				}else if( sSerial.indexOf(codeSensLum) != -1 ){
					sSerial.replace(codeSensLum, "");
					sensor = IInfo.sensorLum;
					
				}else if( sSerial.indexOf(codeSensTemp) != -1 ){
					sSerial.replace(codeSensTemp, "");
					sensor = IInfo.sensorTemp;
				}
				
				if( sensor != -1 ){
					try{
						info = new SensorInfo( sensor, Integer.parseInt(sSerial) );
					}catch( Exception e ){
						System.err.println( "La valeur du capteur "+sensor+" ne peut pas etre parsee" );
					}
				}
			
			}else if(sSerial.indexOf(codeLight) != -1){
				sSerial.replace(codeLight, "");
				
				int matos = -1;
				if( sSerial.indexOf(codeLightLazer) != -1){
					sSerial.replace(codeLightLazer, "");
					matos = IInfo.stateLazer;
					
				}else if( sSerial.indexOf(codeLightSpot) != -1){
					sSerial.replace(codeLightSpot, "");
					matos = IInfo.stateLight;
					
				}else if( sSerial.indexOf(codeLightStrob) != -1){
					sSerial.replace(codeLightStrob, "");
					matos = IInfo.stateStrob;
					
				}
				
				if( matos != -1 ){
					try{
						info = new StateInfo( matos, Boolean.parseBoolean(sSerial) );
					}catch( Exception e ){
						System.err.println( "La valeur du materiel "+matos+" ne peut pas etre parsee" );
					}
					
				}
			}
		}else{
			info = new TextInfo(sSerial);
		}
		
//		if( Verbose.isEnable() )
//			System.out.println("serialToIInfo : "+ info);
		
		return info;
	}
}
