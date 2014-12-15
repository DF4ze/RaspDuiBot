package modeles.dao.communication.beansinfos;

import java.util.ArrayList;

public class StartInfo extends GeneralInfo {

	private ArrayList<IInfo> infos = new ArrayList<IInfo>();
	
	public StartInfo() {
	}

	@Override
	public String getInfo() {
		String sInfos = "";
		for( IInfo info : infos ){
			sInfos += info.toString()+" ";
		}
		
		return sInfos.trim();
	}
	
	public void addInfo( IInfo info ){
		infos.add(info);
	}
	
	public int nbInfo(){
		return infos.size();
	}

}
