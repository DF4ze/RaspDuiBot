package controleurs.network;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beanshell.ShellCmd;

public class LanChecker extends Thread{

	public static final int INITIALISING = 0;
	public static final int SEARCHING = 1;
	public static final int FINISH = 2;

	private int WifiState = INITIALISING;
	private boolean success = false;
	private final File confDir = new File("/etc/network/myWlans/");
	private final String interfaceFileName = "/etc/network/interfaces";
	
	private boolean checkEth = false;
	private boolean checkWlan = false;
	
	public LanChecker( boolean checkWlan, boolean checkEth ) {
		this.checkEth = checkEth;
		this.checkWlan = checkWlan;
	}

	protected ArrayList<File> getConfWifiFiles(){
		File arcFiles[] = confDir.listFiles(new FilenameFilter() 
		{
			public boolean accept(File dir, String name) 
			{
				return name.endsWith(".conf");
			}
		});
		
		return new ArrayList<File>(  Arrays.asList(arcFiles) );
	}
	public void run(){
		boolean initEth0 = false;
		boolean initWlan0 = false;
		
		HashMap<String, String> InitIps = NetworkManager.retreiveIps();
		for( Entry<String, String> ip : InitIps.entrySet()){
			if( ip.getKey().equals("wlan0") ){
				initWlan0 = true;
			
			}else if( ip.getKey().equals("eth0") ){
				initEth0 = true;
			}
				
		}
		
		
		setLanState(LanChecker.SEARCHING);
		
		if( !initWlan0 && checkWlan ){
			success = testWifiConfigs();
		}
		
		if( !initEth0 && checkEth ){
			testEthConfig(); // la connexion a l'ethernet n'influ as sur le succes
		}
	}
	
	private boolean testEthConfig(){
		String [] downEth0Cmd = {"ifdown", "eth0"};
		ShellCmd downEth0ShellCmd = new ShellCmd("down interface", downEth0Cmd );
		FifoSenderShell.put(downEth0ShellCmd);
		
		synchronized (downEth0ShellCmd) {
			try {
				downEth0ShellCmd.wait();
			} catch (InterruptedException e) {}
		}
		
		String [] upEth0Cmd = {"ifup", "eth0"};
		ShellCmd upEth0ShellCmd = new ShellCmd("up interface", upEth0Cmd );
		FifoSenderShell.put(upEth0ShellCmd);
		
		synchronized (upEth0ShellCmd) {
			try {
				upEth0ShellCmd.wait();
			} catch (InterruptedException e) {}
		}	
		boolean success = false;
		HashMap<String, String> ips = NetworkManager.retreiveIps();
		for( Entry<String, String> ip : ips.entrySet()){
			if( ip.getKey().equals("wlan0") ){
				success = true;
				break;
			}
		}
		return success;
	}
	
	private boolean testWifiConfigs(){
		ArrayList<File> configs = getConfWifiFiles();
		
		boolean success = false;
		CONFIG:for( File config : configs){
			if( Verbose.isEnable() )
				System.out.println("Testing WIFI with : '"+config.getAbsolutePath()+"'");
			
			String [] downWlan0Cmd = {"ifdown", "wlan0"};
			ShellCmd downWlan0ShellCmd = new ShellCmd("down interface", downWlan0Cmd );
			FifoSenderShell.put(downWlan0ShellCmd);
			
			synchronized (downWlan0ShellCmd) {
				try {
					downWlan0ShellCmd.wait(5000);
				} catch (InterruptedException e) {}
			}
			
			
			String [] cpCmd = {"cp", config.getAbsolutePath(), interfaceFileName};
			ShellCmd cpShellCmd = new ShellCmd("copy interface", cpCmd );
			FifoSenderShell.put(cpShellCmd);
			
			synchronized (cpShellCmd) {
				try {
					cpShellCmd.wait(5000);
				} catch (InterruptedException e) {}
			}			
			
			String [] upWlan0Cmd = {"ifup", "wlan0"};
			ShellCmd upWlan0ShellCmd = new ShellCmd("up interface", upWlan0Cmd );
			FifoSenderShell.put(upWlan0ShellCmd);
			
			synchronized (upWlan0ShellCmd) {
				try {
					upWlan0ShellCmd.wait(5000);
				} catch (InterruptedException e) {}
			}			
			
			HashMap<String, String> ips = NetworkManager.retreiveIps();
			for( Entry<String, String> ip : ips.entrySet()){
				if( ip.getKey().equals("wlan0") ){
					success = true;
					break CONFIG;
				}
					
			}
		}	
		return success;
	}

	public int getWifiState() {
		return WifiState;
	}

	public void setLanState(int wifiState) {
		WifiState = wifiState;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
