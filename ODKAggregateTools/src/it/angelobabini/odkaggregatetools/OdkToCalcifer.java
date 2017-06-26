package it.angelobabini.odkaggregatetools;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class OdkToCalcifer {
	private static final Logger logger = Logger.getLogger(String.valueOf(OdkToCalcifer.class));
	
	private static OdkToCalcifer instance = null;
	private boolean continuos = false;
	private Runnable runnable = null;
	private Date lastRun = null;

	private Object controlLock = new Object();
	
	private OdkToCalcifer() {
	}
	
	public static synchronized OdkToCalcifer instance() {
		if(instance == null)
			instance = new OdkToCalcifer();
		return instance;
	}
	
	public void startContinuos() {
		logger.info("Start continuos");
		synchronized (controlLock) {
			if(runnable == null) {
				setContinuos(true);
				runnable = new Runnable() {

					@Override
					public void run() {
						logger.info("Starting continuos");
						while(isContinuos()) {
							execute();
							setLastRun(new Date());
							try {
								Thread.sleep(Setting.getAsInt("TASK_SLEEP_MS"));
							} catch (InterruptedException e) {
								logger.info(e.getClass()+" "+e.getMessage());
								e.printStackTrace();
								break;
							}
							logger.info("Exiting continuos");
						}
					}
				};
				new Thread(runnable).start();
			}
		}
	}

	public void stopContinuos() {
		logger.info("Stop continuos");
		synchronized (controlLock) {
			if(runnable != null) {
				logger.info("Stopping continuos");
				setContinuos(false);
				runnable = null;
			}
		}
	}
	
	public synchronized void executeSingle() {
		if(!isContinuos())
			execute();
	}
	
	private void setContinuos(boolean continuos) {
		synchronized (controlLock) {
			this.continuos = continuos;
		}
	}
	
	public boolean isContinuos() {
		synchronized (controlLock) {
			return continuos;
		}
	}
	
	private void setLastRun(Date date) {
		this.lastRun = date;
	}
	
	public Date getLastRun() {
		if(lastRun == null)
			return null;
		else
			return new Date(lastRun.getTime());
	}

	public synchronized void execute() {
		logger.info("Starting executing");
		Connection connection = null;

		byte[] byteArray = null;
		int photoMaxWidth = Setting.getAsInt("PHOTO_MAX_WIDTH");
		int photoMaxHeight = Setting.getAsInt("PHOTO_MAX_HEIGHT");
		String basePath = Setting.getAsString("PHOTO_STORAGE_URL");
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME");
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD");
		String remoteApiBaspath = Setting.getAsString("REMOTE_API_BASEPATH");
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = DBHelper.getConnection();

			statement = connection.createStatement();
			resultSet = statement.executeQuery(
					"select "
					+"coalesce(\"RICO13017_CORE\".\"META_INSTANCE_ID\", '') as RICO13017_CORE§META_INSTANCE_ID  "
					+"from \"RICO13017_CORE\" "
					+"left join load_log on load_log.uuid = \"RICO13017_CORE\".\"_URI\" "
					+"where load_log.uuid is null "
			);
			ArrayList<String> instanceIDS = new ArrayList<String>();
			while(resultSet.next()) {
				instanceIDS.add(resultSet.getString("RICO13017_CORE§META_INSTANCE_ID"));
			}

			try { resultSet.close(); } catch(Exception i) { }
			try { statement.close(); } catch(Exception i) { }

			for(String instanceID : instanceIDS) {
				logger.info("Reading instance "+instanceID);
				statement = connection.createStatement();
				resultSet = statement.executeQuery(
						"select "
						+"\"RICO13017_CORE\".\"INFO_SOPRALLUOGO_INIZIO\" as RICO13017_CORE§INFO_SOPRALLUOGO_INIZIO,  "
						+"\"RICO13017_CORE\".\"INFO_SOPRALLUOGO_FINE\" as RICO13017_CORE§INFO_SOPRALLUOGO_FINE,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_SOPRALLUOGO_OPERATORE\", '') as RICO13017_CORE§INFO_SOPRALLUOGO_OPERATORE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ID\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ID,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_ID\", '') as RICO13017_CORE§ID_CAPOSALDO_ID,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ID_IN_ALTRE_RETI\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ID_IN_ALTRE_RETI,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_ID_IN_ALTRE_RETI\", '') as RICO13017_CORE§ID_CAPOSALDO_ID_IN_ALTRE_RETI,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ID_CAPOSALDO_PRINCIPALE\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ID_CAPOSALDO_PRINCIPALE, "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_ID_CAPOSALDO_PRINCIPALE\", '') as RICO13017_CORE§ID_CAPOSALDO_ID_CAPOSALDO_PRINCIPALE,  "
						+"coalesce(\"RICO13017_CORE\".\"COORDINATE_LAT\", 0) as RICO13017_CORE§COORDINATE_LAT,  "
						+"coalesce(\"RICO13017_CORE\".\"COORDINATE_LNG\", 0) as RICO13017_CORE§COORDINATE_LNG,  "
						+"coalesce(\"RICO13017_CORE\".\"COORDINATE_ALT\", 0) as RICO13017_CORE§COORDINATE_ALT,  "
						+"coalesce(\"RICO13017_CORE\".\"COORDINATE_ACC\", 0) as RICO13017_CORE§COORDINATE_ACC,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_UBICAZIONE\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_UBICAZIONE, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_TOPONOMASTICHE_UBICAZIONE\", '') as RICO13017_CORE§INFO_TOPONOMASTICHE_UBICAZIONE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_INDIRIZZO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_INDIRIZZO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_INDIRIZZO_INDIRIZZO\", '') as RICO13017_CORE§INFO_INDIRIZZO_INDIRIZZO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ACCESSO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ACCESSO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_INDIRIZZO_ACCESSO\", '') as RICO13017_CORE§INFO_INDIRIZZO_ACCESSO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_POSIZIONE_CONTRASSEGNO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_POSIZIONE_CONTRASSEGNO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_POSIZIONE_POSIZIONE_CONTRASSEGNO\", '') as RICO13017_CORE§INFO_POSIZIONE_POSIZIONE_CONTRASSEGNO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_MATERIALIZZAZIONE\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_MATERIALIZZAZIONE, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_CONTRASSEGNO_MATERIALIZZAZIONE\", '') as RICO13017_CORE§INFO_CONTRASSEGNO_MATERIALIZZAZIONE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_CONTRASSEGNO_ANCORATO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_CONTRASSEGNO_ANCORATO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_VERIFICHE_CONTRASSEGNO_ANCORATO\", '') as RICO13017_CORE§INFO_VERIFICHE_CONTRASSEGNO_ANCORATO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_CONTRASSEGNO_DANNEGGIATO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_CONTRASSEGNO_DANNEGGIATO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_VERIFICHE_CONTRASSEGNO_DANNEGGIATO\", '') as RICO13017_CORE§INFO_VERIFICHE_CONTRASSEGNO_DANNEGGIATO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_DESCRIZIONE_DANNEGGIAMENTO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_DESCRIZIONE_DANNEGGIAMENTO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_VERIFICHE_DESCRIZIONE_DANNEGGIAMENTO\", '') as RICO13017_CORE§INFO_VERIFICHE_DESCRIZIONE_DANNEGGIAMENTO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_TIPOLOGIA_FONDAZIONE\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_TIPOLOGIA_FONDAZIONE, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_MANUFATTO_TIPOLOGIA_FONDAZIONE\", '') as RICO13017_CORE§INFO_MANUFATTO_TIPOLOGIA_FONDAZIONE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ANOMALIE_MANUFATTO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ANOMALIE_MANUFATTO, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_MANUFATTO_2_ANOMALIE_MANUFATTO\", '') as RICO13017_CORE§INFO_MANUFATTO_2_ANOMALIE_MANUFATTO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_DESCRIZIONE_ANOMALIE\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_DESCRIZIONE_ANOMALIE, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_MANUFATTO_3_DESCRIZIONE_ANOMALIE\", '') as RICO13017_CORE§INFO_MANUFATTO_3_DESCRIZIONE_ANOMALIE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_NOTE_RILEVANTI\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_NOTE_RILEVANTI, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_MANUFATTO_4_NOTE_RILEVANTI\", '') as RICO13017_CORE§INFO_MANUFATTO_4_NOTE_RILEVANTI,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_DESCR_NOTE_RILEVANTI\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_DESCR_NOTE_RILEVANTI, "
						+"coalesce(\"RICO13017_INFO_MANUFATTO_5_DESCR_NOTE_RILEVANTI\".\"VALUE\", '') as RICO13017_INFO_MANUFATTO_5_DESCR_NOTE_RILEVANTI§VALUE,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ALTRE_NOTE_RILEVANTI\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ALTRE_NOTE_RILEVANTI, "
						+"coalesce(\"RICO13017_CORE\".\"INFO_MANUFATTO_5_ALTRE_NOTE_RILEVANTI\", '') as RICO13017_CORE§INFO_MANUFATTO_5_ALTRE_NOTE_RILEVANTI,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_TIPO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_TIPO, "
						+"coalesce(\"RICO13017_CORE\".\"CONTESTO_AMBIENTALE_TIPO\", '') as RICO13017_CORE§CONTESTO_AMBIENTALE_TIPO,  "
						+"coalesce(\"RICO13017_CORE\".\"ID_CAPOSALDO_PREC_ALTRO_TIPO\", '') as RICO13017_CORE§ID_CAPOSALDO_PREC_ALTRO_TIPO, "
						+"coalesce(\"RICO13017_CORE\".\"CONTESTO_AMBIENTALE_ALTRO_TIPO\", '') as RICO13017_CORE§CONTESTO_AMBIENTALE_ALTRO_TIPO,  "
						+"coalesce(\"RICO13017_CORE\".\"META_INSTANCE_ID\", '') as RICO13017_CORE§META_INSTANCE_ID,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_STATO_ESISTENTE\", '') as RICO13017_CORE§INFO_STATO_ESISTENTE,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_STATO_SCOMPARSO_STATO_CS\", '') as RICO13017_CORE§INFO_STATO_SCOMPARSO_STATO_CS,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_STATO_SCOMPARSO_NOTE_SCOMPARSO\", '') as RICO13017_CORE§INFO_STATO_SCOMPARSO_NOTE_SCOMPARSO,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_STATO_SCOMPARSO_ALTRE_CAUSE\", '') as RICO13017_CORE§INFO_STATO_SCOMPARSO_ALTRE_CAUSE,  "
						+"coalesce(\"RICO13017_CORE\".\"INFO_STATO_SCOMPARSO_NECESSITA_RIPRISTINO\", '') as RICO13017_CORE§INFO_STATO_SCOMPARSO_NECESSITA_RIPRISTINO,  "
						+"coalesce(\"RICO13017_CORE\".\"RIPRISTINO_DESCRIZIONE_RIPRISTINO\", '') as RICO13017_CORE§RIPRISTINO_DESCRIZIONE_RIPRISTINO,  "
						+"coalesce(\"RICO13017_CORE\".\"RIPRISTINO_2_COORDINATE_RIPRISTINO_LAT\", 0) as RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_LAT,  "
						+"coalesce(\"RICO13017_CORE\".\"RIPRISTINO_2_COORDINATE_RIPRISTINO_LNG\", 0) as RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_LNG,  "
						+"coalesce(\"RICO13017_CORE\".\"RIPRISTINO_2_COORDINATE_RIPRISTINO_ALT\", 0) as RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_ALT,  "
						+"coalesce(\"RICO13017_CORE\".\"RIPRISTINO_2_COORDINATE_RIPRISTINO_ACC\", 0) as RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_ACC,  "
						+"\"RICO13017_IMMAGINI_FOTO_MANUFATTO_BLB\".\"VALUE\" as RICO13017_IMMAGINI_FOTO_MANUFATTO_BLB§VALUE, "
						+"\"RICO13017_IMMAGINI_2_FOTO_PANORAMICA_BLB\".\"VALUE\" as RICO13017_IMMAGINI_2_FOTO_PANORAMICA_BLB§VALUE, "
						+"\"RICO13017_IMMGN_VVNUTA_RCGNZNE_FOTO_AGGIORNATA_BLB\".\"VALUE\" as RICO13017_IMMGN_VVNUTA_RCGNZNE_FOTO_AGGIORNATA_BLB§VALUE, "
						+"\"RICO13017_INFO_MANUFATTO_3_FOTO_DANNO_MANUFATTO_BLB\".\"VALUE\" as RICO13017_INFO_MANUFATTO_3_FOTO_DANNO_MANUFATTO_BLB§VALUE, "
						+"\"RICO13017_INFO_VERIFICHE_FOTO_DANNO_CONTRASSEGNO_BLB\".\"VALUE\" as RICO13017_INFO_VERIFICHE_FOTO_DANNO_CONTRASSEGNO_BLB§VALUE, "
						+"\"RICO13017_RIPRISTINO_2_FOTO_SITO_PROPOSTO_BLB\".\"VALUE\" as RICO13017_RIPRISTINO_2_FOTO_SITO_PROPOSTO_BLB§VALUE "
						+"from \"RICO13017_CORE\"  "
						+"left join \"RICO13017_INFO_MANUFATTO_5_DESCR_NOTE_RILEVANTI\" on \"RICO13017_INFO_MANUFATTO_5_DESCR_NOTE_RILEVANTI\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_IMMAGINI_FOTO_MANUFATTO_BLB\" on \"RICO13017_IMMAGINI_FOTO_MANUFATTO_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_IMMAGINI_2_FOTO_PANORAMICA_BLB\" on \"RICO13017_IMMAGINI_2_FOTO_PANORAMICA_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_IMMGN_VVNUTA_RCGNZNE_FOTO_AGGIORNATA_BLB\" on \"RICO13017_IMMGN_VVNUTA_RCGNZNE_FOTO_AGGIORNATA_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_INFO_MANUFATTO_3_FOTO_DANNO_MANUFATTO_BLB\" on \"RICO13017_INFO_MANUFATTO_3_FOTO_DANNO_MANUFATTO_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_INFO_VERIFICHE_FOTO_DANNO_CONTRASSEGNO_BLB\" on \"RICO13017_INFO_VERIFICHE_FOTO_DANNO_CONTRASSEGNO_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join \"RICO13017_RIPRISTINO_2_FOTO_SITO_PROPOSTO_BLB\" on \"RICO13017_RIPRISTINO_2_FOTO_SITO_PROPOSTO_BLB\".\"_TOP_LEVEL_AURI\" = \"RICO13017_CORE\".\"_URI\" "
						+"left join load_log on load_log.uuid = \"RICO13017_CORE\".\"_URI\" "
						+"where load_log.uuid is null "
						+"and \"RICO13017_CORE\".\"META_INSTANCE_ID\"='"+instanceID+"' "
						+"LIMIT 1"
				);
				if(resultSet.next()) {
					Ricognizione r = new Ricognizione();
					r.setId(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ID"));
					r.setId(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_ID"));
					r.setId_caposaldo_principale(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ID_CAPOSALDO_PRINCIPALE"));
					r.setId_caposaldo_principale(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_ID_CAPOSALDO_PRINCIPALE"));
					r.setId_in_altre_reti(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ID_IN_ALTRE_RETI"));
					r.setId_in_altre_reti(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_ID_IN_ALTRE_RETI"));
					r.setInstanceID(resultSet.getString("RICO13017_CORE§META_INSTANCE_ID"));
					r.setInizio(resultSet.getTimestamp("RICO13017_CORE§INFO_SOPRALLUOGO_INIZIO"));
					r.setFine(resultSet.getTimestamp("RICO13017_CORE§INFO_SOPRALLUOGO_FINE"));
					r.setOperatore(resultSet.getString("RICO13017_CORE§INFO_SOPRALLUOGO_OPERATORE"));
					r.setUbicazione(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_UBICAZIONE"));
					r.setUbicazione(resultSet.getString("RICO13017_CORE§INFO_TOPONOMASTICHE_UBICAZIONE"));
					r.setIndirizzo(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_INDIRIZZO"));
					r.setIndirizzo(resultSet.getString("RICO13017_CORE§INFO_INDIRIZZO_INDIRIZZO"));
					r.setAccesso(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ACCESSO"));
					r.setAccesso(resultSet.getString("RICO13017_CORE§INFO_INDIRIZZO_ACCESSO"));
					r.setPosizione_contrassegno(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_POSIZIONE_CONTRASSEGNO"));
					r.setPosizione_contrassegno(resultSet.getString("RICO13017_CORE§INFO_POSIZIONE_POSIZIONE_CONTRASSEGNO"));
					r.setMaterializzazione(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_MATERIALIZZAZIONE"));
					r.setMaterializzazione(resultSet.getString("RICO13017_CORE§INFO_CONTRASSEGNO_MATERIALIZZAZIONE"));
					r.setContrassegno_ancorato(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_CONTRASSEGNO_ANCORATO"));
					r.setContrassegno_ancorato(resultSet.getString("RICO13017_CORE§INFO_VERIFICHE_CONTRASSEGNO_ANCORATO"));
					r.setContrassegno_danneggiato(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_CONTRASSEGNO_DANNEGGIATO"));
					r.setContrassegno_danneggiato(resultSet.getString("RICO13017_CORE§INFO_VERIFICHE_CONTRASSEGNO_DANNEGGIATO"));
					r.setDescrizione_danneggiamento(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_DESCRIZIONE_DANNEGGIAMENTO"));
					r.setDescrizione_danneggiamento(resultSet.getString("RICO13017_CORE§INFO_VERIFICHE_DESCRIZIONE_DANNEGGIAMENTO"));
					r.setTipologia_fondazione(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_TIPOLOGIA_FONDAZIONE"));
					r.setTipologia_fondazione(resultSet.getString("RICO13017_CORE§INFO_MANUFATTO_TIPOLOGIA_FONDAZIONE"));
					r.setAnomalie_manufatto(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ANOMALIE_MANUFATTO"));
					r.setAnomalie_manufatto(resultSet.getString("RICO13017_CORE§INFO_MANUFATTO_2_ANOMALIE_MANUFATTO"));
					r.setDescrizione_anomalie(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_DESCRIZIONE_ANOMALIE"));
					r.setDescrizione_anomalie(resultSet.getString("RICO13017_CORE§INFO_MANUFATTO_3_DESCRIZIONE_ANOMALIE"));
					r.setNote_rilevanti(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_NOTE_RILEVANTI"));
					r.setNote_rilevanti(resultSet.getString("RICO13017_CORE§INFO_MANUFATTO_4_NOTE_RILEVANTI"));
					r.setDescr_note_rilevanti(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_DESCR_NOTE_RILEVANTI"));
					r.setDescr_note_rilevanti(resultSet.getString("RICO13017_INFO_MANUFATTO_5_DESCR_NOTE_RILEVANTI§VALUE"));
					r.setAltre_note_rilevanti(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ALTRE_NOTE_RILEVANTI"));
					r.setAltre_note_rilevanti(resultSet.getString("RICO13017_CORE§INFO_MANUFATTO_5_ALTRE_NOTE_RILEVANTI"));
					r.setTipo_contesto_ambientale(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_TIPO"));
					r.setTipo_contesto_ambientale(resultSet.getString("RICO13017_CORE§CONTESTO_AMBIENTALE_TIPO"));
					r.setAltro_tipo_contesto_ambientale(resultSet.getString("RICO13017_CORE§ID_CAPOSALDO_PREC_ALTRO_TIPO"));
					r.setAltro_tipo_contesto_ambientale(resultSet.getString("RICO13017_CORE§CONTESTO_AMBIENTALE_ALTRO_TIPO"));

					r.setLatitude(resultSet.getDouble("RICO13017_CORE§COORDINATE_LAT"));
					r.setLongitude(resultSet.getDouble("RICO13017_CORE§COORDINATE_LNG"));
					r.setAltitude(resultSet.getDouble("RICO13017_CORE§COORDINATE_ALT"));
					r.setAccuracy(resultSet.getDouble("RICO13017_CORE§COORDINATE_ACC"));

					byteArray = resultSet.getBytes("RICO13017_IMMAGINI_FOTO_MANUFATTO_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_manufatto(image(r, "manufatto", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}
					byteArray = resultSet.getBytes("RICO13017_IMMAGINI_2_FOTO_PANORAMICA_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_panoramica(image(r, "panoramica", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}

					// Aggiunte 2017
					r.setEsistente(resultSet.getString("RICO13017_CORE§INFO_STATO_ESISTENTE"));
					r.setStato_scomparso(resultSet.getString("RICO13017_CORE§INFO_STATO_SCOMPARSO_STATO_CS"));
					r.setNote_scomparso(resultSet.getString("RICO13017_CORE§INFO_STATO_SCOMPARSO_NOTE_SCOMPARSO"));
					r.setAltre_scomparso(resultSet.getString("RICO13017_CORE§INFO_STATO_SCOMPARSO_ALTRE_CAUSE"));
					r.setNecessita_ripristino(resultSet.getString("RICO13017_CORE§INFO_STATO_SCOMPARSO_NECESSITA_RIPRISTINO"));
					r.setDescrizione_ripristino(resultSet.getString("RICO13017_CORE§RIPRISTINO_DESCRIZIONE_RIPRISTINO"));
					r.setLatitude_ripristino(resultSet.getDouble("RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_LAT"));
					r.setLongitude_ripristino(resultSet.getDouble("RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_LNG"));
					r.setAltitude_ripristino(resultSet.getDouble("RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_ALT"));
					r.setAccuracy_ripristino(resultSet.getDouble("RICO13017_CORE§RIPRISTINO_2_COORDINATE_RIPRISTINO_ACC"));

					byteArray = resultSet.getBytes("RICO13017_IMMGN_VVNUTA_RCGNZNE_FOTO_AGGIORNATA_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_aggiornata(image(r, "aggiornata", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}
					byteArray = resultSet.getBytes("RICO13017_INFO_MANUFATTO_3_FOTO_DANNO_MANUFATTO_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_danno_manufatto(image(r, "danno_manufatto", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}
					byteArray = resultSet.getBytes("RICO13017_INFO_VERIFICHE_FOTO_DANNO_CONTRASSEGNO_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_danno_contrassegno(image(r, "danno_contrassegno", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}
					byteArray = resultSet.getBytes("RICO13017_RIPRISTINO_2_FOTO_SITO_PROPOSTO_BLB§VALUE");
					if(byteArray != null && byteArray.length > 0) {
						r.setFoto_sito_ripristino(image(r, "sito_ripristino", byteArray, photoMaxWidth, photoMaxHeight, basePath, user, pass));
					}

					r.setModifica(null);

					ResteasyClient client = new ResteasyClientBuilder().build();
					ResteasyWebTarget target = client.target(remoteApiBaspath + "/ricognizioni/insert");
					Entity<Ricognizione> e = Entity.entity(r, MediaType.APPLICATION_JSON);
					Response response = target.request().post(e);

					if (response.getStatus() != 200) {
						throw new Exception("Failed : HTTP error code : "+ response.getStatus());
					}

					String responseString = response.readEntity(String.class);
					if(Boolean.parseBoolean(responseString) == false)
						throw new Exception("Failed: response=["+responseString+"]");

					logger.info("Done instance "+instanceID);
					connection.createStatement().executeUpdate("insert into load_log ( uuid ) values ('"+r.getInstanceID()+"')");
				}
				try { resultSet.close(); } catch(Exception i) { }
				try { statement.close(); } catch(Exception i) { }
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try { connection.close(); } catch(Exception i) { }
		}
		logger.info("End executing");
	}
	
	private static final SimpleDateFormat sdfYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private String image(Ricognizione r, String fotoType, byte[] imageInByte, int photoMaxWidth, int photoMaxHeight, String basePath, String user, String pass) throws Exception {
		String filename = r.getId()+"_"+(r.getInizio() == null ? "000000" : sdfYYYMMDD.format(r.getInizio()))+"_"+fotoType+".jpg";
		File file = File.createTempFile("tmp_"+filename, ".jpg");

		logger.info("Resizing and sending image "+filename);
		try {
			Photo.imageToFile(imageInByte, file, photoMaxWidth, photoMaxHeight);
			Remote.sendFile(basePath + filename, user, pass, file);
		} finally {
			file.delete();
			file.deleteOnExit();
		}
		return filename;
	}
}
