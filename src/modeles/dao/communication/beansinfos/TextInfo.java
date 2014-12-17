package modeles.dao.communication.beansinfos;

public class TextInfo extends GeneralInfo {


	private static final long serialVersionUID = 1L;
	
	private String text = "";
	
	public TextInfo() {
	}

	public TextInfo( String text ) {
		setText(text);
	}

	@Override
	public String getInfo() {
		return text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
