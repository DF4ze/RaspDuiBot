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
import modeles.dao.communication.beanfifo.FifoSenderSocket;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beansinfos.PingInfo;
import controleurs.audio.Audio;

public class NetworkManager extends TimerTask{
	
	private HashMap<String, String> IPList = null;
	private static Timer timer;
	private int count = 0;
	private int unSuccessConnection = 0;
	private int unSuccessPing = 0;
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
				Thread.sleep(5000);
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
		boolean changed = false;
		HashMap<String, String> newIPList = retreiveIps();
		if( IPList != null )
			if( newIPList.size() != IPList.size() )
				changed = true;
		IPList = newIPList;
		
		if( connected && IPList.size() == 0){
			// on vient de perdre le réseau
			connected = false;
			// tenter de récupérer le réseau
			count = 2;
			
			//...
			String texte = "Je n'ai plus de raiseau";
			SpeakAction sa = new SpeakAction(texte);
			Audio.speak(sa);
		}
		
		
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
				String textForInterface = ip.getKey().equals("wlan0")?"le WIFI":(ip.getKey().equals("eth0")?"éternet":ip.getKey());
				String textForIP = ip.getValue().replace(".", ". ");
				
				texte += textForIP+" sur "+textForInterface;
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
		
		if( !connected && count == 0 ){
			String texte = "Je n'ai pas encore de réseau, merci de patienter.";
			SpeakAction sa = new SpeakAction(texte);
			Audio.speak(sa);
		
		}else if( !connected && count == 20 ){
			String texte;
			if( unSuccessConnection == 0 ){
				texte = "Je n'ai toujours pas de réseau, je vais voir si je peux faire quelque chose.";
				SpeakAction sa = new SpeakAction(texte);
				Audio.speak(sa);
			}else if( unSuccessConnection == 1 ){
				texte = "Je suis désolée, je n'ai pas pu établir de connexion réseau";
				SpeakAction sa = new SpeakAction(texte);
				Audio.speak(sa);
			}
			

			
			count = 1;
			unSuccessConnection++;
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
	 
	         while (interfaces.hasMoreElements()) {  // carte reseau trouvee
	            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
	            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
	            
	            while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
	                InetAddress ia = ienum.nextElement();
	                String adress = ia.getHostAddress().toString();
	                
	                if( adress.startsWith("192") || adress.startsWith("10") ){
	                	IPList.put( interfaceN.getDisplayName() , adress);     
	                }
	            }
	        }
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
	
}
