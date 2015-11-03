package controleurs.socket.comMicro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import modeles.dao.communication.beanfifo.FifoMicro;
import modeles.MicrophoneModel;

public class AudioSender extends Thread{

	private ObjectOutputStream objectOut;
	
	public AudioSender(ObjectOutputStream oos) {
		objectOut = oos;
	}
	
	public void run(){
		
		try {
			while( true ){
				
				synchronized (FifoMicro.getList()) {
					try {
						FifoMicro.getList().wait();
					}catch( Exception e ){
						e.printStackTrace();
						break;
					}
				}
				
				while( FifoMicro.getList().size() != 0 ){
					//System.out.println(FifoMicro.getList().size());
					ByteArrayOutputStream baos = FifoMicro.pop();

					objectOut.write(baos.toByteArray());
					//objectOut.write(CompressionUtils.compress(baos.toByteArray()));
					//System.out.print(",");
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		System.out.println("Client deconnecte microphone");
		MicrophoneModel.setClientConnected(false);
	}

}
