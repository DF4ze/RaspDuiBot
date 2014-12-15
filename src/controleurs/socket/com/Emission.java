package controleurs.socket.com;

import java.io.IOException;
import java.io.ObjectOutputStream;

import modeles.dao.communication.beanfifo.FifoSenderSocket;

public class Emission implements Runnable {

	private ObjectOutputStream out;
//	private String message = null;

	// private Scanner sc = null;

	public Emission(ObjectOutputStream out) {
		this.out = out;
	}

	public void run() {

		// sc = new Scanner(System.in);

		while (true) {
			synchronized (FifoSenderSocket.getInstance()) {
				try {
					FifoSenderSocket.getInstance().wait();

					try {
						while (FifoSenderSocket.getInstance().size() != 0) {
							out.writeObject(FifoSenderSocket.get());
						}

						out.flush();
					} catch (IOException e) {
						System.err.println("Erreur d'ecriture dans le Socket");
					}
				} catch (InterruptedException e) {
					break;
				}

			}
		}
	}
}