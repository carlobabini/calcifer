package it.angelobabini.calcifer.stf.backend.data;

import it.angelobabini.calcifer.backend.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class StfDAO {

	public static List<String> getRicognzioneUUIDs() {
		EntityManager entityManager = null;
		try {
			entityManager = DBHelper.instance().getEntityManager();
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
			entityManager = DBHelper.instance().getEntityManager();
			Ricognizione ricognizione = null;

			Query q = entityManager.createQuery("SELECT e FROM ricognizioni e WHERE instanceid = :instanceid");
			q.setParameter("instanceid", instanceid);

			@SuppressWarnings("unchecked")
			List<Ricognizione> list = q.getResultList();
			if(list.size() == 1) {
				ricognizione = list.get(0);
				entityManager.detach(ricognizione);
			}
			
			return ricognizione;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Ricognizione> getRicognizioneByID(String id) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();

			Query q = entityManager.createQuery("SELECT e FROM ricognizioni e WHERE id = :id order by inizio desc", Ricognizione.class);
			q.setParameter("id", id);

			return q.getResultList();
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}

	public static boolean insertRicognizione(Ricognizione ricognizione) {
		/*EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(ricognizione);
			entityManager.getTransaction().commit();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}*/
		return saveRicognizione(ricognizione);
	}

	public static boolean saveRicognizione(Ricognizione ricognizione) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();
			entityManager.getTransaction().begin();
			if(!entityManager.contains(ricognizione))
				entityManager.merge(ricognizione);
			/*Capisaldo capisaldo = getCapisaldoByID(ricognizione.getId());
			if(capisaldo == null) {
				capisaldo = createCapisaldo(ricognizione);
			}
			boolean found = false;
			for(Ricognizione r : capisaldo.getRicognizioniList()) {
				if(r.getInstanceID().equals(ricognizione.getInstanceID())) {
					found = true;
					break;
				}
			}
			if(!found) {
				capisaldo.getRicognizioniList().add(ricognizione);
			}
			entityManager.merge(capisaldo);*/
			
			entityManager.getTransaction().commit();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
	
	public static List<String> operatoriList() {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();

			Query q = entityManager.createQuery("SELECT distinct e.operatore FROM ricognizioni e");

			@SuppressWarnings("unchecked")
			List<String> list = q.getResultList();

			return list;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
	
	public static boolean saveRicognizioneLog(Ricognizione ricognizione, String username, String azione) throws Exception {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = DBHelper.getConnection();
			String sql = "insert into ricognizioni_log select current_timestamp as ts, ? as username, ? as azione, r.* from ricognizioni as r where instanceID = ?";
			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, azione);
			statement.setString(3, ricognizione.getInstanceID());
			return statement.execute();
		} finally {
			try {
				statement.close();
			} catch(Exception e) { }
			try {
				conn.close();
			} catch(Exception e) { }
		}
		/*EntityManager entityManager = null;

		try {
			//entityManager = DBHelper.getEntityManager();
			entityManager.getTransaction().getconn
		} finally {
			//DBHelper.closeEntityManager(entityManager);
		}
		INSERT INTO ricognizioni_log(
	            ts, utente, azione, inizio, fine, operatore, id, id_in_altre_reti, 
	            id_caposaldo_principale, latitude, longitude, altitude, accuracy, 
	            ubicazione, indirizzo, accesso, posizione_contrassegno, materializzazione, 
	            contrassegno_ancorato, contrassegno_danneggiato, descrizione_danneggiamento, 
	            tipologia_fondazione, anomalie_manufatto, descrizione_anomalie, 
	            note_rilevanti, descr_note_rilevanti, altre_note_rilevanti, tipo, 
	            altro_tipo, foto_manufatto, foto_panoramica, instanceid, modifica, 
	            appartenenza, punto, affidabilita, esistente, stato_scomparso, 
	            note_scomparso, altre_scomparso, necessita_ripristino, descrizione_ripristino, 
	            latitude_ripristino, longitude_ripristino, altitude_ripristino, 
	            accuracy_ripristino, foto_sito_ripristino, foto_aggiornata, foto_danno_contrassegno, 
	            foto_danno_manufatto)
	    VALUES (?, ?, ?, ?, ?, ?, ?, ?, 
	            ?, ?, ?, ?, ?, 
	            ?, ?, ?, ?, ?, 
	            ?, ?, ?, 
	            ?, ?, ?, 
	            ?, ?, ?, ?, 
	            ?, ?, ?, ?, ?, 
	            ?, ?, ?, ?, ?, 
	            ?, ?, ?, ?, 
	            ?, ?, ?, 
	            ?, ?, ?, ?, 
	            ?);*/
	}
	
	public static Capisaldo createCapisaldo(Ricognizione ricognizione) {
		Capisaldo capisaldo = new Capisaldo();
		capisaldo.setId(ricognizione.getId());
		capisaldo.setId_caposaldo_principale(ricognizione.getId_caposaldo_principale());
		capisaldo.setId_in_altre_reti(ricognizione.getId_in_altre_reti());
		capisaldo.setLatitude(ricognizione.getLatitude());
		capisaldo.setLongitude(ricognizione.getLongitude());
		capisaldo.setAltitude(ricognizione.getAltitude());
		capisaldo.setAccuracy(ricognizione.getAccuracy());
		capisaldo.setAccesso(ricognizione.getAccesso());
		capisaldo.setIndirizzo(ricognizione.getIndirizzo());
		capisaldo.setUbicazione(ricognizione.getUbicazione());
		return capisaldo;
	}
	
	public static Capisaldo getCapisaldoByID(String id) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();
			Capisaldo capisaldo = null;

			Query q = entityManager.createQuery("SELECT e FROM capisaldo e WHERE id = :id");
			q.setParameter("id", id);

			@SuppressWarnings("unchecked")
			List<Capisaldo> list = q.getResultList();
			if(list.size() == 1) {
				capisaldo = list.get(0);
				entityManager.detach(capisaldo);
			}
			
			return capisaldo;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
	
	public static Capisaldo getCapisaldoByKey(String key) {
		EntityManager entityManager = null;

		try {
			entityManager = DBHelper.instance().getEntityManager();
			Capisaldo capisaldo = null;

			Query q = entityManager.createQuery("SELECT e FROM capisaldo e WHERE kint = :key");
			q.setParameter("key", key);

			@SuppressWarnings("unchecked")
			List<Capisaldo> list = q.getResultList();
			if(list.size() == 1){
				capisaldo = list.get(0);
				entityManager.detach(capisaldo);
			}

			return capisaldo;
		} finally {
			DBHelper.closeEntityManager(entityManager);
		}
	}
	
	public static void saveCapisaldo(Ricognizione r) {
		
	}
	
	public static int quickUpdate(String sql) throws Exception {
		Connection conn = null;
		Statement statement = null;
		try {
			conn = DBHelper.getConnection();
			statement = conn.createStatement();
			return statement.executeUpdate(sql);
		} finally {
			try {
				statement.close();
			} catch(Exception e) {}
			DBHelper.closeConnection(conn);
		}
	}
}
