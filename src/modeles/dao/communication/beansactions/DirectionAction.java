package modeles.dao.communication.beansactions;



public class DirectionAction extends GeneralAction {


	private static final long serialVersionUID = 1L;
	
	private Integer vitesse 	= null;
	private Integer delta 		= null;
		
	public DirectionAction() {
		super(); 
	}

	public DirectionAction( int vitesse, int delta ) {
		super(); 
		this.vitesse = vitesse;
		this.delta = delta;
	}

	public DirectionAction( int vitesse, int delta, int priority ) {
		super(); 
		this.vitesse = vitesse;
		this.delta = delta;
		setPriority(priority);
	}
	
	@Override
	public String getAction() {
		return IAction.modeMotor+"."+vitesse+"."+delta;
	}
	public boolean isComplete(){
		boolean bOk = true;
		if( vitesse == null )
			bOk = false;
		else if( delta == null )
			bOk = false;
		
		return bOk;
	}
	
	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}
}
