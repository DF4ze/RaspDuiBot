package modeles.dao.shell;

import java.util.LinkedList;

import modeles.ServeurModele;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.GetStateAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beansactions.VolumeAction;
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
	@SuppressWarnings("unused")
	private static String [] test = {"ping", "-c", "1", "www.google.fr"};
	
	public static String stateStreamingName = "State Streaming";
	private static String [] stateStreaming = {""};//{"ps", "-eo","cmd"};
//	private static String [] stateStreaming = {"ps", "-ef", "|grep", "mjpg_streamer"};
	
	public static String resetPinAlimName = "Declare pin alim OUT";
	private static String [] resetPinAlim = {"gpio", "-g", "mode", Integer.toString(ServeurModele.DEFAULT_PINALIM), "out"};
	
	public static String pinAlimOnName = "pin alim ON";
	private static String [] pinAlimOn = {"gpio", "-g", "write", Integer.toString(ServeurModele.DEFAULT_PINALIM), "1"};
	
	public static String pinAlimOffName = "pin alim OFF";
	private static String [] pinAlimOff = {"gpio", "-g", "write", Integer.toString(ServeurModele.DEFAULT_PINALIM), "0"};
	
	public static String pinAlimStateName = "pin State";
	private static String [] pinAlimState = {"gpio", "-g", "read", Integer.toString(ServeurModele.DEFAULT_PINALIM)};
	
	public static String speakName = "Speaking";
	private static String [] speak_espeak = {"/usr/bin/espeak", "-vfr+15", "-k20", "-s150", "Text To Speech", "> /dev/null 2>&1"};
	//private static String [] speak_pico = {"/root/scripts/pico2wave/pico2wave", "Text to Speech", ">", "/dev/null", "2>&1", "&&", "aplay", "outputfile.wav", ">", "/dev/null", "2>&1"};
	private final static String pico2wavePath = "/root/scripts/pico2wave/";
	private static String [] synth_pico = {pico2wavePath+"pico2wave","-o",pico2wavePath+"outputfile.wav", "Text to Speech" };
	private static String [] speak_pico = {"aplay", pico2wavePath+"outputfile.wav" };

	public static String volumeName = "Volume";
	private static String [] volume = {"amixer", "-c", "0", "-M", "--", "sset", "PCM", "playback", "10%+"};

	public static String stateVolumeName = "State Volume";
	private static String [] stateVolume = {"amixer", "get", "PCM", "-M"};

	//amixer -c 0 -M -- sset PCM playback 10%-
//	public static String stateWebcamName = "State Webcam";
//	private static String [] stateWebcam = {"ps", "-ef", "|grep", "mjpg_streamer"};
	
