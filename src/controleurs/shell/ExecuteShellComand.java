package controleurs.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellComand implements Runnable{
 
	private String[] command = {};
	private StringBuffer output = new StringBuffer("");
	private boolean error = false;
	private boolean commandEnded = false;
	
	public static void main(String[] args) {
		
		String [] command = new String[6];
		command[0] = "/usr/local/bin/mjpg_streamer";
		command[1] = "-i";
		command[2] = "/usr/local/lib/input_uvc.so -f 10r VGA";
		command[3] = "-o";
		command[4] = "/usr/local/lib/output_http.so -w /var/www/cam";
		command[5] = "&";
		
		ExecuteShellComand obj = new ExecuteShellComand();
		String output = obj.executeCommand(command);
 
		System.out.println(output);
 		
	}
	
	public ExecuteShellComand(){
		
	}
 
	public ExecuteShellComand( String[] cmd ){
		command = cmd;
	}
	
	public String executeCommand(String command[]) {
  
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
 
            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
			reader.close();
			
		} catch (Exception e) {
			error = true;
		}finally{
			commandEnded = true;
			//notifyAll();
		}
 
		return output.toString();
 
	}
	public String executeCommand() {
		if( command.length != 0 )
			executeCommand(command);
		return output.toString();
	}	 
	@Override
	public void run() {
		executeCommand( command ) ;
	}

	public String[] getCommand() {
		return command;
	}
	public void setCommand(String[] command) {
		this.command = command;
	}

	public String getOutput() {
		return output.toString();
	}
	protected void setOutput(StringBuffer output) {
		this.output = output;
	}

	public boolean isCommandEnded() {
		return commandEnded;
	}
	protected void setCommandEnded(boolean commandEnded) {
		this.commandEnded = commandEnded;
	}

	public boolean isError() {
		return error;
	}

	protected void setError(boolean error) {
		this.error = error;
	}
 
}