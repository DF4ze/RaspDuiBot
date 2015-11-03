package controleurs;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beanfifo.FifoSenderShell;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansactions.SpeakAction;
import modeles.dao.shell.ShellPattern;
import controleurs.audio.Audio;
import controleurs.network.NetworkManager;
import controleurs.serial.SerialMgr;
import controleurs.socket.SocketMgr;

public class CtrlGeneral implements Observer{
	
	@SuppressWarnings("unused")
	private SocketMgr srv;
	private ServeurModele mod;
	private SerialMgr serial;
	private static Timer timerVeille;
	private static verifVeille veille;
	private NetworkManager networkManager;
	private ShutDownTreatement sdt;

	public CtrlGeneral( int iPort, int iMaxCon, boolean isRunning, String sSerialPort, int iSerialSpeed, int iSerialTimeOut, boolean bNoSerial, boolean asService ) {
		Audio.play(Audio.SOUND_START);
		
		if( iPort == -1 ) 
			iPort = ServeurModele.DEFAUT_PORT;

		if( iMaxCon == -1 )
			iMaxCon = ServeurModele.DEFAUT_MAXCON;
		
		mod = new ServeurModele(iPort, iMaxCon, isRunning, sSerialPort, iSerialSpeed, iSerialTimeOut, bNoSerial);
		new SocketMgr(mod);
		mod.addObserver(this);
		
		if( !mod.isbNoSerial() ){
			serial = new SerialMgr( mod );
			try {
				serial.connect();
			} catch (PortInUseException e) {
				System.err.println("Port serie deja utilise");
				//e.printStackTrace();
			} catch (UnsupportedCommOperationException e) {
				System.err.println("Port serie : Operation non supportee");
				
			} catch (IOException e) {
				System.err.println("Port serie : Erreur sur le stream");
				
			} catch (NoSuchPortException e) {
				System.err.println("Port serie n'existe pas");
				
			}
		}
		
		CtrlRecep cr = new CtrlRecep( mod );
		CtrlSend cs = new CtrlSend( mod );
		
		cr.start();
		cs.start();
		
		// init des pins
		ExtraAction ea = new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.Restart);
		FifoSenderShell.put(ShellPattern.actionToShell(ea));
		
		ea = new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.On);
		FifoSenderShell.put(ShellPattern.actionToShell(ea));
		

		// envoi d'un son de bienvenue
		SpeakAction sa = new SpeakAction("Bonjour! Je m'appelle Arduibote.");
		//FifoSenderShell.put( ShellPattern.actionToShell(sa) );
		Audio.speak(sa);
		
		//Runtime.getRuntime().addShutdownHook(new ShutDownTreatement());
		sdt = new ShutDownTreatement();
		if( !asService )
			sdt.start();
		
		// On lance le v�rificateur de r�seau
		networkManager = NetworkManager.launch( mod );
	}
	
	public void shutdown(){
		sdt.shutdown();
	}
	
	public static class verifVeille extends TimerTask {

		@Override
		public void run() {
			if( Verbose.isEnable() )
				System.out.println( "Timer mise en veille, nb commandes : "+ ServeurModele.getCmdReceptLastMinute());
			
			if( ServeurModele.getCmdReceptLastMinute() == 0 ){
				FifoReceiverSocket.put(new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.On));
			}
			ServeurModele.setCmdReceptLastMinute(0);
		}
		
		public static verifVeille getNewInstance(){
			return new verifVeille();
		}
	}
	
	public static void startTimerVeille(){
		if( Verbose.isEnable() )
			System.out.println("Timer Enable");
		timerVeille = new Timer();
		veille = new verifVeille();
		timerVeille.scheduleAtFixedRate(veille, 0, 60000);
	}

	public static void stopTimerVeille(){
		if( Verbose.isEnable() )
			System.out.println("Timer Disable");
		
		try{
			timerVeille.cancel();
		}catch( IllegalStateException e){}
		catch( NullPointerException e ){}
	}

	@Override
	public void update(Observable o, Object arg) {

		if( arg.equals("NOMORECLIENT") ){
			ExtraAction ea = new ExtraAction( IAction.typeAlim, IAction.alimStandBy, IAction.On );
			FifoReceiverSocket.put(ea);
			
		}else if( arg.equals("PING") ){
			networkManager.pingReceived( mod.getLastPing() );
		}
		
	}

}
