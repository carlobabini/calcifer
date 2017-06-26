package it.angelobabini.stf_api;

import it.angelobabini.calcifer.backend.DBHelper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartAndStop implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	DBHelper.instance().initEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	DBHelper.instance().closeEntityManagerFactory();
    }

}
