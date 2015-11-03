package principal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import controleurs.network.NetworkManager;
import modeles.dao.communication.beansactions.DirectionAction;
import modeles.dao.communication.beansactions.ExtraAction;
import modeles.dao.communication.beansactions.IAction;

public class Commandes {

	public Commandes() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		ArrayList<IAction> actions = new ArrayList<IAction>();

		for( String arg : args ){
			if( arg.equals("-avance") ){
				actions.add(new DirectionAction(150, 0));
			}
			else if( arg.equals("-stop") ){
				actions.add(new DirectionAction(0, 0));
			}
			else if( arg.equals("-recule") ){
				actions.add(new DirectionAction(-150, 0));
			}
			else if( arg.equals("-droite") ){
				actions.add(new DirectionAction(0, 100));
			}
			else if( arg.equals("-gauche") ){
				actions.add(new DirectionAction(0, -100));
			}
			else if( arg.equals("-lumiereOn") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Light, IAction.On));
			}
			else if( arg.equals("-lumiereOff") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Light, IAction.Off));
			}
			else if( arg.equals("-laserOn") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Lazer, IAction.On));
			}
			else if( arg.equals("-laserOff") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Lazer, IAction.Off));
			}
			else if( arg.equals("-strombOn") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Strobe, IAction.On));
			}
			else if( arg.equals("-strombOff") ){
				actions.add(new ExtraAction(IAction.typeLight, IAction.Strobe, IAction.Off));
			}
			else if( arg.equals("-standByOn") ){
				actions.add(new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.On));
			}
			else if( arg.equals("-standByOff") ){
				actions.add(new ExtraAction(IAction.typeAlim, IAction.alimStandBy, IAction.Off));
			}
			else if( arg.equals("-ip") ){
				HashMap<String, String> ips = NetworkManager.retreiveIps();
				String sIps = "";
				int count = 0;
				for( Entry<String, String> ip : ips.entrySet()){
					sIps += (ip.getValue()+" sur "+ip.getKey());
					count++;
					if( count != ips.size() )
						sIps += " et ";
				}
				sIps = sIps.replace(".", ". ");
				System.out.println(sIps);
			}
			else
				System.out.println("Commande non reconnue : "+arg);
		}
		
		try {
			Socket socket = new Socket( "127.0.0.1", 2010);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			
			for( IAction action : actions ){
				outStream.writeObject( action );
				outStream.flush();
				
				Thread.sleep(500);
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
