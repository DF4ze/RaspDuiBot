package modeles.dao.communication.beansinfos;

public class StateInfo extends GeneralInfo {
	
	private int materiel;
	private int stat;

	public StateInfo() {
	}

	public StateInfo( int materiel, int stat ) {
		setMateriel(materiel);
		setStat(stat);
	}

	@Override
	public String getInfo() {
		return IInfo.typeState+"."+materiel+"."+stat;
	}

	public int getMateriel() {
		return materiel;
	}
	public void setMateriel(int materiel) {
		this.materiel = materiel;
	}

	public int getStat() {
		return stat;
	}
	public void setStat(int stat) {
		this.stat = stat;
	}

}
