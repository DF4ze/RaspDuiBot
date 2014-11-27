package modeles;

import java.io.IOException;
import java.net.ServerSocket;
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
	
	
	private Integer iPort 			= DEFAUT_PORT;
	private Integer iMaxConnexion 	= DEFAUT_MAXCON;
	private boolean bRunning 		= DEFAUT_STATE;
	private Semaphore semaphore;			
	


	public ServeurModele(Integer iPort, Integer iMaxConnexion, boolean isRunning) {
		setiPort(iPort);
		setiMaxConnexion(iMaxConnexion);
		setRunning(isRunning);
		 
		setSemaphore(new Semaphore( getiMaxConnexion() ));
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



	
}
