package controleurs.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.communication.beanshell.ShellResult;
import modeles.dao.communication.beansinfos.PingInfo;
import modeles.dao.communication.beansinfos.WifiInfo;
import controleurs.audio.Audio;

public class NetworkManager extends TimerTask{
	
	private HashMap<String, String> IPList = null;
	private static Timer timer;
	private int count = 0;
	private int unSuccessConnection = 0;
	private int unSuccessPing = 0;
	private static boolean wifiDongleConnected = false;
	private ServeurModele srvMod;
	private static NetworkManager me;
	
	private static boolean connected = false;
	public static boolean isConnected() {
		return connected;
	}

	protected void setConnected(boolean isConnected) {
		NetworkManager.connected = isConnected;
	}

	
	
	private NetworkManager( ServeurModele m ) {
		srvMod = m;
	}
	
	public static NetworkManager launch(ServeurModele m){
		if( me == null ){
	   		timer = new Timer();
	   		long rate = 1000l; // 1s
	   		me = new NetworkManager( m );
	   		
	   		try {
				Thread.sleep(4000); // laisse le temps au serveur de démarrer
			} catch (InterruptedException e) {}
	   		
	   		timer.scheduleAtFixedRate( me, rate, rate);
		}
		return me;
	}

	@Override
	public void run() {
		if( Verbose.isEnable() )
			System.out.println("Checking lan");
		
		checkingInterfaces();
		checkingClients();

		
	}
	
	private void checkingClients(){
		
		if( srvMod.getClientsConnected().size() != 0 ){
			if( unSuccessPing > 5 ){
				srvMod.delAllClient();
				try {
					srvMod.releaseAllConnexion();
				} catch (InterruptedException e) {}
			}
				
			FifoSenderSocket.put(new PingInfo(new Date().getTime()));
			unSuccessPing ++;
		}
	}

