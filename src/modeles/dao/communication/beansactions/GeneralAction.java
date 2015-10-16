package modeles.dao.communication.beansactions;

import java.util.Date;

public abstract class GeneralAction implements IAction {

	private static final long serialVersionUID = 1L;
	
	private int priority 		= 0;
	private long timeStamp 		= 0;
	private boolean repeatable 	= false;

	public GeneralAction() {
		setTimeStamp(0);
	}

	@Override
	public abstract String getAction();
	@Override
	public abstract boolean isComplete();

	@Override
	public long getTimeStamp() {
		return timeStamp;
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setTimeStamp(long timeStamp) {
		if( timeStamp == 0 )
			this.timeStamp = new Date().getTime();
		else
			this.timeStamp = timeStamp;
	}
	
	public String toString(){
		return getAction() + IAction.serialEndCommand;
	}

	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}

}
