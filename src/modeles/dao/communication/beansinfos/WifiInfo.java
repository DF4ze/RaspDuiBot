package modeles.dao.communication.beansinfos;

import java.util.HashMap;

public class WifiInfo extends GeneralInfo {
	

	private static final long serialVersionUID = 1L;
	
	private String signal;
	private String quality;
	private String noise;
	

	public WifiInfo() {
	}

	public WifiInfo( HashMap<String, String> wifiInfos) {
		if( wifiInfos.containsKey("signal") )
			signal = wifiInfos.get("signal");
		
		if( wifiInfos.containsKey("quality") )
			quality = wifiInfos.get("quality");
		
		if( wifiInfos.containsKey("noise") )
			noise = wifiInfos.get("noise");
	}

	@Override
	public String getInfo() {
		return "Wifi : "+
				(signal!=null?" signal : "+signal:"")+
				(quality!=null?" quality : "+quality:"")+
				(noise!=null?" noise : "+noise:"");
	}

	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getNoise() {
		return noise;
	}

	public void setNoise(String noise) {
		this.noise = noise;
	}


}
