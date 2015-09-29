package modeles.dao.communication.beansinfos;

public class SensorInfo extends GeneralInfo {
	

	private static final long serialVersionUID = 1L;
	
	private int sensor;
	private float value;

	public SensorInfo() {
		
	}
	public SensorInfo( int sensor, float value ) {
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

	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

}
