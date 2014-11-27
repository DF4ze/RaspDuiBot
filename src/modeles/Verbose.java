package modeles;

public class Verbose {

	private static boolean enable = true;
	private Verbose() {
		
	}
	
	public static boolean isEnable(){
		return enable;
	}
	public static void setEnable(boolean en){
		enable = en;
	}

}
