package controleurs.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExecuteShellComand {
 
	public static void main(String[] args) {
 
//		ExecuteShellComand obj = new ExecuteShellComand();
 
		String domainName = "google.com";
 
		//in mac oxs
		String command = "ping -c 1 " + domainName;
 
		//in windows
		//String command = "ping -n 3 " + domainName;
 
		String output = ExecuteShellComand.executeCommand(command);
//		String output = obj.executeCommand(command);
 
		System.out.println(output);
 
	}
 
	public static String executeCommand(String command) {
 
		StringBuffer output = new StringBuffer();
 
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
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
 
}