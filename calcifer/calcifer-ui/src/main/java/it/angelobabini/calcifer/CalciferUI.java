package it.angelobabini.calcifer;

import javax.servlet.annotation.WebServlet;

import it.angelobabini.calcifer.LoginScreen.LoginListener;
import it.angelobabini.calcifer.authentication.AccessControl;
import it.angelobabini.calcifer.authentication.BasicAccessControl;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("calcifer")
@Widgetset("it.angelobabini.calcifer.CalciferWidgetset")
public class CalciferUI extends UI {
	private static final long serialVersionUID = -6645021520794103645L;
	private AccessControl accessControl = new BasicAccessControl();
	
	public static final String PERSISTENCE_UNIT_NAME = "calcifer-backend";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("Calcifer");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
				private static final long serialVersionUID = 6961525843786200182L;

				@Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(CalciferUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static CalciferUI get() {
        return (CalciferUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "CalciferUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CalciferUI.class, productionMode = false)
    public static class CalciferUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 6045924633742041509L;
    }
}
