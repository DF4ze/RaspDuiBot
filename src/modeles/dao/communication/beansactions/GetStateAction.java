package modeles.dao.communication.beansactions;

public class GetStateAction extends GeneralAction {

	private int type;
	private int compo;

	private static final long serialVersionUID = 1L;

	public GetStateAction() {
	}
	public GetStateAction( int type, int compo) {
		setType(type);
		setCompo(compo);
	}

	@Override
	public String getAction() {
		return IAction.modeState+"."+type+"."+compo;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getCompo() {
		return compo;
	}
	public void setCompo(int compo) {
		this.compo = compo;
	}

}
