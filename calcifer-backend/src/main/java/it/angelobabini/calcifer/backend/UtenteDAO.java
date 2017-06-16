package it.angelobabini.calcifer.backend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UtenteDAO implements CRUDLogic<Utente> {
	
	public static Utente canLogin(String username, String password) {
		EntityManager entityManager = null;
		try {
			entityManager = DBHelper.instance().getEntityManager();
			Utente utente = null;
			
			Query q = entityManager.createQuery("SELECT e FROM utenti e WHERE username=:username and password=:password");
			q.setParameter("username", username);
			q.setParameter("password", Utente.encodePassword(password));
			
			@SuppressWarnings("unchecked")
			List<Utente> list = q.getResultList();
			entityManager.close();
			if(list.size() == 1)
				utente = list.get(0);
			
			return utente;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
	
	public static Utente readUtente(String username) {
		EntityManager entityManager = null;
		try {
			entityManager = DBHelper.instance().getEntityManager();
			Utente utente = null;
			
			Query q = entityManager.createQuery("SELECT e FROM utenti e WHERE username=:username");
			q.setParameter("username", username);
			
			@SuppressWarnings("unchecked")
			List<Utente> list = q.getResultList();
			entityManager.close();
			if(list.size() == 1)
				utente = list.get(0);
			
			return utente;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	@Override
	public Utente newEntity() {
		throw new RuntimeException("Can't be called");
	}

	@Override
	public void deleteEntity(Utente t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveEntity(Utente t) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(t);
			entityManager.getTransaction().commit();
			entityManager.close();
			//return true;
		} catch(Exception e) {
			DBHelper.rollback(entityManager);
			e.printStackTrace();
			//return false;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	@Override
	public void editEntity(Utente t) {
		saveEntity(t);
	}

}
