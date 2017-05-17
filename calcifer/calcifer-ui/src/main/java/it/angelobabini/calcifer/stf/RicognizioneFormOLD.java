package it.angelobabini.calcifer.stf;

import java.util.Date;

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class RicognizioneFormOLD extends FormLayout {
	private static final long serialVersionUID = 8434108421302157224L;
	
	private final DateField inizioField = new DateField("Inizio");
	private final DateField fineField = new DateField("Fine");
	private final ComboBox operatoreField = new ComboBox("Operatore");
	private final TextField idField = new TextField("ID Caposaldo");
	private final TextField id_in_altre_retiField = new TextField("ID Altre reti");
	/*id_caposaldo_principale character varying(40),
	  latitude numeric(15,10),
	  longitude numeric(15,10),
	  altitude numeric(15,10),
	  accuracy numeric(15,10),
	  ubicazione character varying(500),
	  indirizzo character varying(250),
	  accesso character varying(250),
	  posizione_contrassegno character varying(500),
	  materializzazione character varying(100),
	  contrassegno_ancorato character varying(10),
	  contrassegno_danneggiato character varying(10),
	  descrizione_danneggiamento character varying(500),
	  tipologia_fondazione character varying(40),
	  anomalie_manufatto character varying(10),
	  descrizione_anomalie character varying(500),
	  note_rilevanti character varying(10),
	  descr_note_rilevanti character varying(500),
	  altre_note_rilevanti character varying(500),
	  tipo character varying(100),
	  altro_tipo character varying(500),
	  foto_manufatto character varying(100),
	  foto_panoramica character varying(100),
	  instanceid character varying(50) NOT NULL,
	  modifica timestamp with time zone,
	  appartenenza character varying(3),
	  punto geometry,
	  affidabilita integer NOT NULL DEFAULT (-1),
	  esistente character varying(10),
	  stato_scomparso character varying(500),
	  note_scomparso character varying(500),
	  altre_scomparso character varying(500),
	  necessita_ripristino character varying(10),
	  descrizione_ripristino character varying(500),
	  latitude_ripristino numeric(15,10),
	  longitude_ripristino numeric(15,10),
	  altitude_ripristino numeric(15,10),
	  accuracy_ripristino numeric(15,10),
	  foto_sito_ripristino character varying(100),
	  foto_aggiornata character varying(100),
	  foto_danno_contrassegno character varying(100),
	  foto_danno_manufatto character varying(100),*/

	public RicognizioneFormOLD() {
		super();
        addStyleName("product-form");
        setSizeFull();
        
        addComponent(inizioField);
        addComponent(fineField);
        addComponent(operatoreField);
        addComponent(idField);
        addComponent(id_in_altre_retiField);
	}
	
	public void editItem(Item item) {
		//TODO what?
		inizioField.setValue(item==null ? null : (Date)item.getItemProperty("inizio").getValue());
		fineField.setValue(item==null ? null : (Date)item.getItemProperty("fine").getValue());
		idField.setValue(item==null ? null : String.valueOf(item.getItemProperty("id").getValue()));
	}
}
