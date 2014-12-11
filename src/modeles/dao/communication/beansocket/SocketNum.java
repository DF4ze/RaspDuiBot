package modeles.dao.communication.beansocket;

import java.io.IOException;
import java.net.Socket;

public class SocketNum {
	
	private Integer number = null;
	private Socket socket = null;

	public SocketNum() {
	}

	public SocketNum(Socket accept) throws IOException {
		socket = accept;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
