package modeles.dao.communication.beansinfos;

public class StateInfo extends GeneralInfo {
	

	private static final long serialVersionUID = 1L;
	
	private int materiel;
	private boolean stat;

	public StateInfo() {
	}

	public StateInfo( int materiel, boolean stat ) {
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

	public boolean getStat() {
		return stat;
	}
	public void setStat(boolean stat) {
		this.stat = stat;
	}

}
