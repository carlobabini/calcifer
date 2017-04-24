package it.angelobabini.calcifer.admin;

import it.angelobabini.calcifer.CalciferUI;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AdminView extends GridLayout implements View {
	private static final long serialVersionUID = -1758712775497822344L;
	
	public static final String VIEW_NAME = "Admin";
	private final Button adminUser = new Button("Utenti");
	
	public AdminView() {
		setSizeFull();
		addButton(adminUser, AdminUserView.VIEW_NAME);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	private void addButton(Button button, final String path) {
		addComponent(button);
		button.addStyleName(ValoTheme.BUTTON_HUGE);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3475166508720226522L;

			@Override
			public void buttonClick(ClickEvent event) {
				//viewLogic.newRicognizione();
				CalciferUI.get().getNavigator().navigateTo(AdminUserView.VIEW_NAME);
			}
		});
	}

}
