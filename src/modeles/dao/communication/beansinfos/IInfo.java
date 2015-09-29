package modeles.dao.communication.beansinfos;

import java.io.Serializable;

public interface IInfo extends Serializable{

	// Type d'info
	final static int typeSensor 	= 0;
	final static int typeState 		= 1;
	
	
	// Sensor
	final static int sensorDistance = 0;
	final static int sensorTemp 	= 1;
	final static int sensorLum 		= 2;
	final static int sensorSpeed 	= 3;
	final static int sensorVolt 	= 4;
	
	// State matos
	final static int stateAlim 		= 0;
	final static int stateStream 	= 1;
	
	final static int stateLight 	= 2;
	final static int stateLazer 	= 3;
	final static int stateStrob 	= 4;
	
	

	public String getInfo();
	public String toString();

}