//	public static String stateAlimName = "State Alim";
//	public static String stateModePinName = "State Alim - mode pin";
//	private static String [] stateModePin = {"gpio", "-g", "mode", Integer.toString(ServeurModele.DEFAULT_PINALIM), "in"};
//	
//	public static String stateReadPinName = "State Alim - read pin";
//	private static String [] stateReadPin = {"gpio", "-g", "read", Integer.toString(ServeurModele.DEFAULT_PINALIM)};
	
	
	protected ShellPattern() {
	}

	public static ShellCmd actionToShell( IAction ia ){
		ShellCmd shellCmd = null;
		
		if( ia instanceof ExtraAction ){
			shellCmd = extraToShell( (ExtraAction) ia);
		
		}else if( ia instanceof GetStateAction ){
			shellCmd = getStateToShell((GetStateAction)ia);
			
		}else if( ia instanceof SpeakAction ){
			shellCmd = speakToShell( (SpeakAction)ia );
		
		}else if( ia instanceof VolumeAction ){
			shellCmd = volumeToShell( (VolumeAction)ia );
		
		}
			
		return shellCmd;
	}
	
	public static void actionToShell( IAction ia, LinkedList<ShellCmd> alShellCmd  ){
		
		if( ia instanceof ExtraAction ){
			alShellCmd.add( extraToShell( (ExtraAction) ia));
		
		}else if( ia instanceof GetStateAction ){
			getStateToShell((GetStateAction)ia, alShellCmd );
			
		}if( ia instanceof SpeakAction ){
			SpeakToShell((SpeakAction)ia, alShellCmd );
		}
			
		
	}
	
	protected static ShellCmd extraToShell( ExtraAction ea ){
		ShellCmd shellCmd = new ShellCmd();
		String [] cmd = {};
		if( ea.getType() == IAction.typeWebcam ){
			if( ea.getKey() == IAction.webcamTour ){
				if( ea.getValue() == IAction.Off ){
					cmd = stopSteamWCTour;
					shellCmd.setName(stopSteamWCTourName);
					
				}else{
					cmd = startSteamWCTour;
					shellCmd.setName(startSteamWCTourName);
				}
			}
		}else if( ea.getType() == IAction.typeAlim ){
			if( ea.getKey() == IAction.alimStandBy ){
				if( ea.getValue() == IAction.Off ){
					cmd = pinAlimOn;
					shellCmd.setName(pinAlimOnName);
					
				}else if( ea.getValue() == IAction.On ){
					cmd = pinAlimOff;
					shellCmd.setName(pinAlimOffName);
					
				}else{
					cmd = resetPinAlim;
					shellCmd.setName(resetPinAlimName);
				}
			}
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
			cmd = pinAlimState;
			shellCmd.setName(pinAlimStateName);
			
		}else if( gsa.getType() == IAction.typeVolume ){
			cmd = stateVolume;
			shellCmd.setName(stateVolumeName);
			
		}
		
		shellCmd.setCommand(cmd);
		return shellCmd;
	}
	
	protected static void getStateToShell( GetStateAction gsa, LinkedList<ShellCmd> shellCmds ){
		
		if( gsa.getType() == IAction.typeWebcam ){
			ShellCmd shellCmd = new ShellCmd();
			shellCmd.setCommand(stateStreaming);
			shellCmd.setName(stateStreamingName);
			shellCmds.addLast( shellCmd );
			
		}/*else if( gsa.getType() == IAction.typeAlim ){
			ShellCmd shellCmd = new ShellCmd();
			
			shellCmd.setCommand(stateModePin);
			shellCmd.setName(stateModePinName);
			shellCmds.add( shellCmd );
			
			shellCmd.setCommand(stateReadPin);
			shellCmd.setName(stateReadPinName);
			shellCmds.add( shellCmd );
		}*/
	}
	
	protected static ShellCmd speakToShell( SpeakAction sa ){
		// {"/usr/bin/espeak", "-vfr+15", "-k20", "-s150", "Test TTS"}
		
		ShellCmd shellCmd = new ShellCmd();
		shellCmd = new ShellCmd();
		shellCmd.setName(speakName);
		
		if( sa.getTss() == SpeakAction.ttsEspeak )
			shellCmd.setCommand( espeakToCmd(sa) );
		
		/*else if( sa.getTss() == SpeakAction.ttsPico )
			shellCmd.setCommand( picoToCmd(sa) );
		*/
		return shellCmd;
	}
	
	protected static void SpeakToShell( SpeakAction sa, LinkedList<ShellCmd> alShellCmd ){
		ShellCmd shellCmd1 = new ShellCmd();
		shellCmd1.setName(speakName);
		
		ShellCmd shellCmd2 = new ShellCmd();
		shellCmd2.setName(speakName);
		
		if( sa.getTss() == SpeakAction.ttsPico ){
			shellCmd1.setCommand( picoSynthToCmd(sa) );
			alShellCmd.addLast(shellCmd1);
			shellCmd2.setCommand(speak_pico);
			alShellCmd.addLast(shellCmd2);
		}
	}
	
	private static String[] espeakToCmd( SpeakAction sa ){
		String[] cmd = speak_espeak;
		
		switch( sa.getVoix() ){
		case SpeakAction.voixFemme :
			cmd[1] = "-vfr+15";
			break;
		case SpeakAction.voixHomme :
			cmd[1] = "-vfr+1";
			break;
		default :
			cmd[1] = "-vfr+15";
		}
		
		switch( sa.getVitesse() ){
		case SpeakAction.voixRapide :
			cmd[3] = "-s250";
			break;
		case SpeakAction.voixMoyenne :
			cmd[3] = "-s150";
			break;
		case SpeakAction.voixLente :
			cmd[3] = "-s50";
			break;
		default :
			cmd[3] = "-s150";
		}
		
		cmd[4] = sa.getAction();		
		
		return cmd;
	}
	
	private static String[] picoSynthToCmd( SpeakAction sa ){
		String[] cmd = synth_pico;
		
		cmd[3] = sa.getAction();
		
		return cmd;
		
	}

	
	
	private static ShellCmd volumeToShell( VolumeAction va ){
		ShellCmd shellCmd = new ShellCmd();
		shellCmd = new ShellCmd();
		shellCmd.setName(volumeName);

		// {"amixer", "-c", "0", "-M", "--", "sset", "PCM", "playback", "10%+"};
		if( va.isMuting() ){
			volume[8] = (va.isSens()?va.getPourcent():"0")+"%";
		}else{
			volume[8] = va.getPourcent()+"%"+(va.isSens()?"+":"-");
		}

		shellCmd.setCommand(volume);
		
		return shellCmd;
	}
}
