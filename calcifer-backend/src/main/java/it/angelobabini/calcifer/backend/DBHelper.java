package it.angelobabini.calcifer.backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBHelper {

	private static final EntityManagerFactory emFactory;
	static {
		emFactory = Persistence.createEntityManagerFactory("calcifer-backend");
	}
	public static void closeEntityManagerFactory(){
		emFactory.close();
	}

	public static EntityManager getEntityManager(){
		return emFactory.createEntityManager();
	}

	public static void closeEntityManager(EntityManager entityManager) {
		if(entityManager != null) {
			try {
				entityManager.close();
			} catch(Exception e) {
				e.printStackTrace();
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

}
