package modeles.dao.communication.beansinfos;

public class SensorInfo extends GeneralInfo {
	

	private static final long serialVersionUID = 1L;
	
	private int sensor;
	private int value;

	public SensorInfo() {
		
	}
	public SensorInfo( int sensor, int value ) {
		setSensor(sensor);
		setValue(value);
	}

	@Override
	public String getInfo() {
		return IInfo.typeSensor+"."+sensor+"."+value;
	}

	public int getSensor() {
		return sensor;
	}
	public void setSensor(int sensor) {
		this.sensor = sensor;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

}
