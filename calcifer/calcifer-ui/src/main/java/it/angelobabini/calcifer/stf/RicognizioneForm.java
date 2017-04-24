package it.angelobabini.calcifer.stf;

import com.vaadin.ui.CssLayout;

public class RicognizioneForm extends CssLayout {
	private static final long serialVersionUID = 8434108421302157224L;
	
	private RicognizioniLogic viewLogic;

	public RicognizioneForm(RicognizioniLogic logic) {
		super();
        addStyleName("product-form");
        viewLogic = logic;
	}
	
	public void editItem(Object item) {
		//TODO what?
	}
}
