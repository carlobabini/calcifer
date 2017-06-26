package it.angelobabini.odkaggregatetools;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Ricognizione implements Serializable {
	private static final long serialVersionUID = -547857491522914222L;

	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private Timestamp inizio; //inizo
	
	private Timestamp fine; //fine

	private String operatore; //operatore

	private String id; //id

	private String id_in_altre_reti; //id_in_altre_reti

	private String id_caposaldo_principale; //id_caposaldo_principale

	private double latitude; //coordinate

	private double longitude; //coordinate

	private double altitude; //coordinate

	private double accuracy; //coordinate

	private String ubicazione; //ubicazione

	private String indirizzo; //indirizzo

	private String accesso; //accesso

	private String posizione_contrassegno; //posizione_contrassegno

	private String materializzazione; //materializzazione

	private String contrassegno_ancorato; //contrassegno_ancorato

	private String contrassegno_danneggiato; //contrassegno_danneggiato

	private String descrizione_danneggiamento; 

	private String tipologia_fondazione; //tipologia_fondazione

	private String anomalie_manufatto; //anomalie_manufatto

	private String descrizione_anomalie; 

	private String note_rilevanti;

	private String descr_note_rilevanti;

	private String altre_note_rilevanti;

	private String tipo_contesto_ambientale; //tipo

	private String altro_tipo_contesto_ambientale; //altro_tipo

	private String foto_manufatto;

	private String foto_panoramica;
	
	private String instanceID;
	
	private Timestamp modifica;

	private String appartenenza = "";

	private int affidabilita = -1;

	private String esistente; //esistente

	private String stato_scomparso; //stato_cs

	private String note_scomparso; //note_scomparso

	private String altre_scomparso; // altre-cause

	private String necessita_ripristino; //necessita_ripristino

	private String descrizione_ripristino; //descrizione_ripristino

	private double latitude_ripristino; //coordinate_ripristino

	private double longitude_ripristino; //coordinate_ripristino

	private double altitude_ripristino; //coordinate_ripristino

	private double accuracy_ripristino; //coordinate_ripristino

	private String foto_sito_ripristino; //foto_sito_proposto

	private String foto_aggiornata; //foto_aggiornata

	private String foto_danno_contrassegno; 

	private String foto_danno_manufatto;
	
	public Timestamp getInizio() {
		return inizio;
	}
	public void setInizio(Timestamp inizio) {
		this.inizio = inizio;
	}
	
	public Timestamp getFine() {
		return fine;
	}
	public void setFine(Timestamp fine) {
		this.fine = fine;
	}
	
	public String getOperatore() {
		return operatore;
	}
	public void setOperatore(String operatore) {
		this.operatore = operatore!=null ? operatore : "";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId_in_altre_reti() {
		return id_in_altre_reti;
	}
	public void setId_in_altre_reti(String id_in_altre_reti) {
		this.id_in_altre_reti = id_in_altre_reti!=null ? id_in_altre_reti : "";
	}
	
	public String getId_caposaldo_principale() {
		return id_caposaldo_principale;
	}
	public void setId_caposaldo_principale(String id_caposaldo_principale) {
		this.id_caposaldo_principale = id_caposaldo_principale!=null ? id_caposaldo_principale : "";
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public String getUbicazione() {
		return ubicazione;
	}
	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione!=null ? ubicazione : "";
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo!=null ? indirizzo : "";
	}
	
	public String getAccesso() {
		return accesso;
	}
	public void setAccesso(String accesso) {
		this.accesso = accesso!=null ? accesso : "";
	}
	
	public String getPosizione_contrassegno() {
		return posizione_contrassegno;
	}
	public void setPosizione_contrassegno(String posizione_contrassegno) {
		this.posizione_contrassegno = posizione_contrassegno!=null ? posizione_contrassegno : "";
	}
	
	public String getMaterializzazione() {
		return materializzazione;
	}
	public void setMaterializzazione(String materializzazione) {
		this.materializzazione = materializzazione!=null ? materializzazione : "";
	}
	
	public String getContrassegno_ancorato() {
		return contrassegno_ancorato;
	}
	public void setContrassegno_ancorato(String contrassegno_ancorato) {
		this.contrassegno_ancorato = contrassegno_ancorato!=null ? contrassegno_ancorato : "";
	}
	
	public String getContrassegno_danneggiato() {
		return contrassegno_danneggiato;
	}
	public void setContrassegno_danneggiato(String contrassegno_danneggiato) {
		this.contrassegno_danneggiato = contrassegno_danneggiato!=null ? contrassegno_danneggiato : "";
	}
	
	public String getDescrizione_danneggiamento() {
		return descrizione_danneggiamento;
	}
	public void setDescrizione_danneggiamento(String descrizione_danneggiamento) {
		this.descrizione_danneggiamento = descrizione_danneggiamento!=null ? descrizione_danneggiamento : "";
	}
	
	public String getTipologia_fondazione() {
		return tipologia_fondazione;
	}
	public void setTipologia_fondazione(String tipologia_fondazione) {
		this.tipologia_fondazione = tipologia_fondazione!=null ? tipologia_fondazione : "";
		calcolaAffidabilita();
	}
	
	public String getAnomalie_manufatto() {
		return anomalie_manufatto;
	}
	public void setAnomalie_manufatto(String anomalie_manufatto) {
		this.anomalie_manufatto = anomalie_manufatto!=null ? anomalie_manufatto : "";
		calcolaAffidabilita();
	}
	
	public String getDescrizione_anomalie() {
		return descrizione_anomalie;
	}
	public void setDescrizione_anomalie(String descrizione_anomalie) {
		this.descrizione_anomalie = descrizione_anomalie!=null ? descrizione_anomalie : "";
	}
	
	public String getNote_rilevanti() {
		return note_rilevanti;
	}
	public void setNote_rilevanti(String note_rilevanti) {
		this.note_rilevanti = note_rilevanti!=null ? note_rilevanti : "";
		calcolaAffidabilita();
	}
	
	public String getDescr_note_rilevanti() {
		return descr_note_rilevanti;
	}
	public void setDescr_note_rilevanti(String descr_note_rilevanti) {
		this.descr_note_rilevanti = descr_note_rilevanti!=null ? descr_note_rilevanti : "";
	}
	
	public String getAltre_note_rilevanti() {
		return altre_note_rilevanti;
	}
	public void setAltre_note_rilevanti(String altre_note_rilevanti) {
		this.altre_note_rilevanti = altre_note_rilevanti!=null ? altre_note_rilevanti : "";
	}
	
	public String getTipo_contesto_ambientale() {
		return tipo_contesto_ambientale;
	}
	public void setTipo_contesto_ambientale(String tipo_contesto_ambientale) {
		this.tipo_contesto_ambientale = tipo_contesto_ambientale!=null ? tipo_contesto_ambientale : "";
	}
	
	public String getAltro_tipo_contesto_ambientale() {
		return altro_tipo_contesto_ambientale;
	}
	public void setAltro_tipo_contesto_ambientale(String altro_tipo_contesto_ambientale) {
		this.altro_tipo_contesto_ambientale = altro_tipo_contesto_ambientale!=null ? altro_tipo_contesto_ambientale : "";
	}
	
	public String getFoto_manufatto() {
		return foto_manufatto;
	}
	public void setFoto_manufatto(String foto_manufatto) {
		this.foto_manufatto = foto_manufatto!=null ? foto_manufatto : "";
	}
	public String getFoto_panoramica() {
		return foto_panoramica;
	}
	public void setFoto_panoramica(String foto_panoramica) {
		this.foto_panoramica = foto_panoramica!=null ? foto_panoramica : "";
	}
	public String getInstanceID() {
		return instanceID;
	}
	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID!=null ? instanceID : "";
	}
	
	public Timestamp getModifica() {
		return modifica;
	}
	public void setModifica(Timestamp modifica) {
		this.modifica = modifica;
	}
	
	public String getAppartenenza() {
		return appartenenza;
	}
	public void setAppartenenza(String appartenenza) {
		this.appartenenza = appartenenza!=null ? appartenenza : "";
	}
	
	@Override
	public String toString() {
		return id+"|"+id_in_altre_reti+"|"+operatore+"|"+ubicazione+"|"+
				(inizio!=null ? Ricognizione.dateFormatter.format(inizio).substring(0,10) : "")+"|"+
				(modifica!=null ? Ricognizione.dateFormatter.format(modifica) : "");
	}
	
	public int calcolaAffidabilita() {
		int moltiplicatore = 0;
		moltiplicatore += ("profonda".equalsIgnoreCase(tipologia_fondazione) ? 1 : 0);
		moltiplicatore += ("incerta".equalsIgnoreCase(tipologia_fondazione) ? 10 : 0);
		moltiplicatore += ("superficiale".equalsIgnoreCase(tipologia_fondazione) ? 100 : 0);
		moltiplicatore += ("si".equalsIgnoreCase(anomalie_manufatto) ? 1000 : 0);
		moltiplicatore += ("si".equalsIgnoreCase(note_rilevanti) ? 10000 : 0);
		switch(moltiplicatore) {
		case 1: { affidabilita = 4; break; }
		case 10001: { affidabilita = 4; break; }
		case 1001: { affidabilita = 3; break; }
		case 11001: { affidabilita = 2; break; }
		case 10: { affidabilita = 3; break; }
		case 1010: { affidabilita = 2; break; }
		case 10010: { affidabilita = 2; break; }
		case 11010: { affidabilita = 0; break; }
		case 100: { affidabilita = 1; break; }
		case 1100: { affidabilita = 0; break; }
		case 10100: { affidabilita = 0; break; }
		case 11100: { affidabilita = 0; break; }
		default:  { affidabilita = -1; break; }
		}
		return affidabilita;
	}
	
	public int getAffidabilita() {
		return affidabilita;
	}
	
	public String getEsistente() {
		return esistente;
	}
	public void setEsistente(String esistente) {
		this.esistente = esistente!=null ? esistente : "";
	}
	
	public String getStato_scomparso() {
		return stato_scomparso;
	}
	public void setStato_scomparso(String stato_scomparso) {
		this.stato_scomparso = stato_scomparso!=null ? stato_scomparso : "";
	}
	
	public String getNote_scomparso() {
		return note_scomparso;
	}
	public void setNote_scomparso(String note_scomparso) {
		this.note_scomparso = note_scomparso!=null ? note_scomparso : "";
	}
	
	public String getAltre_scomparso() {
		return altre_scomparso;
	}
	public void setAltre_scomparso(String altre_scomparso) {
		this.altre_scomparso = altre_scomparso!=null ? altre_scomparso : "";
	}
	
	public String getNecessita_ripristino() {
		return necessita_ripristino;
	}
	public void setNecessita_ripristino(String necessita_ripristino) {
		this.necessita_ripristino = necessita_ripristino!=null ? necessita_ripristino : "";
	}
	
	public String getDescrizione_ripristino() {
		return descrizione_ripristino;
	}
	public void setDescrizione_ripristino(String descrizione_ripristino) {
		this.descrizione_ripristino = descrizione_ripristino!=null ? descrizione_ripristino : "";
	}
	
	public double getLatitude_ripristino() {
		return latitude_ripristino;
	}
	public void setLatitude_ripristino(double latitude_ripristino) {
		this.latitude_ripristino = latitude_ripristino;
	}
	public double getLongitude_ripristino() {
		return longitude_ripristino;
	}
	public void setLongitude_ripristino(double longitude_ripristino) {
		this.longitude_ripristino = longitude_ripristino;
	}
	public double getAltitude_ripristino() {
		return altitude_ripristino;
	}
	public void setAltitude_ripristino(double altitude_ripristino) {
		this.altitude_ripristino = altitude_ripristino;
	}
	public double getAccuracy_ripristino() {
		return accuracy_ripristino;
	}
	public void setAccuracy_ripristino(double accuracy_ripristino) {
		this.accuracy_ripristino = accuracy_ripristino;
	}
	
	public String getFoto_sito_ripristino() {
		return foto_sito_ripristino;
	}
	public void setFoto_sito_ripristino(String foto_sito_ripristino) {
		this.foto_sito_ripristino = foto_sito_ripristino!=null ? foto_sito_ripristino : "";
	}
	
	public String getFoto_aggiornata() {
		return foto_aggiornata;
	}
	public void setFoto_aggiornata(String foto_aggiornata) {
		this.foto_aggiornata = foto_aggiornata!=null ? foto_aggiornata : "";
	}

	public String getFoto_danno_contrassegno() {
		return foto_danno_contrassegno;
	}
	public void setFoto_danno_contrassegno(String foto_danno_contrassegno) {
		this.foto_danno_contrassegno = foto_danno_contrassegno!=null ? foto_danno_contrassegno : "";
	}
	
	public String getFoto_danno_manufatto() {
		return foto_danno_manufatto;
	}
	public void setFoto_danno_manufatto(String foto_danno_manufatto) {
		this.foto_danno_manufatto = foto_danno_manufatto!=null ? foto_danno_manufatto : "";
	}
}
