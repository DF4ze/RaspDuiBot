package modeles.dao.communication.beansactions;

public class GetStateAction extends GeneralAction {


	private static final long serialVersionUID = 1L;

	public GetStateAction() {
	}

	@Override
	public String getAction() {
		return IAction.modeExtra+"."+IAction.typeState+"."+IAction.stateGet;
	}

	@Override
	public boolean isComplete() {
		return true;
	}

}
