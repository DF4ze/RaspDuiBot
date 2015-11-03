package controleurs.audio.micro;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import modeles.MicrophoneModel;
import modeles.dao.communication.beanfifo.FifoMicro;

public class MicroRetreiver extends Thread{

	public MicroRetreiver() {
		// il faut faire un singleton...!
	}
	
	public void run(){
        
        TargetDataLine microphone;
//        AudioInputStream audioInputStream;
//        SourceDataLine sourceDataLine;
        try {
            microphone = AudioSystem.getTargetDataLine(MicrophoneModel.audioFormat);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, MicrophoneModel.audioFormat);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(MicrophoneModel.audioFormat);

            microphone.start();

            System.out.println("Debut de la diffusion");
            try {
                while (MicrophoneModel.isClientConnected()) { 
                	int totalByteRead = 0;
                    //byte[] data = new byte[microphone.getBufferSize() / 5];
                	byte[] data = new byte[MicrophoneModel.CHUNK_SIZE];
                	ByteArrayOutputStream out = new ByteArrayOutputStream();
                	
                	//long t0 = new Date().getTime();
                	while( totalByteRead < MicrophoneModel.BUFF_SIZE ){ 
	                    int numBytesRead = microphone.read(data, 0, MicrophoneModel.CHUNK_SIZE);
	                    totalByteRead += numBytesRead;
	                    out.write(data, 0, numBytesRead);
                	}
                	//long t1 = new Date().getTime();
                	//System.out.print(".");
                    FifoMicro.add(out);
                 
                    //System.out.println("delay : "+(t1-t0));
                }
                System.out.println("\narret de la diffusion");
            } catch (Exception e) {
            	System.err.println("Error reading microphone : "+e.getMessage());
                //e.printStackTrace();
            }
            microphone.close();
            
        }catch( Exception e ){
        	e.printStackTrace();
        }
	}

}
