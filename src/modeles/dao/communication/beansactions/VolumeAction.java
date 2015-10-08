package modeles.dao.communication.beansactions;

public class VolumeAction extends GeneralAction {

	private static final long serialVersionUID = 1L;
	
	private boolean sens = true; // true monte, false descend
	private int pourcent = 10;
	private boolean muting = false;
	
	public VolumeAction() {
	}

	public VolumeAction( boolean sens, int pourcent ) {
		setPourcent(pourcent);
		setSens(sens);
	}

	public VolumeAction( boolean sens, boolean muting ) {
		setMuting(muting);
		setSens(sens);
	}

	public VolumeAction( boolean sens, boolean muting, int pourcent ) {
		setMuting(muting);
		setSens(sens);
		setPourcent(pourcent);
	}

	@Override
	public String getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isSens() {
		return sens;
	}

	public void setSens(boolean sens) {
		this.sens = sens;
	}

	public int getPourcent() {
		return pourcent;
	}

	public void setPourcent(int pourcent) {
		this.pourcent = pourcent;
	}

	public boolean isMuting() {
		return muting;
	}

	public void setMuting(boolean muting) {
		this.muting = muting;
	}

}
