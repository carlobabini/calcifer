package it.angelobabini.calcifer.backend;

import java.sql.Connection;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class DBHelper implements ServletContextListener {
	public static final Logger logger = Logger.getLogger(String.valueOf(DBHelper.class));
	
	private static DBHelper instance = null;
	private static EntityManagerFactory emFactory = null;
	
	private DBHelper() {
		logger.info("Creating DBHelper");
	}
	
	public static synchronized DBHelper instance() {
		if(instance == null)
			instance = new DBHelper();
		return instance;
	}

	public void initEntityManagerFactory() {
		if(emFactory == null)
			emFactory = Persistence.createEntityManagerFactory("calcifer-backend");
	}
	public void closeEntityManagerFactory() {
		emFactory.close();
	}
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("contextInitialized: initializing EntityManagerFactory");
		initEntityManagerFactory();
	}

	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("contextDestroyed: closing EntityManagerFactory");
		closeEntityManagerFactory();
	}

	public EntityManager getEntityManager(){
		if(emFactory == null)
			initEntityManagerFactory();
		return emFactory.createEntityManager();
	}

	public static void closeEntityManager(EntityManager entityManager) {
		if(entityManager != null) {
			try {
				entityManager.close();
			} catch(Exception e) {
			}
		}
	}
	
	public static void rollback(EntityManager entityManager) {
		if(entityManager != null) {
			try {
				entityManager.getTransaction().rollback();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Connection getConnection() throws Exception {
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:jboss/datasources/PostgreSQLDS");

		return ds.getConnection(); 
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch(Exception e) { }
	}
}
