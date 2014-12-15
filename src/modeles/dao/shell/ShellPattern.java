package modeles.dao.shell;

import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.GetStateAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beanshell.ShellCmd;

public class ShellPattern {

	//private static String startSteamWCTour = "/usr/local/bin/mjpg_streamer -i \"/usr/local/lib/input_uvc.so -f 10r VGA\" -o \"/usr/local/lib/output_http.so -w /var/www/cam\" 2>&1";
	public static String startSteamWCTourName = "Streaming Webcam ON";
	private static String [] startSteamWCTour = {
		"/usr/local/bin/mjpg_streamer",
		"-i", "/usr/local/lib/input_uvc.so -f 10r VGA",
		"-o", "/usr/local/lib/output_http.so -w /var/www/cam"};
	
	public static String stopSteamWCTourName = "Streaming Webcam OFF";
	private static String [] stopSteamWCTour = {"pkill", "mjpg_streamer"};
	
	public static String testName = "ping Google";
	private static String [] test = {"ping", "-c", "1", "www.google.fr"};
	
	public static String stateStreamingName = "State Streaming";
	private static String [] stateStreaming = {"ps", "-ef", "|grep", "mjpg_streamer"};
	
	public static String stateAlimName = "State Alim";
	private static String [] stateAlim = {""};
	
	
	protected ShellPattern() {
	}

	public static ShellCmd actionToShell( IAction ia ){
		ShellCmd shellCmd = null;
		
		if( ia instanceof ExtraAction ){
			shellCmd = extraToShell( (ExtraAction) ia);
		
		}else if( ia instanceof GetStateAction ){
			shellCmd = getStateToShell((GetStateAction)ia);
		}
			
		return shellCmd;
	}
	
	protected static ShellCmd extraToShell( ExtraAction ea ){
		ShellCmd shellCmd = new ShellCmd();
		String [] cmd = {};
		if( ea.getType() == IAction.typeWebcam ){
			if( ea.getKey() == IAction.webcamTour ){
				if( ea.getValue() == 0 ){
					cmd = stopSteamWCTour;
					
					shellCmd.setName(stopSteamWCTourName);
				}else{
					cmd = startSteamWCTour;
					
					shellCmd.setName(startSteamWCTourName);
				}
			}
		}else if( ea.getType() == IAction.typeAlim ){
			cmd = test;
			
			shellCmd.setName(testName);
		}
		
		shellCmd.setCommand(cmd);
		return shellCmd;
	}
	
	protected static ShellCmd getStateToShell( GetStateAction gsa ){
		ShellCmd shellCmd = new ShellCmd();
		String [] cmd = {};
		
		if( gsa.getType() == IAction.typeWebcam ){
			cmd = stateStreaming;
			shellCmd.setName(stateStreamingName);
			
		}else if( gsa.getType() == IAction.typeAlim ){
			cmd = stateAlim;
			shellCmd.setName(stateAlimName);
			
		}
		
		shellCmd.setCommand(cmd);
		return shellCmd;
	}
	
}
