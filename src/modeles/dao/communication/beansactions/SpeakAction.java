package modeles.dao.communication.beansactions;

/**
 * @author Klm&Didie
 *
 */
public class SpeakAction extends GeneralAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int voixHomme 	= 1;
	public static final int voixFemme 	= 2;
	
	public static final int voixRapide 	= 1;
	public static final int voixMoyenne = 2;
	public static final int voixLente 	= 3;
	
	public static final int ttsEspeak 	= 1;
	public static final int ttsPico		= 2;
	

	private String text;
	private boolean completed = false;
	private int voix = voixFemme;
	private int vitesse = voixMoyenne;
	private int tss = ttsPico;
	
	public SpeakAction() {
	}
	
	public SpeakAction( String text ) {
		this.text = text;
		completed = true;
	}

	@Override
	public String getAction() {
		return getText();
	}

	@Override
	public boolean isComplete() {
		return completed;
	}
	
	@Override
	public String toString() {
		return getText();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		completed = true;
	}

	public int getVoix() {
		return voix;
	}

	public void setVoix(int voix) {
		this.voix = voix;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}

	public int getTss() {
		return tss;
	}

	public void setTss(int tss) {
		this.tss = tss;
	}

}
