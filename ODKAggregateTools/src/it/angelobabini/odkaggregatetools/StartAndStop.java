package it.angelobabini.odkaggregatetools;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartAndStop implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	OdkToCalcifer.instance().startContinuos();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	OdkToCalcifer.instance().stopContinuos();
    }
}
