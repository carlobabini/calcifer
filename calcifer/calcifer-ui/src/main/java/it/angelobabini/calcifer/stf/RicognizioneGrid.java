package it.angelobabini.calcifer.stf;

import org.vaadin.gridutil.cell.GridCellFilter;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.Grid;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

public class RicognizioneGrid extends Grid {
	private static final long serialVersionUID = -7472060043692841054L;
	
	private GridCellFilter filter;
	private JPAContainer<Ricognizione> container = null;

	public RicognizioneGrid(RicognizioniView view) {
		setSizeFull();
        setSelectionMode(SelectionMode.SINGLE);
		setEditorEnabled(true);
		setColumnReorderingAllowed(true);

		this.container = JPAContainerFactory.make(Ricognizione.class, CalciferUI.PERSISTENCE_UNIT_NAME);
		this.container.setAutoCommit(true);
		this.setContainerDataSource(container);
		
		for(Column c : getColumns()) {
			c.setHidable(true);
		}
		filter = new GridCellFilter(this);
		
		setColumnOrder(
				"inizio",
				"fine",
				"operatore",
				"id",
				"id_in_altre_reti",
				"id_caposaldo_principale",
				"appartenenza",
				"affidabilita",
				"latitude",
				"longitude",
				"altitude",
				"accuracy",
				"ubicazione",
				"indirizzo",
				"accesso",
				"posizione_contrassegno",
				"materializzazione",
				"contrassegno_ancorato",
				"contrassegno_danneggiato",
				"descrizione_danneggiamento",
				"tipologia_fondazione",
				"anomalie_manufatto",
				"descrizione_anomalie",
				"note_rilevanti",
				"descr_note_rilevanti",
				"altre_note_rilevanti",
				"tipo_contesto_ambientale",
				"altro_tipo_contesto_ambientale",
				"foto_manufatto",
				"foto_panoramica",
				"instanceID",
				"modifica",
				"esistente",
				"stato_scomparso",
				"note_scomparso",
				"altre_scomparso",
				"necessita_ripristino",
				"descrizione_ripristino",
				"latitude_ripristino",
				"longitude_ripristino",
				"altitude_ripristino",
				"accuracy_ripristino"
		);
		
		getColumn("inizio").setHeaderCaption("Inizio").setHidable(false);
		filter.setDateFilter("inizio");
		
		getColumn("fine").setHeaderCaption("Fine");
		
		getColumn("operatore").setHeaderCaption("Operatore");
		filter.setTextFilter("operatore", true, false);
		
		getColumn("id").setHeaderCaption("ID");
		filter.setTextFilter("id", true, false);
		
		getColumn("id_in_altre_reti").setHeaderCaption("ID altre reti");
		filter.setTextFilter("id_in_altre_reti", true, false);
		
		getColumn("id_caposaldo_principale").setHeaderCaption("ID principale");
		filter.setTextFilter("id_caposaldo_principale", true, false);
		
		getColumn("appartenenza").setHeaderCaption("Appartenenza");
		filter.setTextFilter("appartenenza", true, false);
		
		getColumn("affidabilita").setHeaderCaption("Affidabilità");
		filter.setTextFilter("affidabilita", true, false);

		getColumn("latitude").setHeaderCaption("Latitudine");
		getColumn("longitude").setHeaderCaption("Longitudine");
		getColumn("altitude").setHeaderCaption("Altitudine");
		getColumn("accuracy").setHeaderCaption("Accuracy");
		
		getColumn("ubicazione").setHeaderCaption("Ubicazione");
		filter.setTextFilter("ubicazione", true, false);
		
		getColumn("indirizzo").setHeaderCaption("Indirizzo");
		filter.setTextFilter("indirizzo", true, false);
		
		getColumn("accesso").setHeaderCaption("Accesso").setHidable(true);
		filter.setTextFilter("accesso", true, false);
		
		getColumn("posizione_contrassegno").setHeaderCaption("Posizione");
		filter.setTextFilter("posizione_contrassegno", true, false);
		
		getColumn("materializzazione").setHeaderCaption("Materializzazione");
		filter.setTextFilter("materializzazione", true, false);
		
		getColumn("contrassegno_ancorato").setHeaderCaption("Ancorato");
		filter.setTextFilter("contrassegno_ancorato", true, false);
		
		getColumn("contrassegno_danneggiato").setHeaderCaption("Danneggiato");
		filter.setTextFilter("contrassegno_danneggiato", true, false);
		
		getColumn("descrizione_danneggiamento").setHeaderCaption("Descrizione danneggiamento");
		filter.setTextFilter("descrizione_danneggiamento", true, false);
		
		getColumn("tipologia_fondazione").setHeaderCaption("Fondazione");
		filter.setTextFilter("tipologia_fondazione", true, false);
		
		getColumn("anomalie_manufatto").setHeaderCaption("Anomalie");
		filter.setTextFilter("anomalie_manufatto", true, false);
		
		getColumn("descrizione_anomalie").setHeaderCaption("Descrizione anomalie");
		filter.setTextFilter("descrizione_anomalie", true, false);
		
		getColumn("note_rilevanti").setHeaderCaption("Note");
		filter.setTextFilter("note_rilevanti", true, false);
		
		getColumn("descr_note_rilevanti").setHeaderCaption("Descrizione note");
		filter.setTextFilter("descr_note_rilevanti", true, false);
		
		getColumn("altre_note_rilevanti").setHeaderCaption("Altre note");
		filter.setTextFilter("altre_note_rilevanti", true, false);
		
		getColumn("tipo_contesto_ambientale").setHeaderCaption("Contesto ambientale");
		filter.setTextFilter("tipo_contesto_ambientale", true, false);
		
		getColumn("altro_tipo_contesto_ambientale").setHeaderCaption("Altro contesto");
		filter.setTextFilter("altro_tipo_contesto_ambientale", true, false);
		
		getColumn("foto_manufatto").setHeaderCaption("Foto manufatto");
		getColumn("foto_panoramica").setHeaderCaption("Foto panoramica");
		
		getColumn("instanceID").setHeaderCaption("Istanza");
		filter.setTextFilter("instanceID", true, false);
		
		getColumn("modifica").setHeaderCaption("Ultima modifica");
		filter.setDateFilter("modifica");

		getColumn("esistente").setHeaderCaption("Esistente");
		filter.setTextFilter("esistente", true, false);

		getColumn("stato_scomparso").setHeaderCaption("Stato scomparso");
		filter.setTextFilter("stato_scomparso", true, false);
		
		getColumn("note_scomparso").setHeaderCaption("Note scomparso");
		filter.setTextFilter("note_scomparso", true, false);
		
		getColumn("altre_scomparso").setHeaderCaption("Altre scomparso");
		filter.setTextFilter("altre_scomparso", true, false);
		
		getColumn("necessita_ripristino").setHeaderCaption("Necessita ripristino");
		filter.setTextFilter("necessita_ripristino", true, false);
		
		getColumn("descrizione_ripristino").setHeaderCaption("Descrizione ripristino");
		filter.setTextFilter("descrizione_ripristino", true, false);

		getColumn("latitude_ripristino").setHeaderCaption("Latitudine ripristino");
		getColumn("longitude_ripristino").setHeaderCaption("Longitudine ripristino");
		getColumn("altitude_ripristino").setHeaderCaption("Altitudine ripristino");
		getColumn("accuracy_ripristino").setHeaderCaption("Accuracy ripristino");
	}
	
	public JPAContainer<Ricognizione> getContainer() {
		return container;
	}
}
