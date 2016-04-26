package controleurs.socket.comMicro;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import modeles.MicrophoneModel;
import controleurs.audio.micro.MicroRetreiver;

public class AccepterMicro extends Thread{

	ServerSocket srvSocket;
	
	public AccepterMicro( ServerSocket srvSocket ) {
		this.srvSocket = srvSocket;
	}
	
	public void run(){
		while( true ){
			try {
				System.out.println("Attente de connexion microphone...");
				Socket socket = srvSocket.accept();
				
				if( !MicrophoneModel.isClientConnected() ){
					ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
					
					
					Thread t1 = new Thread(new AudioSender(outObject));
					t1.start();
					
					MicrophoneModel.setClientConnected(true);
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					
					Thread t2 = new Thread(new MicroRetreiver()); // il faut faire un singleton...!
					t2.start();
					System.out.println("Client Connecte microphone");
					
				}else
					System.out.println("Tentative de connexion d'un clients alors que le stream microphone est occupe");

			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Client deconnecte microphone");
				MicrophoneModel.setClientConnected(false);
			}
		}
	}

}
