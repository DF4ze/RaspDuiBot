package modeles.dao.communication.beansinfos;

public interface IInfo {

	// Type d'info
	final static int typeSensor 	= 0;
	final static int typeState 	= 1;
	
	
	// Sensor
	final static int sensorDistance 	= 0;
	final static int sensorTemp 		= 1;
	final static int sensorLum 		= 2;
	final static int sensorSpeed 	= 3;
	
	// State matos
	final static int stateAlim 		= 0;
	final static int stateStream 	= 1;

	public String getInfo();
	public String toString();

}
