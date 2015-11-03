package principal;

import modeles.ServeurModele;
import modeles.Verbose;
import controleurs.CtrlGeneral;

public class Lanceur {
	
	private static int iPort = -1;
	private static int iMaxCon = -1;
	private static boolean isRunning = ServeurModele.DEFAUT_STATE;
	private static String sSerialPort = ServeurModele.DEFAUT_SERIAL;
	private static int iSerialSpeed = ServeurModele.DEFAUT_SPEED;
	private static int iSerialTimeout = ServeurModele.DEFAUT_TIMEOUT;
	private static boolean bNoSerial = false;
	private static boolean  bOk = true;
	private static boolean  bAsService = false;
	
	private static CtrlGeneral ctrlGeneral;

	/*
	 * Pour jsvc : init() start(); stop(); destroy();
	 * 
	 */
	public static void init(String[] args){
		// Récupération des parametres
		for( int i=0; i < args.length; i++ ){
			// si detection du parametre de PORT
			if( args[i].equals("-p") || args[i].equals("-port") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					int erreur[] = {-1};
					// le parametre est-il valide?
					if( ServeurModele.isValidPort(args[i+1], erreur)){
						iPort = Integer.valueOf(args[i+1]);
						i++;
					}else{
						System.out.println(ServeurModele.erreurToMessage(erreur[0]));
						bOk = false;
					}
						
				}else
					bOk = false;
				
			// Si parametre de verbosité detecté
			}else if( args[i].equals("-v") || args[i].equals("-verbose") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					if( args[i+1].equals("0") || args[i+1].equals("false")){
						Verbose.setEnable(false);
						i++;
					}else if( args[i+1].equals("1") || args[i+1].equals("true")){
						Verbose.setEnable(true);
						i++;
					}else
						bOk = false;
				}else
					bOk = false;
			
				
			// Si parametre de max connexion
			}else if( args[i].equals("-m") || args[i].equals("-maxcon") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					// le parametre est-il valide?
					int erreur[]={0};
					if( ServeurModele.isValidMaxCon(args[i+1], erreur) ){
						iMaxCon = Integer.valueOf(args[i+1]);
						i++;
					}else{
						System.out.println(ServeurModele.erreurToMessage(erreur[0]));
						bOk = false;
					}
					
				}else{
					bOk = false;
				}
				
			// Si parametre de Lancement
			}else if( args[i].equals("-r") || args[i].equals("-run") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					if( args[i+1].equals("0") || args[i+1].equals("false")){
						isRunning = false;
						i++;
					}else if( args[i+1].equals("1") || args[i+1].equals("true")){
						isRunning = true;
						i++;
					}else{
						bOk = false;
					}
					
				}else{
					bOk = false;
				}
			
			// Serial Port
			}else if( args[i].equals("-sp") || args[i].equals("-serialport") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					sSerialPort = args[i+1];
					i++;
				}else{
					bOk = false;
				}
			
			// Serial Speed
			}else if( args[i].equals("-ss") || args[i].equals("-serialspeed") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					try{
						iSerialSpeed = Integer.valueOf(args[i+1]);
						i++;
					}catch( Exception e ){
						bOk = false;
					}
				}else{
					bOk = false;
				}
				
			// Serial TimeOut
			}else if( args[i].equals("-t") || args[i].equals("-serialtimeout") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					try{
						iSerialTimeout = Integer.valueOf(args[i+1]);
						i++;
					}catch( Exception e ){
						bOk = false;
					}
				}else{
					bOk = false;
				}
				
			// mode non serial
			}else if( args[i].equals("-ns") || args[i].equals("-noserial") ){
				bNoSerial = true;
			}
			// run as a service
			else if( args[i].equals("-s") || args[i].equals("-service") ){
				bAsService = true;
			}
			
			// si demande de l'aide ou s'il y a eu une erreur
			if( args[i].equals("-h") || args[i].equals("-help") || !bOk ){
				System.out.println( getHelpMsg( args ) );
				bOk = false;
			}
			
			if( !bOk )
				break;
		}
	}
	
	public static void start(){
		
		// Lancement de l'application
		if( bOk ){
			ctrlGeneral = new CtrlGeneral( iPort, iMaxCon, isRunning, sSerialPort, iSerialSpeed, iSerialTimeout, bNoSerial, bAsService );
		}
		
	}
	
	public static void stop(){
		ctrlGeneral.shutdown();
	}
	
	public static void destroy(){
		
	}
	/*
	 * 
	 */
	


	
	public static String getHelpMsg(String[] args){
		return "use :  [-p/-port PORTNUMBER] [-h/-help] [-v/-verbose 0/1]\n\n"+
				"-p/-port PORTNUMBER : doit etre un entier entre "+ServeurModele.PLAGE_MIN +" et " +ServeurModele.PLAGE_MAX+"\n"+
				"                      port "+ ServeurModele.DEFAUT_PORT+ " par defaut\n"+
				"-m/-maxcon NBCONNEXION : doit etre un entier positif. Par defaut : "+ServeurModele.DEFAUT_MAXCON+"\n"+
				"-r/-run TRUE/FALSE: soit 1/true pour forcer le demarrage du serveur ou 0/false pour le desactiver. Par defaut : "+ServeurModele.DEFAUT_STATE+"\n"+
				"-v/-verbose TRUE/FALSE: soit 1/true pour forcer l'activation ou 0/false pour desactiver la verbosite. "+Verbose.isEnable()+" par defaut\n"+
				"-sp/-serialport CHEMIN : chemin d'accès au port pour la communication serie. Par defaut : "+ServeurModele.DEFAUT_SERIAL+"\n"+
				"-ss/-serialspeed VITESSE : vitesse de communication du port serie. Par defaut : "+ServeurModele.DEFAUT_SPEED+"\n"+
				"-t/-serialtimeout MILLIS : timeout de connexion au port serie (en ms). Par defaut : "+ServeurModele.DEFAUT_TIMEOUT+"\n"+
				"-ns/-noserial TRUE/FALSE: desactive le port serie. Par defaut : FALSE"+"\n";
	}
	
	
	public static void main(String[] args) {
		
		init(args);
		
		start();
	}
}
