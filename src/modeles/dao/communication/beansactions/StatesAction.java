package modeles.dao.communication.beansactions;

public class StatesAction extends GeneralAction {

	private static final long serialVersionUID = 1L;
	
	private boolean streamEnable;
	private boolean standByEnable;

	public StatesAction() {
	}

	public StatesAction( boolean stream, boolean standBy ) {
		streamEnable = stream;
		standByEnable = standBy; 
	}

	public StatesAction( boolean stream, boolean standBy, int prio ) {
		streamEnable = stream;
		standByEnable = standBy; 
		setPriority(prio);
	}

	@Override
	public String getAction() {
		return null;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	public boolean isStreamEnable() {
		return streamEnable;
	}
	public void setStreamEnable(boolean streamEnable) {
		this.streamEnable = streamEnable;
	}

	public boolean isStandByEnable() {
		return standByEnable;
	}
	public void setStandByEnable(boolean standByEnable) {
		this.standByEnable = standByEnable;
	}

}
