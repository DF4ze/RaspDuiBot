package modeles.dao.communication.beansactions;


public class TourelleAction extends GeneralAction {

	private static final long serialVersionUID = 2L;
	
	private Integer degresX	 	= null;
	private Integer degresY		= null;

	public TourelleAction() {
		super();
	}

	public TourelleAction( int degresX, int degresY) {
		super();
		this.degresX=degresX;
		this.degresY=degresY;
	}

	public TourelleAction( int degresX, int degresY, int priority) {
		super();
		this.degresX=degresX;
		this.degresY=degresY;
		setPriority(priority);
	}

	
	public String getAction() {
		return IAction.modeServo +"."+ degresX +"."+ degresY;
	}
	
	public boolean isComplete(){
		boolean bOk = true;
		if( degresX == null )
			bOk = false;
		else if( degresY == null )
			bOk = false;
		
		return bOk;
	}
	
	public int getDegresX() {
		return degresX;
	}
	public void setDegresX(int degresX) {
		this.degresX = degresX;
	}

	public int getDegresY() {
		return degresY;
	}
	public void setDegresY(int degresY) {
		this.degresY = degresY;
	}





}
