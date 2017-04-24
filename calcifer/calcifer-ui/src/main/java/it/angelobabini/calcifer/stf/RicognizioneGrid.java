package it.angelobabini.calcifer.stf;

import java.sql.SQLException;

import org.vaadin.gridutil.cell.GridCellFilter;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer;
import org.vaadin.gridutil.renderer.EditDeleteButtonValueRenderer.EditDeleteButtonClickListener;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;

public class RicognizioneGrid extends Grid {
	private static final long serialVersionUID = -7472060043692841054L;
	
	private final GridCellFilter filter;

	public RicognizioneGrid() {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);
		setEditorEnabled(false);
		setColumnReorderingAllowed(true);
		
		filter = new GridCellFilter(this);
	}
	
	public void initGrid() {	
		getColumn("inizio").setHeaderCaption("Inizio");
		
		getColumn("fine").setHeaderCaption("Fine");
		
		getColumn("operatore").setHeaderCaption("Operatore");
		
		getColumn("id").setHeaderCaption("ID");
		filter.setTextFilter("id", true, false);
		
		getColumn("id_in_altre_reti").setHeaderCaption("ID altre reti");
		filter.setTextFilter("id_in_altre_reti", true, false);
		
		getColumn("id_caposaldo_principale").setHeaderCaption("ID principale");
		filter.setTextFilter("", true, false);
		
		getColumn("appartenenza").setHeaderCaption("Appartenenza");
		filter.setTextFilter("", true, false);
		
		getColumn("affidabilita").setHeaderCaption("Affidabilità");

		getColumn("latitude").setHeaderCaption("Latitudine");
		getColumn("longitude").setHeaderCaption("Longitudine");
		getColumn("altitude").setHeaderCaption("Altitudine");
		getColumn("accuracy").setHeaderCaption("Accuracy");
		
		getColumn("ubicazione").setHeaderCaption("Ubicazione");
		filter.setTextFilter("ubicazione", true, false);
		
		getColumn("indirizzo").setHeaderCaption("Indirizzo");
		filter.setTextFilter("indirizzo", true, false);
		
		getColumn("accesso").setHeaderCaption("Accesso");
		filter.setTextFilter("accesso", true, false);
		
		getColumn("posizione_contrassegno").setHeaderCaption("Posizione");
		filter.setTextFilter("posizione_contrassegno", true, false);
		
		getColumn("materializzazione").setHeaderCaption("Materializzazione");
		getColumn("contrassegno_ancorato").setHeaderCaption("Ancorato");
		getColumn("contrassegno_danneggiato").setHeaderCaption("Danneggiato");
		getColumn("descrizione_danneggiamento").setHeaderCaption("Descrizione danneggiamento");
		filter.setTextFilter("descrizione_danneggiamento", true, false);
		
		getColumn("tipologia_fondazione").setHeaderCaption("Fondazione");
		getColumn("anomalie_manufatto").setHeaderCaption("Anomalie");
		getColumn("descrizione_anomalie").setHeaderCaption("Descrizione anomalie");
		filter.setTextFilter("descrizione_anomalie", true, false);
		
		getColumn("note_rilevanti").setHeaderCaption("Note");
		getColumn("descr_note_rilevanti").setHeaderCaption("Descrizione note");
		filter.setTextFilter("descr_note_rilevanti", true, false);
		
		getColumn("altre_note_rilevanti").setHeaderCaption("Altre note");
		filter.setTextFilter("altre_note_rilevanti", true, false);
		
		getColumn("tipo").setHeaderCaption("Contesto ambientale");
		getColumn("altro_tipo").setHeaderCaption("Altro contesto");
		getColumn("foto_manufatto").setHeaderCaption("Foto manufatto");
		getColumn("foto_panoramica").setHeaderCaption("Foto panoramica");
		getColumn("instanceid").setHeaderCaption("istanza");
		getColumn("modifica").setHeaderCaption("Ultima modifica");
		removeColumn("punto");
		
		final Grid grid = this;
		
		/*getColumn("inizio").setRenderer(new EditDeleteButtonValueRenderer(new EditDeleteButtonClickListener() {

			@Override
			public void onEdit(RendererClickEvent event) {
				editItem(event.getItemId());
			}

			@Override
			public void onDelete(RendererClickEvent event) {
				grid.getContainerDataSource().removeItem(event.getItemId());
				try {
					((SQLContainer)grid.getContainerDataSource()).commit();
					((SQLContainer)grid.getContainerDataSource()).refresh();
				} catch (UnsupportedOperationException e) {
					//TODO MainUI.get().notificateException("Errore cancellazione", e);
				} catch (SQLException e) {
					//TODO MainUI.get().notificateException("Errore cancellazione", e);
				}
			}
			
		}));*/
		
		// Intercetto la modifica inline
		getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
			private static final long serialVersionUID = 6012020572584410249L;

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				// do nothing
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				try {
					((SQLContainer)grid.getContainerDataSource()).commit();
				} catch (UnsupportedOperationException e) {
					//TODO MainUI.get().notificateException("Errore cancellazione", e);
				} catch (SQLException e) {
					//TODO MainUI.get().notificateException("Errore cancellazione", e);
				}
				((SQLContainer)grid.getContainerDataSource()).refresh();
			}
		});
	}
}
