package controleurs.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.communication.beansactions.VolumeAction;
import modeles.dao.shell.ShellPattern;

public class Audio extends Thread {

	private static final String SOUNDS_PATH = "/root/scripts/sounds/";
/*	static{
		// ici il faut récupérer le volume de départ...
		
		// et l'emplacement du JAR
		String path = Audio.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			SOUNDS_PATH = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			if( Verbose.isEnable() )
				System.out.println("Audio error : "+e.getMessage());
		}
	}	
*/	//private static final String SOUNDS_PATH = System.getProperty("user.dir" )+"sounds/";
	
	public static final String SOUND_START 				= SOUNDS_PATH+	"sessionStart.wav";
	public static final String SOUND_STOP 				= SOUNDS_PATH+	"sessionEnd.wav";
	public static final String SOUND_CLIENT_CONNECT 	= SOUNDS_PATH+	"connect.wav";
	public static final String SOUND_CLIENT_DISCONNECT 	= SOUNDS_PATH+	"disconnect.wav";
	public static final String SOUND_STATUS_CHANGE 		= SOUNDS_PATH+	"online.wav";
	

	public static final String SOUND_COEUR 				= SOUNDS_PATH+	"coeur.wav";
	public static final String SOUND_MOTO 				= SOUNDS_PATH+	"moto.wav";
	public static final String SOUND_SIRENE 			= SOUNDS_PATH+	"sirene.wav";
	public static final String SOUND_LAZER 				= SOUNDS_PATH+	"blaster.wav";

	
	private AudioInputStream audioInputStream = null;
	private SourceDataLine line;
	private String chemin;
	private static Speak sp;


	
	private Audio(String chemin) {
		this.chemin = chemin;
	}

	public void run() {
		File fichier = new File(chemin);
		try {
			@SuppressWarnings("unused")
			AudioFileFormat format = AudioSystem.getAudioFileFormat(fichier);

			audioInputStream = AudioSystem.getAudioInputStream(fichier);

			AudioFormat audioFormat = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);

			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();
			// Fenetre.begin=true;
		
			byte bytes[] = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1) {
				line.write(bytes, 0, bytesRead);
			}
		} catch (UnsupportedAudioFileException e) {
			if( Verbose.isEnable() )
				System.out.println("Audio error, UnsupportedAudioFileException : "+e.getMessage());
			return;
		} catch (IOException e) {
			if( Verbose.isEnable() )
				System.out.println("Audio error, IOException : "+e.getMessage());
			return;
		} catch (LineUnavailableException e) {
			if( Verbose.isEnable() )
				System.out.println("Audio error, LineUnavailableException : "+e.getMessage());
		}
	}

	public static void play( String sound) {
		Audio son = new Audio( sound );
		son.start();

	}

	public static void volumeUp( int pourcent ) {
		ServeurModele sm = ServeurModele.getInstance();
		VolumeAction va = null;
		if( sm.isVolumeMute() ){
			sm.setVolumeMute(false);
			sm.setVolume(pourcent);
			va = new VolumeAction(true, true, pourcent);
		
		}else{
			sm.setVolume( sm.getVolume() + pourcent );
			va = new VolumeAction(true, pourcent);
		}
		FifoSenderShell.put( ShellPattern.actionToShell(va) );
	}

	public static void volumeDown(int pourcent) {		
		ServeurModele sm = ServeurModele.getInstance();
		if( !sm.isVolumeMute() ){
			sm.setVolume( sm.getVolume() - pourcent );
			VolumeAction va = new VolumeAction(false, pourcent);
			FifoSenderShell.put( ShellPattern.actionToShell(va) );
		}
	}

	public static void volumeMute() {	
		ServeurModele sm = ServeurModele.getInstance();
		VolumeAction va = null;
		if( sm.isVolumeMute() ){
			sm.setVolumeMute(false);
			va = new VolumeAction(true, true);
		
		}else{
			sm.setVolumeMute(true);
			va = new VolumeAction(false, true, sm.getVolume());		
		}		
		FifoSenderShell.put( ShellPattern.actionToShell(va) );
	}
	
	public static void speak( SpeakAction sa ){
		if( sp != null && !sp.isFinished()){
			synchronized (sp) {
				try {
					sp.wait(20000);
				} catch (InterruptedException e) {}
				
			}
		}
		sp = new Speak(sa);
		sp.start();
	}

}