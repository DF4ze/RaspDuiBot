package controleurs;

import java.util.Scanner;

import modeles.dao.communication.beansactions.SpeakAction;
import controleurs.audio.Audio;
import controleurs.serial.SerialMgr;

public class ShutDownTreatement extends Thread {

	public ShutDownTreatement() {
		// TODO Auto-generated constructor stub
	}

	public void run(){
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Faites 'q' puis 'entree' pour quitter...");
		while( scanner.hasNext() ){
			try{
				String in = scanner.next();
				
				if( in.equalsIgnoreCase("q") ){
					System.out.println("Etes-vous sur de vouloir fermer le serveur? (o/n)");
					scanner.hasNext();
					if( scanner.next().equalsIgnoreCase("o") )
						break;
					
				}else if( in.equalsIgnoreCase("+") ){
					Audio.volumeUp(10);
				}else if( in.equalsIgnoreCase("-") ){
					Audio.volumeDown(10);
				}else if( in.equalsIgnoreCase("m") ){
					Audio.volumeMute();
				}else if( in.equalsIgnoreCase("t") ){
					Audio.play(Audio.SOUND_LAZER);
					SpeakAction sa = new SpeakAction("Un petit test de vocalise.");
					Audio.speak(sa);
					
				}
			}catch(Exception e){}
		}
		
		shutdown();

	}
	
	public void shutdown(){
		SpeakAction sa = new SpeakAction("L'application va s'arraiter, a bientot!");
		Audio.speak(sa);
		try {
			ShutDownTreatement.sleep(3000l);
		} catch (InterruptedException e) {}
		
		Audio.play(Audio.SOUND_STOP);
		SerialMgr.close();
		
		try {
			ShutDownTreatement.sleep(1000l);
		} catch (InterruptedException e) {}

		
		Runtime.getRuntime().exit(0);		
	}
}