	private void checkingInterfaces(){

		if( count == 0 ){
			boolean dongleTest = isWifiDongleConnected();
			HashMap<String, String> newIPList = retreiveIps();
			if( !newIPList.containsKey("wlan0") && dongleTest){
				LanChecker lc = new LanChecker(dongleTest, false);
				lc.start();
				
				try {
					lc.join();
				} catch (InterruptedException e) {}
				
				if( lc.isSuccess() )
					Audio.speak(new SpeakAction("Je me suis permi de connecter le OUIFI."));
			}
				
		}
		
		
		
		
		boolean changed = false;
		HashMap<String, String> newIPList = retreiveIps();
		if( IPList != null )
			if( newIPList.size() != IPList.size() )
				changed = true;
		IPList = newIPList;
		
		

		
		
		if( !connected && IPList.size() != 0 || changed ){
			// on vient de se connecter à un réseau
			connected = true;

			//...
			String texte;
			if( changed )
				texte = "Changement dans les connexions. ";
			else
				texte = "Connecté au réseau. ";
			
			if( IPList.size() > 1 ){
				texte += IPList.size()+" adresses IP. ";
				
			}
			
			int nb = 0;
			for( Entry<String, String> ip : IPList.entrySet() ){
				String textForInterface = ip.getKey().equals("wlan0")?"le OUIFI":(ip.getKey().equals("eth0")?"éternet":ip.getKey());
				String textForIP = ip.getValue().replace(".", ". point ");
				
				texte += textForIP+", sur "+textForInterface;
				nb++;
				if( nb < IPList.size() ){
					if( nb == IPList.size()-1 )
						texte += " et ";
					else
						texte += ", ";
				}
			}
				
			SpeakAction sa = new SpeakAction(texte);
			Audio.speak(sa);
		}
		
		if( connected && IPList.size() == 0){
			// on vient de perdre le réseau
			connected = false;
			// tenter de récupérer le réseau
			count = 20;
			
			//...
			String texte = "Je n'ai plus de réseau";
			SpeakAction sa = new SpeakAction(texte);
			Audio.speak(sa);
		}		
		
		if( !connected && count == 0 ){
			String texte = "Je n'ai pas encore de réseau, merci de patienter.";
			SpeakAction sa = new SpeakAction(texte);
			Audio.speak(sa);
			
			
			
			
		
		}else if( !connected && count == 20 ){
			String texte;
			if( unSuccessConnection%2 == 0 ){
				texte = "Je vais voir si je peux résoudre ce problème de réseau";
				SpeakAction sa = new SpeakAction(texte);
				Audio.speak(sa);
					
				
				LanChecker lc = new LanChecker(isWifiDongleConnected(), true);
				lc.start();
				
				try {
					lc.join();
				} catch (InterruptedException e) {}
				
				connected = lc.isSuccess();
				
			}else if( unSuccessConnection%2 == 1 ){
				texte = "Je suis désolée, je n'ai pas pu établir de connexion réseau";
				SpeakAction sa = new SpeakAction(texte);
				Audio.speak(sa);
					
				
				
				if( isWifiDongleConnected() ){
					texte = "Vous pouvez activer un partage de connexion avec votre téléphone."+
							" Nom du partage : android A P, avec le A et le P en majuscule."+
							" et pareil en mot de passe."+
							" Je me connecterais automatiquement dessus.";
					sa = new SpeakAction(texte);
					Audio.speak(sa);
					
					
				
				}else{
					texte = "Je ne reconnais pas de clé wifi."+
							" Je ne peux que me connecter avec le cable éternet.";
					sa = new SpeakAction(texte);
					Audio.speak(sa);
						
				}
			}
			

			
			count = 1;
			unSuccessConnection++;
		}
		
		if( IPList.containsKey("wlan0") && srvMod.getClientsConnected().size() != 0 ){
			WifiInfo wi = new WifiInfo( getWifiInfos() );
			FifoSenderSocket.put(wi);
		}
		
		
		if( count == Integer.MAX_VALUE )
			count = 1;
		
		count ++;		
	}
	
	
	/**
	 * Retourne toutes les adresses ips des carte réseau de la machine. Retourne seulement les addresses IPV4
	 * 
	 * @return Une liste des addresses ip
	 */
	public static HashMap<String, String> retreiveIps(){
		HashMap<String, String> IPList = new HashMap<String, String>();
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	 
//			boolean isWifiDongle = false;
	         while (interfaces.hasMoreElements()) {  // carte reseau trouvee
	            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
	            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
	            
	            //String niName = interfaceN.getName().toLowerCase();
	            //System.out.println("---- NetworkInterface Name : "+niName);
	            
//	            if( niName.contains("wifi") || niName.contains("netgear"))
//	            	isWifiDongle = true;
	            
	            while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
	                InetAddress ia = ienum.nextElement();
	                String adress = ia.getHostAddress().toString();
	                
	                if( adress.startsWith("192") || adress.startsWith("10") ){
	                	IPList.put( interfaceN.getDisplayName() , adress);     
	                }
	            }
	        }
//	        wifiDongleConnected = isWifiDongle;
	    }
	    catch(Exception e){
	        //System.out.println("pas de carte reseau");
	        //e.printStackTrace();
	    }
		return IPList;
	}


	public void pingReceived( long timeStamp ){
		unSuccessPing = 0;
	}
	
	public HashMap<String, String> getIPList(){
		return IPList;
	}

	public boolean isWifiDongleConnected() {
		String[] cmd = {"lsusb"};
		ShellCmd sCmd = new ShellCmd("lsusb", cmd);
		synchronized (sCmd) {
			FifoSenderShell.put(sCmd);
			
			try {
				sCmd.wait(2000);
			} catch (InterruptedException e) {}
		}
		
		ShellResult sr = sCmd.getResult();
		if( sr != null ){
			String result = sr.getResult().toLowerCase();
			if( result.contains("wifi") || result.contains("netgear") )
				setWifiDongleConnected(true);
			else
				setWifiDongleConnected(false);
		}
		
		return wifiDongleConnected;
	}

	protected void setWifiDongleConnected(boolean wifiDongleConnected) {
		NetworkManager.wifiDongleConnected = wifiDongleConnected;
	}
	
	public HashMap<String, String> getWifiInfos(){
		HashMap<String, String> wifiInfos = new HashMap<String, String>();
		
		String signal = "N/A";
		String quality = "N/A";
		String noise = "N/A";
		
		String[] cmd = {"iwconfig", "wlan0"};
		ShellCmd sCmd = new ShellCmd("Signal WIFI", cmd);
		synchronized (sCmd) {
			FifoSenderShell.put(sCmd);
			
			try {
				sCmd.wait(2000);
			} catch (InterruptedException e) {}
		}
		
		ShellResult sr = sCmd.getResult();
		if( sr != null ){
			String result = sr.getResult().toLowerCase();
			
			String delim = "signal level=";
			int pos0 = result.indexOf(delim);
			if( pos0 != -1 ){
				int pos1 = result.indexOf("noise", pos0);
				if( pos1 != -1 ){
					signal = result.substring(pos0+delim.length(), pos1-1).trim();
				}
			}
			
			delim = "link quality=";
			pos0 = result.indexOf(delim);
			if( pos0 != -1 ){
				int pos1 = result.indexOf("signal", pos0);
				if( pos1 != -1 ){
					quality = result.substring(pos0+delim.length(), pos1-1).trim();					
				}
			}
			
			delim = "noise level=";
			pos0 = result.indexOf(delim);
			if( pos0 != -1 ){
				int pos1 = result.indexOf("\n", pos0+delim.length());
				if( pos1 != -1 ){
					noise = result.substring( pos0+delim.length(), pos1 ).trim();		
				}
			}
		}

		wifiInfos.put("quality", quality);
		wifiInfos.put("noise", noise);
		wifiInfos.put("signal", signal);
		
//		System.out.println( "Signal = "+signal );
//		System.out.println( "noise = "+noise );
//		System.out.println( "quality = "+quality );
		
		
		
		return wifiInfos;
	}
}
