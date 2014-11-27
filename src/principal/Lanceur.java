package principal;

import controleurs.Controleur;
import modeles.ServeurModele;
import modeles.Verbose;

public class Lanceur {

	public static void main(String[] args) {
		int iPort = -1;
		int iMaxCon = -1;
		boolean isRunning = ServeurModele.DEFAUT_STATE;
		
		boolean bOk = true;
		
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
						break;
					}
						
				}else
					bOk = false;
				
			// Si parametre de verbosité detecté
			}else if( args[i].equals("-v") || args[i].equals("-verbose") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					if( args[i+1].equals("0") || args[i+1].equals("false")){
						Verbose.setEnable(false);
					}else if( args[i+1].equals("1") || args[i+1].equals("true")){
						Verbose.setEnable(true);
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
						break;
					}
					
				}else
					bOk = false;
				
			// Si parametre de Lancement
			}else if( args[i].equals("-r") || args[i].equals("-run") ){
				// le parametre est-il présent?
				if( i+1 < args.length ){
					if( args[i+1].equals("0") || args[i+1].equals("false")){
						isRunning = false;
					}else if( args[i+1].equals("1") || args[i+1].equals("true")){
						isRunning = true;
					}else
						bOk = false;
					
				}else
					bOk = false;
			}
			
			// si demande de l'aide ou s'il y a eu une erreur
			if( args[i].equals("-h") || args[i].equals("-help") || !bOk ){
				System.out.println( getHelpMsg( args ) );
			}
		}
		
		
		
		// Lancement de l'application
		if( bOk ){
			new Controleur( iPort, iMaxCon, isRunning );
		}
	}
	
	public static String getHelpMsg(String[] args){
		return "use : "+args[0]+" [-p/-port PORTNUMBER] [-h/-help] [-v/-verbose 0/1]\n\n"+
				"-p/-port PORTNUMBER : doit etre un entier entre "+ServeurModele.PLAGE_MIN +" et " +ServeurModele.PLAGE_MAX+"\n"+
				"                      port "+ ServeurModele.DEFAUT_PORT+ " par defaut\n"+
				"-m/-maxcon NBCONNEXION : doit etre un entier positif. Par defaut : "+ServeurModele.DEFAUT_MAXCON+"\n"+
				"-r/-run : soit 1/true pour forcer le demarrage du serveur ou 0/false pour le desactiver. Par defaut : "+ServeurModele.DEFAUT_STATE+"\n"+
				"-v/-verbose : soit 1/true pour forcer l'activation ou 0/false pour desactiver la verbosite. "+Verbose.isEnable()+" par defaut\n";
	}

}
