package it.angelobabini.calcifer.backend;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

public class DBHelper {

	private static EntityManagerFactory emFactory = null;
	public static void initEntityManagerFactory() {
		emFactory = Persistence.createEntityManagerFactory("calcifer-backend");
	}
	public static void closeEntityManagerFactory() {
		emFactory.close();
	}

	public static EntityManager getEntityManager(){
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
}
