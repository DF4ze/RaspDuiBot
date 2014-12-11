package modeles.dao.shell;

import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;

public class ShellPattern {

	private static String startSteamWCTour = "mjpg_streamer -i \"/usr/local/lib/input_uvc.so -f 10r VGA\" -o \"/usr/local/lib/output_http.so -w /var/www/cam\"";
	private static String stopSteamWCTour = "pkill mjpg_streamer";
	
	protected ShellPattern() {
		// TODO Auto-generated constructor stub
	}

	public static String extraToShell( ExtraAction ea ){
		String shellCmd = "";
		if( ea.getType() == IAction.typeWebcam ){
			if( ea.getKey() == IAction.webcamTour ){
				if( ea.getValue() == 0 ){
					shellCmd = stopSteamWCTour;
				}else
					shellCmd = startSteamWCTour;
			}
		}
		return shellCmd;
	}
	
}
