package modeles.dao.communication.beansactions;

import java.io.Serializable;

public interface IAction extends Serializable {
	//// Priorities
	final int prioLow 		= 0;
	final int prioMedium 	= 1;
	final int prioHight 	= 2;
	final int prioHighest 	= 3;
	//// Modes
	final int modeMotor		= 1;
	final int modeServo		= 2;
	final int modeExtra		= 3;
	final int modeState		= 4;
	final int modeSpeak		= 5;
	
	//// Extras
	final int typeAll		= 0;
	final int typeLight		= 1;
	final int typeWebcam	= 2;
	final int typeAlim		= 3;
	final int typeDrone		= 4;
	final int typeVolume	= 5;
	
	// Extra-Lights
	final static int Lazer 	= 1;
	final static int Strobe = 2;
	final static int Light 	= 3;
	
	// Extra-Webcam
	final static int Off		= 0;
	final static int On 		= 1;
	final static int Restart	= 3;
	
	final static int webcamTour	= 1;

	// Extra-Alims
	final static int alimStandBy = 1;
	
	// Extra-Drone
	final static int DroneAuto 		= 1;
	final static int DroneSemi 		= 2;
	final static int DroneManuel  	= 3;
	
	// State
	final static int stateAll = 0;
	
	// Serial char
	final static String serialEndCommand = "*";
	
	public String getAction();
	public long getTimeStamp();
	public int getPriority();
	public boolean isComplete();
	public boolean isRepeatable();
	public String toString();
}
