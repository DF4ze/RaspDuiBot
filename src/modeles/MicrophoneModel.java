package modeles;

import javax.sound.sampled.AudioFormat;

import modeles.dao.communication.beanfifo.FifoMicro;

public class MicrophoneModel {
	
	private static boolean clientConnected = false;
	public static final int CHUNK_SIZE = 1024;
	public static final int BUFF_SIZE = CHUNK_SIZE*10;
	public static final AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, true);
	public static final String IP = "192.168.1.35";
	public static final int PORT = 2015;
	public static final int MIN_BUFFERED = 0;

	public MicrophoneModel() {
		// TODO Auto-generated constructor stub
	}

	public static boolean isClientConnected() {
		return clientConnected;
	}

	public static void setClientConnected(boolean clientConnected) {
		MicrophoneModel.clientConnected = clientConnected;
		
		if( !clientConnected )
			FifoMicro.getList().clear();
	}

}
