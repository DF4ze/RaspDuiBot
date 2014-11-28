package modeles.dao.beansreceve;


public class TourelleAction extends GeneralAction {

	private static final long serialVersionUID = 1L;
	
	private Integer servo	 	= null;
	private Integer degres 		= null;

	public TourelleAction() {
		super();
	}

	public TourelleAction( int servo, int degres) {
		super();
		this.degres=degres;
		this.servo=servo;
	}

	public TourelleAction( int servo, int degres, int priority) {
		super();
		this.degres=degres;
		this.servo=servo;
		setPriority(priority);
	}

	
	public String getAction() {
		return IAction.modeServo +"."+ servo +"."+ degres;
	}
	
	public boolean isComplete(){
		boolean bOk = true;
		if( servo == null )
			bOk = false;
		else if( degres == null )
			bOk = false;
		
		return bOk;
	}
	
	public int getServo() {
		return servo;
	}
	public void setServo(int servo) {
		this.servo = servo;
	}

	public int getDegres() {
		return degres;
	}
	public void setDegres(int degres) {
		this.degres = degres;
	}





}
