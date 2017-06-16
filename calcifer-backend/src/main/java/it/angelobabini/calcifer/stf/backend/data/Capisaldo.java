package it.angelobabini.calcifer.stf.backend.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity(name="capisaldo")
public class Capisaldo implements Serializable {
	private static final long serialVersionUID = -5765611045093801024L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long kint;

	@Column(name = "id", length = 20, nullable = false)
	private String id; //id

	@Column(name = "id_in_altre_reti", length = 40, nullable = false)
	private String id_in_altre_reti; //id_in_altre_reti

	@Column(name = "id_caposaldo_principale", length = 40, nullable = false)
	private String id_caposaldo_principale; //id_caposaldo_principale

	@Column(name = "latitude", nullable = false)
	private double latitude; //coordinate

	@Column(name = "longitude", nullable = false)
	private double longitude; //coordinate

	@Column(name = "altitude", nullable = false)
	private double altitude; //coordinate

	@Column(name = "accuracy", nullable = false)
	private double accuracy; //coordinate

	@Column(name = "ubicazione", length = 500, nullable = false)
	private String ubicazione; //ubicazione

	@Column(name = "indirizzo", length = 250, nullable = false)
	private String indirizzo; //indirizzo

	@Column(name = "accesso", length = 250, nullable = false)
	private String accesso; //accesso
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("inizio ASC")
	private List<Ricognizione> ricognizioniList;
	
	/*@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Ricognizione primaRicognizione;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Ricognizione ultimaRicognizione;*/
	
	
	public long getKint() {
		return kint;
	}
	/*public void setKint(long kint) {
		this.kint = kint;
	}*/
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
		this.id_in_altre_reti = id_in_altre_reti;
	}
	public String getId_caposaldo_principale() {
		return id_caposaldo_principale;
	}
	public void setId_caposaldo_principale(String id_caposaldo_principale) {
		this.id_caposaldo_principale = id_caposaldo_principale;
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
		this.ubicazione = ubicazione;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getAccesso() {
		return accesso;
	}
	public void setAccesso(String accesso) {
		this.accesso = accesso;
	}
	public List<Ricognizione> getRicognizioniList() {
		if(ricognizioniList == null)
			ricognizioniList = new ArrayList<Ricognizione>(1);
		return ricognizioniList;
	}
	public void setRicognizioniList(List<Ricognizione> ricognizioniList) {
		this.ricognizioniList = ricognizioniList;
	}

	public Timestamp getPrimaRicognizione() {
		if(ricognizioniList == null || ricognizioniList.size() == 0)
			return null;
		
		Timestamp ret = null;
		for(Ricognizione r : ricognizioniList) {
			if(r != null) {
				if(ret == null || (r.getInizio() != null && r.getInizio().before(ret)))
					ret = r.getInizio();
			}
		}
		return ret;
	}
	
	public Timestamp getUltimaRicognizione() {
		if(ricognizioniList == null || ricognizioniList.size() == 0)
			return null;
		
		Timestamp ret = null;
		for(Ricognizione r : ricognizioniList) {
			if(r != null) {
				if(ret == null || (r.getInizio() != null && r.getInizio().after(ret)))
					ret = r.getInizio();
			}
		}
		return ret;
	}
}
