package modeles.dao.communication.beansactions;

public class ExtraAction extends GeneralAction{

	private static final long serialVersionUID = 1L;
	
	private Integer type 	= null;
	private Integer key 	= null;
	private Integer value 	= null;

	public ExtraAction() {
		super();
	}

	public ExtraAction(int type, int key, int value) {
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public ExtraAction(int type, int key, int value, int priority) {
		super();
		this.type = type;
		this.key = key;
		this.value = value;
		setPriority(priority);
	}



	public String getAction() {
		return IAction.modeExtra +"."+ type +"."+ key +"."+ value;
	}
	
	public boolean isComplete(){
		boolean bOk = true;
		if( key == null )
			bOk = false;
		else if( value == null )
			bOk = false;
		
		return bOk;
	}

	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


}
