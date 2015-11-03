package controleurs.socket.comCommandes;

import java.io.IOException;
import java.io.ObjectInputStream;

import modeles.Verbose;
import modeles.dao.communication.beanfifo.FifoReceiverSocket;
import modeles.dao.communication.beansactions.IAction;
import modeles.dao.communication.beansocket.SocketNum;

public class ReceptionCommandes implements Runnable {


	private ObjectInputStream inObject;
	private SocketNum mySocket;
	
	public ReceptionCommandes( ObjectInputStream in, SocketNum s){
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
		        		System.out.println("Socket Commandes : "+ action);
		        	
		        	FifoReceiverSocket.put(action);
		        	
		        }else
		        	System.out.println("Socket Commandes"+mySocket.getNumber()+" : "+"ORNI : Object Received None Identified ! ");
	        
		    } catch (IOException e) {
		    	if( mySocket != null ){		    		
					
					break;
		    	}

				break;
			} catch (ClassNotFoundException e) {
				if( Verbose.isEnable() )
					System.out.println("Erreur de lecture du socket Commandes : "+e.getMessage());
				break;
			}
		}
	}

}