package modeles.dao.communication.beansinfos;

public class SensorInfoDist extends SensorInfo {


	private int pos;
	
	private static final long serialVersionUID = 1L;

	public SensorInfoDist() {
		// TODO Auto-generated constructor stub
	}

	public SensorInfoDist(int sensor, int value) {
		super(sensor, value);
	}

	public SensorInfoDist(int sensor, int pos, int value ) {
		super(sensor, value);

		setPos(pos);
	}

	
	@Override
	public String getInfo() {
		return IInfo.typeSensor+"."+getSensor()+"."+getPos()+"."+getValue();
	}

	
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
