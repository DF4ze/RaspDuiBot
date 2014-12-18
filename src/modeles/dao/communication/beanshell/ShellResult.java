package modeles.dao.communication.beanshell;

public class ShellResult {

	private String name;
	private String[] command;
	private String result;
	private boolean error;
	
	public ShellResult() {
	}

	public ShellResult( String name, String[] command, String result, boolean error) {
		setName(name);
		setCommand(command);
		setResult(result);
		setError(error);
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

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
