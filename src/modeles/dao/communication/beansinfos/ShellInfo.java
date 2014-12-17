package modeles.dao.communication.beansinfos;



public class ShellInfo extends GeneralInfo {
	

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String[] command;
	private String result;

	public ShellInfo() {
	}

//	public ShellInfo( ShellResult shellresult ) {
//		setName( shellresult.getName() );
//		setCommand( shellresult.getCommand() );
//		setResult( shellresult.getResult() );
//	}

	public ShellInfo( String name, String[] command, String result ) {
		setName( name );
		setCommand( command );
		setResult( result );
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
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
