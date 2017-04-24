package it.angelobabini.calcifer.stf.backend.data;

import it.angelobabini.calcifer.backend.DBHelper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class StfDAO {

	public static List<String> getRicognzioneUUIDs() {
		EntityManager entityManager = null;
		try {
			entityManager = DBHelper.getEntityManager();
			Query q = entityManager.createQuery("SELECT e.instanceID FROM ricognizioni e");
			@SuppressWarnings("unchecked")
			List<String> list = q.getResultList();
			return list;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	public static Object[][] getRicognzioneSyncData() {
		List<String> ids = getRicognzioneUUIDs();
		Object[][] result = new Object[ids.size()][4];
		int i = 0;
		for(String id : ids) {
			Ricognizione r = getRicognizione(id);
			result[i] = new Object[] {
					r.getInstanceID(),
					r.getId(),
					r.getInizio(),
					r.getModifica()
			};
			i++;
		}
		return result;
	}

	public static Ricognizione getRicognizione(String instanceid) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.getEntityManager();
			Ricognizione ricognizione = null;

			Query q = entityManager.createQuery("SELECT e FROM ricognizioni e WHERE instanceid = :instanceid");
			q.setParameter("instanceid", instanceid);

			@SuppressWarnings("unchecked")
			List<Ricognizione> list = q.getResultList();
			entityManager.close();
			if(list.size() == 1)
				ricognizione = list.get(0);

			return ricognizione;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	public static boolean saveRicognizione(Ricognizione ricognizione) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.getEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(ricognizione);
			entityManager.getTransaction().commit();
			entityManager.close();
			return true;
		} catch(Exception e) {
			DBHelper.rollback(entityManager);
			e.printStackTrace();
			return false;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
}
