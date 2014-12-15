package modeles.dao.communication.beansinfos;

public abstract class GeneralInfo implements IInfo {

	public GeneralInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract String getInfo();
	
	public String toString(){
		return getInfo();
	}

}
