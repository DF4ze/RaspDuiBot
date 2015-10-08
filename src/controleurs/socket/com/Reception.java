package controleurs.socket.com;

import java.io.IOException;
import java.io.ObjectInputStream;

import modeles.ServeurModele;
import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansocket.SocketNum;

public class Reception implements Runnable {


	private ObjectInputStream inObject;
	private ServeurModele mod;
	private SocketNum mySocket;
	
	public Reception(ServeurModele m, ObjectInputStream in, SocketNum s){
		mod = m;
		inObject = in;
		mySocket = s;
	}
	
	public void run() {
		
		while(true){
	        try {
	        			
		        Object Obj = inObject.readObject();
		        
		        if( Obj instanceof IAction ){
		        	IAction action = (IAction)Obj;
		        	
		        	if( Verbose.isEnable() )
		        		System.out.println("Socket "+mySocket.getNumber()+" : "+ action);
		        	
		        	FifoReceiverSocket.put(action);
		        	
		        }else
		        	System.out.println("Socket "+mySocket.getNumber()+" : "+"ORNI : Object Received None Identified ! ");
	        
		    } catch (IOException e) {
		    	if( mySocket != null ){
		    		System.out.println("Client "+mySocket.getNumber()+" disconnected");
		    		
					try {
						mod.releaseConnexion(mySocket.getSocket());
					} catch (InterruptedException e1) {}
					
					try {
						mySocket.getSocket().close();
					} catch (IOException e1) {}
					
					if( mod.getNbConnected() == 0 ){
						ExtraAction ea = new ExtraAction( IAction.typeAlim, IAction.alimStandBy, IAction.On );
						FifoReceiverSocket.put(ea);
					}
						
					
					break;
		    	}

				break;
			} catch (ClassNotFoundException e) {
				if( Verbose.isEnable() )
					System.out.println("Erreur de lecture du socket : "+e.getMessage());
			}
		}
	}

}