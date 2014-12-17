package modeles.dao.shell;

import java.util.ArrayList;

import modeles.ServeurModele;
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
	private static String [] stateStreaming = {"ps", "-eo","cmd"};
//	private static String [] stateStreaming = {"ps", "-ef", "|grep", "mjpg_streamer"};
	
//	public static String stateWebcamName = "State Webcam";
//	private static String [] stateWebcam = {"ps", "-ef", "|grep", "mjpg_streamer"};
	
	public static String stateAlimName = "State Alim";
	public static String stateModePinName = "State Alim - mode pin";
	private static String [] stateModePin = {"gpio", "-g", "mode", Integer.toString(ServeurModele.DEFAULT_PINALIM), "in"};
	
	public static String stateReadPinName = "State Alim - read pin";
	private static String [] stateReadPin = {"gpio", "-g", "read", Integer.toString(ServeurModele.DEFAULT_PINALIM)};
	
	
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
	
	public static void actionToShell( IAction ia, ArrayList<ShellCmd> alShellCmd  ){
		
		if( ia instanceof ExtraAction ){
			alShellCmd.add( extraToShell( (ExtraAction) ia));
		
		}else if( ia instanceof GetStateAction ){
			getStateToShell((GetStateAction)ia, alShellCmd );
		}
			
		
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
			int lenght = stateModePin.length + stateReadPin.length;
			String[] concat = new String [lenght+1];
			
			System.arraycopy(stateModePin, 0, concat, 0, stateModePin.length);
			concat[ stateModePin.length ] = "\n";
			System.arraycopy(stateReadPin, 0, concat, stateModePin.length+1, stateReadPin.length);
			
			cmd = concat;
			shellCmd.setName(stateAlimName);
			
		}
		
		shellCmd.setCommand(cmd);
		return shellCmd;
	}
	
	protected static void getStateToShell( GetStateAction gsa, ArrayList<ShellCmd> shellCmds ){
		
		if( gsa.getType() == IAction.typeWebcam ){
			ShellCmd shellCmd = new ShellCmd();
			shellCmd.setCommand(stateStreaming);
			shellCmd.setName(stateStreamingName);
			shellCmds.add( shellCmd );
			
		}else if( gsa.getType() == IAction.typeAlim ){
			ShellCmd shellCmd = new ShellCmd();
			
			shellCmd.setCommand(stateModePin);
			shellCmd.setName(stateModePinName);
			shellCmds.add( shellCmd );
			
			shellCmd.setCommand(stateReadPin);
			shellCmd.setName(stateReadPinName);
			shellCmds.add( shellCmd );
		}
		
		
	}
	
}
