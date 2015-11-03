package modeles.dao.communication.beansinfos;


public class PingInfo extends GeneralInfo {


	private static final long serialVersionUID = 1L;
	private Long timeStamp;

	public PingInfo( long timeStamp ) {
		setTimeStamp(timeStamp);
	}

	@Override
	public String getInfo() {
		return getTimeStamp().toString();
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}



}
