package it.angelobabini.calcifer;

import it.angelobabini.calcifer.admin.AdminUserView;
import it.angelobabini.calcifer.admin.AdminView;
import it.angelobabini.calcifer.samples.about.AboutView;
import it.angelobabini.calcifer.samples.crud.SampleCrudView;
import it.angelobabini.calcifer.stf.RicognizioniView;
import it.angelobabini.calcifer.stf.RicognizioniViewOLD;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends HorizontalLayout {
	private static final long serialVersionUID = -1532707564399978804L;
	private Menu menu;

    public MainScreen(CalciferUI ui) {

        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(ui, viewContainer);
        navigator.setErrorView(ErrorView.class);
        menu = new Menu(navigator);
        
        //TODO generazione automatica delle voci di menu leggendo da DB
        menu.addView(new RicognizioniView(), RicognizioniView.VIEW_NAME, RicognizioniViewOLD.VIEW_NAME, FontAwesome.ARROWS_ALT);
        if (CalciferUI.get().getAccessControl().isUserInRole("admin")) {
        	//menu.addView(new AdminView(), AdminView.VIEW_NAME, AdminView.VIEW_NAME, FontAwesome.USER);
        	//navigator.addView(AdminUserView.VIEW_NAME, new AdminUserView());
        	//menu.addView(new SampleCrudView(), SampleCrudView.VIEW_NAME, SampleCrudView.VIEW_NAME, FontAwesome.EDIT);
    	}
        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME, FontAwesome.INFO_CIRCLE);

        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {
		private static final long serialVersionUID = -424077844518588659L;

		@Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
