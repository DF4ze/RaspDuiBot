package modeles.dao.communication.beanshell;

public class ShellCmd {
	private String name;
	private String[] command;

	public ShellCmd() {
	}

	public ShellCmd( String name, String[] command ) {
		setName(name);
		setCommand(command);
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

}
