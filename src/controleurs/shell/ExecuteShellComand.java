package controleurs.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import modeles.Verbose;
import modeles.dao.communication.beanshell.ShellCmd;
import modeles.dao.communication.beanshell.ShellResult;

public class ExecuteShellComand implements Runnable{
 
	private String[] command = {};
	private StringBuffer output = new StringBuffer("");
	private boolean error = false;
	private boolean commandEnded = false;
	private String name = "";
	
	public static void main(String[] args) {
		
		String [] command = new String[6];
		command[0] = "/usr/local/bin/mjpg_streamer";
		command[1] = "-i";
		command[2] = "/usr/local/lib/input_uvc.so -f 10r VGA";
		command[3] = "-o";
		command[4] = "/usr/local/lib/output_http.so -w /var/www/cam";
		command[5] = "&";
		
		ExecuteShellComand obj = new ExecuteShellComand();
		ShellResult sr = obj.executeCommand(command);
 
		System.out.println(sr.getResult());
 		
	}
	
	public ExecuteShellComand(){
		
	}
 
	public ExecuteShellComand( ShellCmd shCmd ){
		command = shCmd.getCommand();
		setName(shCmd.getName());
	}
	
	public ShellResult executeCommand(String command[]) {
  
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader readerr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			p.waitFor();
 
            String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			line = "";			
			while ((line = readerr.readLine())!= null) {
				output.append(line + "\n");
			}
 
			reader.close();
			
		} catch (Exception e) {
			error = true;
			
			if( Verbose.isEnable() )
				System.err.println("erreur Shell : "+e.getMessage());
		}finally{
			commandEnded = true;
			//notifyAll();
		}
 
		return new ShellResult( getName(), getCommand(), getOutput(), error);
 
	}
	public ShellResult executeCommand() {
		ShellResult sr = null;
		if( command.length != 0 )
			sr = executeCommand(command);
		return sr;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
}