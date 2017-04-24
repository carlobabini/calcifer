package it.angelobabini.calcifer.stf.backend.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class RicognizioneOld {

	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private Timestamp inizio;
	private Timestamp fine;
	private String operatore;
	private String id;
	private String id_in_altre_reti;
	private String id_caposaldo_principale;
	private double latitude;
	private double longitude;
	private double altitude;
	private double accuracy;
	private String ubicazione;
	private String indirizzo;
	private String accesso;
	private String posizione_contrassegno;
	private String materializzazione;
	private String contrassegno_ancorato;
	private String contrassegno_danneggiato;
	private String descrizione_danneggiamento;
	private String tipologia_fondazione;
	private String anomalie_manufatto;
	private String descrizione_anomalie;
	private String note_rilevanti;
	private String descr_note_rilevanti;
	private String altre_note_rilevanti;
	private String tipo;
	private String altro_tipo;
	private byte[] foto_manufatto;
	private byte[] foto_panoramica;
	private String instanceID;
	private Timestamp modifica;
	private String appartenenza = "";
	private int affidabilita = -1;
	
	public Timestamp getInizio() {
		return inizio;
	}
	public void setInizio(Timestamp inizio) {
		this.inizio = inizio;
	}
	public String getInizioString() {
		if(inizio!=null) {
			return dateFormatter.format(inizio);
		}
		return "";
	}
	public Timestamp getFine() {
		return fine;
	}
	public void setFine(Timestamp fine) {
		this.fine = fine;
	}
	public String getFineString() {
		if(fine!=null) {
			return dateFormatter.format(fine);
		}
		return "";
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo!=null ? tipo : "";
	}
	public String getAltro_tipo() {
		return altro_tipo;
	}
	public void setAltro_tipo(String altro_tipo) {
		this.altro_tipo = altro_tipo!=null ? altro_tipo : "";
	}
	public byte[] getFoto_manufatto() {
		return foto_manufatto;
	}
	public void setFoto_manufatto(byte[] foto_manufatto) {
		this.foto_manufatto = foto_manufatto;
	}
	public byte[] getFoto_panoramica() {
		return foto_panoramica;
	}
	public void setFoto_panoramica(byte[] foto_panoramica) {
		this.foto_panoramica = foto_panoramica;
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
	public String getModificaString() {
		if(modifica!=null) {
			return dateFormatter.format(modifica);
		}
		return "";
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
	
	public String getFoto_manufattoCode() {
		return "ricognizione^"+id+"^manufatto";
	}
	
	public String getFoto_panoramicaCode() {
		return "ricognizione^"+id+"^panoramica";
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
}
