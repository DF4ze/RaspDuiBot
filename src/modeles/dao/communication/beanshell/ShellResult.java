package modeles.dao.communication.beanshell;

public class ShellResult {

	private String name;
	private String[] command;
	private String result;
	
	public ShellResult() {
	}

	public ShellResult( String name, String[] command, String result) {
		setName(name);
		setCommand(command);
		setResult(result);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getCommand() {
		return command;
	}

	public void setCommand(String[] command) {
		this.command = command;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
