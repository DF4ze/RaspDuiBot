package modeles.dao.communication.beansactions;

public class PongAction extends GeneralAction {

	private static final long serialVersionUID = 1L;

	public PongAction( long timeStamp ) {
		setTimeStamp(timeStamp);
	}

	@Override
	public String getAction() {
		return getTimeStamp()+"";
	}

	@Override
	public boolean isComplete() {
		return true;
	}


}
