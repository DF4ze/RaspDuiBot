package modeles;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.concurrent.Semaphore;


public class ServeurModele extends Observable {

	public static final int ERREUR_PLAGE 	= 1;
	public static final int ERREUR_INT 		= 2;
	public static final int ERREUR_FREE 	= 3;
	public static final int ERREUR_NEG 		= 4;
	
	public static final int PLAGE_MIN 		= 1024;
	public static final int PLAGE_MAX 		= 65535;
	
	public static final int DEFAUT_PORT		= 2009;
	public static final int DEFAUT_MAXCON	= 1;
	public static final boolean DEFAUT_STATE= true;

	public static final String DEFAUT_SERIAL= "/dev/ttyAMA0";
	public static final int DEFAUT_SPEED	= 9600;
	public static final int DEFAUT_TIMEOUT	= 2000;
	
	public static final int DEFAULT_PINALIM	= 17;
	
	
	private Integer iPort 			= DEFAUT_PORT;
	private Integer iMaxConnexion 	= DEFAUT_MAXCON;
	private boolean bRunning 		= DEFAUT_STATE;
	private String sSerialPort 		= DEFAUT_SERIAL;
	private int iSerialSpeed 		= DEFAUT_SPEED;
	private int iSerialTimeOut 		= DEFAUT_TIMEOUT;
//	private int iPinAlim			= DEFAULT_PINALIM;
	
	private Semaphore semaphore;	
	private HashMap<Integer, Socket> clientsConnected = new HashMap<Integer, Socket>();
	private int numClient = 0;
	


	public ServeurModele(Integer iPort, Integer iMaxConnexion, boolean isRunning, String sSerialPort, int iSerialSpeed, int iSerialTimeOut) {
		setiPort(iPort);
		setiMaxConnexion(iMaxConnexion);
		setRunning(isRunning);
		setsSerialPort(sSerialPort);
		setiSerialSpeed(iSerialSpeed);
		setiSerialTimeOut(iSerialTimeOut);
		 
		setSemaphore(new Semaphore( getiMaxConnexion() ));
	}

	public int addClient( Socket client ){
		numClient++;
		clientsConnected.put(numClient, client);
		return numClient;
	}
	public boolean delClient( int key ){
		Object o = clientsConnected.remove(key);
		boolean bOk = true;
		if( o == null )
			bOk = false;
		
		if( clientsConnected.size() == 0 ){
			setChanged();
			notifyObservers("NOMORECLIENT");
		}
			
		return bOk;
	}
	public Integer delClient( Socket client ){
		Integer num = null;
		for( Entry<Integer, Socket> entry : clientsConnected.entrySet()){
			if( entry.getValue().equals(client) ){
				delClient(entry.getKey());
				num = entry.getKey();
				break;
			}
		}
		return num;
	}
	public void delAllClient(){
		clientsConnected.clear();
	}
	public int getNbConnected(){
		return clientsConnected.size();
	}
	
	public Integer getiPort() {
		return iPort;
	}
	public void setiPort(Integer iPort) {
		this.iPort = iPort;
		
		setChanged();
		notifyObservers("PORT");
	}

	public Integer getiMaxConnexion() {
		return iMaxConnexion;
	}
	public void setiMaxConnexion(Integer iMaxConnexion) {
		this.iMaxConnexion = iMaxConnexion;
		setChanged();
		notifyObservers("MAXCONNEXION");
	}
	
	public boolean isRunning() {
		return bRunning;
	}

	public void setRunning(boolean bRunning) {
		this.bRunning = bRunning;
		setChanged();
		notifyObservers("RUNNING");
	}

	protected Semaphore getSemaphore() {
		return semaphore;
	}
	protected void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public void acquireConnexion() throws InterruptedException{
		semaphore.acquire();
	}
	
	public void releaseConnexion() throws InterruptedException{
		semaphore.release();
	}
	
	public void releaseConnexion( Socket s ) throws InterruptedException{
		releaseConnexion();
		delClient(s);
	}
	
	

	public static boolean isValidPort( String sPort, int[] erreur ){
		boolean bOk = false;
		int iPort = -1;
		
		try{
			// on tente de le transformer en INT
			iPort = Integer.valueOf(sPort);
			
			// on vérifie la plage
			if( iPort > PLAGE_MIN && iPort < PLAGE_MAX ){
				// on tente une connexion
				ServerSocket sock = new ServerSocket( iPort );
				sock.close();
				bOk = true;
			}else
				erreur[0] = ERREUR_PLAGE;
		}catch( NumberFormatException e){
			erreur[0] = ERREUR_INT;
		} catch (IOException e) {
			erreur[0] = ERREUR_FREE;
		}
		
		return bOk;
	}
	public static boolean isValidPort( String sPort ){
		int erreur[] = {0};
		return isValidPort(sPort, erreur);
	}
	
	public static boolean isValidMaxCon( String sMaxCon, int[] erreur ){
		boolean bOk = false;
		
		try{
			int iMaxCon = Integer.valueOf(sMaxCon);
			
			if( iMaxCon >= 0  ){
				bOk = true;
			}else
				erreur[0] = ERREUR_NEG;
				
		}catch( NumberFormatException e ){
			erreur[0] = ERREUR_INT;
		}
		
		return bOk;
	}
	
	
	
	public static String erreurToMessage(int erreur ){
		String sMsg = "";
		
		switch( erreur ){
		case ERREUR_FREE :
			sMsg = "Le port demande n'est pas libre"+"\n";
			break;
		case ERREUR_INT :
			sMsg = "Le port demande n'est pas considere comme un chiffre"+"\n";
			break;
		case ERREUR_PLAGE :
			sMsg = "Le port demande n'est pas dans la plage "+ PLAGE_MIN +" > port >"+ PLAGE_MAX+"\n";
			break;
		case ERREUR_NEG :
			sMsg = "Le nombre de Connexion Max ne peut pas etre negatif"+"\n";
			break;
		default :
			sMsg = "Erreur non referencee"+"\n";
		}
		
		return sMsg;
	}
	public static String erreurToMessage(int[] erreur ){
		String sMsg = "";
		if( erreur.length > 0 ){
			for( int i=0; i< erreur.length; i++ )
				sMsg += erreurToMessage(erreur[i]);
		}
		return sMsg;
	}

	public String getsSerialPort() {
		return sSerialPort;
	}
	public void setsSerialPort(String sSerialPort) {
		this.sSerialPort = sSerialPort;
	}

	public int getiSerialSpeed() {
		return iSerialSpeed;
	}
	public void setiSerialSpeed(int iSerialSpeed) {
		this.iSerialSpeed = iSerialSpeed;
	}

	public int getiSerialTimeOut() {
		return iSerialTimeOut;
	}
	public void setiSerialTimeOut(int iSerialTimeOut) {
		this.iSerialTimeOut = iSerialTimeOut;
	}

//	public int getiPinAlim() {
//		return iPinAlim;
//	}
//	public void setiPinAlim(int iPinAlim) {
//		this.iPinAlim = iPinAlim;
//	}

}
