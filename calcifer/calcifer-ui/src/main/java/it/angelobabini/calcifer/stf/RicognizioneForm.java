package it.angelobabini.calcifer.stf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.backend.Helper;
import it.angelobabini.calcifer.backend.Setting;
import it.angelobabini.calcifer.stf.backend.data.ImageManager;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;
import it.angelobabini.calcifer.stf.backend.data.StfDAO;

public class RicognizioneForm extends VerticalLayout {
	private static final long serialVersionUID = 8434108421302157224L;

	private EntityItem<Ricognizione> entityItem = null;
	private boolean formChanged = false;

	private final Label titleLabel = new Label("RicognizioneForm");
	private final FormLayout formLayout = new FormLayout();
	//Info sopralluogo
	private final DateField inizioField = new DateField("Inizio");
	private final DateField fineField = new DateField("Fine");
	private final ComboBox operatoreField = new ComboBox("Operatore");
	//Identificazione del caposaldo
	private final TextField idField = new TextField("Nome del caposaldo");
	private final TextField id_in_altre_retiField = new TextField("Nome del caposaldo in altre reti");
	private final TextField id_caposaldo_principaleField = new TextField("Nome del caposaldo principale");
	private final TextField appartenenzaField = new TextField("Appartenenza");
	private final TextField affidabilitaField = new TextField("Affidabilità");
	//Coordinate
	private final TextField latitudineField = new TextField("Latitudine");
	private final TextField longitudineField = new TextField("Longitudine");
	private final TextField altitudineField = new TextField("Altitudine");
	private final TextField accuracyField = new TextField("Accuracy");
	//Informazioni toponomastiche
	private final TextArea ubicazioneField = new TextArea("Ubicazione del manufatto");
	private final TextArea indirizzoField = new TextArea("Indirizzo e località");
	private final TextArea accessoField = new TextArea("Informazioni utili per accedere al caposaldo");
	//Informazioni sul contrassegno
	private final TextArea posizione_contrassegnoField = new TextArea("Posizione contrassegno");
	private final ComboBox materializzazioneField = new ComboBox("Materializzazione");
	private final ComboBox contrassegno_ancoratoField = new ComboBox("Contrassegno saldamente ancorato al manufatto?");
	private final ComboBox contrassegno_danneggiatoField = new ComboBox("Contrassegno danneggiato?");
	private final TextArea descrizione_danneggiamentoField = new TextArea("Descrizione dell'eventuale danneggiamento");
	//Informazioni sul manufatto
	private final ComboBox tipologia_fondazioneField = new ComboBox("Tipologia di fondazione");
	private final ComboBox anomalie_manufattoField = new ComboBox("Anomalie manufatto");
	private final TextArea descrizione_anomalieField = new TextArea("Descrizione dell'eventuale anomalia");
	private final ComboBox note_rilevantiField = new ComboBox("Il manufatto può essere compromesso?");
	private final ComboBox descr_note_rilevantiField = new ComboBox("Specifica tale condizione");
	private final TextArea altre_note_rilevantiField = new TextArea("Descrivi la condizione");
	//Contesto ambientale
	private final ComboBox tipo_contesto_ambientaleField = new ComboBox("Contesto ambientale");
	private final TextArea altro_tipo_contesto_ambientaleField = new TextArea("Descrizione contesto");
	//Caposaldo utilizzabile
	private final ComboBox esistenteField = new ComboBox("Caposaldo utilizzabile?");
	private final ComboBox stato_scomparsoField = new ComboBox("Condizione inutilizzabilità");
	private final ComboBox note_scomparsoField = new ComboBox("Specificare la causa");
	private final TextArea altre_scomparsoField = new TextArea("Descrivere la causa");
	//Ripristino
	private final ComboBox necessita_ripristinoField = new ComboBox("Si suggerisce il ripristino?");
	private final TextArea descrizione_ripristinoField = new TextArea("Descrivere materializzazione e sito proposti");
	private final TextField latitudine_ripristinoField = new TextField("Latitudine ripristino");
	private final TextField longitudine_ripristinoField = new TextField("Longitudine ripristino");
	private final TextField altitudine_ripristinoField = new TextField("Altitudine ripristino");
	private final TextField accuracy_ripristinoField = new TextField("Accuracy ripristino");
	private Image imageManufattoField = new Image("Manufatto");
	private Image imagePanoramicaField = new Image("Panoramica");
	private Image imageAggiornataField = new Image("Aggiornata");
	private Image imageDanno_contrassegnoField = new Image("Danno contrassegno");
	private Image imageDanno_manufattoField = new Image("Danno manufatto");
	private Image imageSito_ripristinoField = new Image("Sito ripristino");
	//Meta
	private final TextField instanceidField = new TextField("UUID");
	private final DateField modificaField = new DateField("Ultima modifica");

	private final Button saveButton = new Button("Salva");
	private final Button deleteButton = new Button("Elimina");
	private final Button cancelButton = new Button("Annulla");

	public RicognizioneForm(final RicognizioniView view) {
		super();
		addStyleName("product-form");
		addStyleName("product-form-wrapper");
		setResponsive(true);

		titleLabel.setWidth(100, Unit.PERCENTAGE);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);
		titleLabel.addStyleName(ValoTheme.LABEL_HUGE);

		formLayout.setSizeFull();
		//Info sopralluogo
		formLayout.addComponent(new Label("Info sopralluogo"));
		formLayout.addComponent(inizioField);
		inizioField.setEnabled(false);
		inizioField.setResolution(Resolution.MINUTE);

		formLayout.addComponent(fineField);
		fineField.setEnabled(false);
		fineField.setResolution(Resolution.MINUTE);

		formLayout.addComponent(operatoreField);
		operatoreField.setTextInputAllowed(false);
		operatoreField.addItems(StfDAO.operatoriList());

		//Identificazione del caposaldo
		formLayout.addComponent(new Label("Identificazione del caposaldo"));
		formLayout.addComponent(idField);

		formLayout.addComponent(id_in_altre_retiField);
		formLayout.addComponent(id_caposaldo_principaleField);
		formLayout.addComponent(appartenenzaField);
		formLayout.addComponent(affidabilitaField);

		//Coordinate
		formLayout.addComponent(new Label("Coordinate"));
		formLayout.addComponent(latitudineField);
		formLayout.addComponent(longitudineField);
		formLayout.addComponent(altitudineField);
		formLayout.addComponent(accuracyField);

		//Informazioni toponomastiche
		formLayout.addComponent(new Label("Informazioni toponomastiche"));
		formLayout.addComponent(ubicazioneField);
		formLayout.addComponent(indirizzoField);
		formLayout.addComponent(accessoField);

		//Informazioni sul contrassegno
		formLayout.addComponent(new Label("Informazioni sul contrassegno"));
		formLayout.addComponent(posizione_contrassegnoField);
		formLayout.addComponent(materializzazioneField);
		materializzazioneField.addItems("", "bullone", "borchia", "chiusino_contrassegno_interno","chiusino_contrassegno_esterno","centrino_topografico","segno");

		formLayout.addComponent(contrassegno_ancoratoField);
		contrassegno_ancoratoField.addItems("", "si", "no");

		formLayout.addComponent(contrassegno_danneggiatoField);
		contrassegno_danneggiatoField.addItems("", "si", "no");

		formLayout.addComponent(descrizione_danneggiamentoField);

		//Informazioni sul manufatto
		formLayout.addComponent(new Label("Informazioni sul manufatto"));

		formLayout.addComponent(tipologia_fondazioneField);
		tipologia_fondazioneField.addItems("", "profonda", "incerta", "superficiale");

		formLayout.addComponent(anomalie_manufattoField);
		anomalie_manufattoField.addItems("", "si", "no");

		formLayout.addComponent(descrizione_anomalieField);

		formLayout.addComponent(note_rilevantiField);
		note_rilevantiField.addItems("", "si", "no");

		formLayout.addComponent(descr_note_rilevantiField);
		descr_note_rilevantiField.addItems("","piante_con_radici_superficiali", "manufatto_sottoposto_al_transito", "spinta_del_terreno_agente", "urti", "altro");
		formLayout.addComponent(altre_note_rilevantiField);

		//Contesto ambientale
		formLayout.addComponent(new Label("Contesto ambientale"));

		formLayout.addComponent(tipo_contesto_ambientaleField);
		tipo_contesto_ambientaleField.addItems("","argine_fiume_o_canale","prossimità_argine","spiaggia","terreno_sabbioso","zona_calanchiva","altro");
		formLayout.addComponent(altro_tipo_contesto_ambientaleField);

		//Caposaldo utilizzabile
		formLayout.addComponent(new Label("Caposaldo utilizzabile"));

		formLayout.addComponent(esistenteField);
		esistenteField.addItems("", "SI", "NO");

		formLayout.addComponent(stato_scomparsoField);
		stato_scomparsoField.addItems("", "SCOMPARSO", "INACCESSIBILE", "NON_RINTRACCIATO", "ABBANDONATO");

		formLayout.addComponent(note_scomparsoField);
		note_scomparsoField.addItems("","DEMOLITO","ASPORTATO","CANTIERE","RECINTATO", "ALTRO");

		formLayout.addComponent(altre_scomparsoField);

		//Ripristino
		formLayout.addComponent(new Label("Ripristino"));

		formLayout.addComponent(necessita_ripristinoField);
		necessita_ripristinoField.addItems("", "SI", "NO");

		formLayout.addComponent(descrizione_ripristinoField);
		formLayout.addComponent(latitudine_ripristinoField);
		formLayout.addComponent(longitudine_ripristinoField);
		formLayout.addComponent(altitudine_ripristinoField);
		formLayout.addComponent(accuracy_ripristinoField);

		//Immagini
		formLayout.addComponent(new Label("Immagini"));
		formLayout.addComponent(imageManufattoField);
		imageManufattoField.setId("manufatto");
		formLayout.addComponent(imagePanoramicaField);
		imagePanoramicaField.setId("panoramica");
		formLayout.addComponent(imageAggiornataField);
		imageAggiornataField.setId("aggiornata");
		formLayout.addComponent(imageDanno_contrassegnoField);
		imageDanno_contrassegnoField.setId("danno_contrassegno");
		formLayout.addComponent(imageDanno_manufattoField);
		imageDanno_manufattoField.setId("danno_manufatto");
		formLayout.addComponent(imageSito_ripristinoField);
		imageSito_ripristinoField.setId("sito_ripristino");

		//Meta
		formLayout.addComponent(new Label("Meta"));
		formLayout.addComponent(instanceidField);
		instanceidField.setEnabled(false);

		formLayout.addComponent(modificaField);
		modificaField.setEnabled(false);
		modificaField.setResolution(Resolution.MINUTE);

		ValueChangeListener valueListener = new ValueChangeListener() {
			private static final long serialVersionUID = -8248645837076024474L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				formHasChanged();
			}
		};

		for(int i=0; i<formLayout.getComponentCount(); i++) {
			formLayout.getComponent(i).setWidth(100, Unit.PERCENTAGE);
			if(formLayout.getComponent(i) instanceof Label) {
				formLayout.getComponent(i).addStyleName(ValoTheme.LABEL_COLORED);
			}
			if(formLayout.getComponent(i) instanceof TextField) {
				((TextField)formLayout.getComponent(i)).addValueChangeListener(valueListener);
			}
			if(formLayout.getComponent(i) instanceof TextArea) {
				((TextArea)formLayout.getComponent(i)).setRows(3);
				((TextArea)formLayout.getComponent(i)).addValueChangeListener(valueListener);
			}
			if(formLayout.getComponent(i) instanceof DateField) {
				((DateField)formLayout.getComponent(i)).addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
				((DateField)formLayout.getComponent(i)).addValueChangeListener(valueListener);
				((DateField)formLayout.getComponent(i)).setSizeUndefined();
			}
			if(formLayout.getComponent(i) instanceof ComboBox) {
				((ComboBox)formLayout.getComponent(i)).addValueChangeListener(valueListener);
			}
			if(formLayout.getComponent(i) instanceof Image) {
				final Image image = ((Image)formLayout.getComponent(i));
				image.setSizeUndefined();
				/*image.addClickListener(new MouseEvents.ClickListener() {
					private static final long serialVersionUID = 6730791563317659361L;

					@Override
					public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
						showImage(image);
					}

				});*/
			}
		}

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		buttonLayout.addComponent(saveButton);
		buttonLayout.setExpandRatio(saveButton, 2);
		saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		saveButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5439757885433914067L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.saveRicognizione(readItem());
				formClean();
				view.editRicognizione(null);
			}
		});

		buttonLayout.addComponent(cancelButton);
		buttonLayout.setExpandRatio(cancelButton, 2);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5439757885433914067L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(formChanged) {
					/*ConfirmDialog.show(CalciferUI.getCurrent(),
							"Chiusura senza salvataggio",
							"Vuoi chiudere e perdere le modifiche?",
							"Si", "No", new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 4744786090942500378L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								formClean();
								view.editRicognizione(null);
							}
						}
					});*/
					formClean();
					view.editRicognizione(null);
				} else {
					formClean();
					view.editRicognizione(null);
				}
			}
		});

		buttonLayout.addComponent(deleteButton);
		buttonLayout.setExpandRatio(deleteButton, 0);
		deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		deleteButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5439757885433914067L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(CalciferUI.getCurrent(),
						"Eliminazione definitiva",
						"Vuoi davvero eliminare definitivamente?",
						"Si", "No", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 4744786090942500378L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							formClean();
							view.deleteRicognizione(entityItem);
						}
					}
				});
			}
		});

		addComponent(titleLabel);
		addComponent(formLayout);
		addComponent(buttonLayout);
	}

	private void formHasChanged() {
		formChanged = true;
		saveButton.setEnabled(true);
	}

	private void formClean() {
		for(int i=0; i<formLayout.getComponentCount(); i++) {
			if(formLayout.getComponent(i) instanceof Image) {
				final Image image = ((Image)formLayout.getComponent(i));
				image.setSource(null);
				image.setSizeUndefined();
				image.setAlternateText("");
			}
		}
		formChanged = false;
		saveButton.setEnabled(false);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void writeItem(EntityItem<Ricognizione> entityItem) {
		formClean();
		this.entityItem = entityItem;
		if(entityItem == null || entityItem.getEntity() == null) {
			for(int i=0; i<formLayout.getComponentCount(); i++) {
				if(formLayout.getComponent(i) instanceof Field) {
					((Field)formLayout.getComponent(i)).setValue(null);
				}
			}
			titleLabel.setValue("Ricognizione Form");
		} else {
			Ricognizione r = entityItem.getEntity();
			titleLabel.setValue("Ricognizione "+r.getInstanceID());
			putValue(inizioField, r.getInizio());
			putValue(fineField, r.getFine());
			putValue(operatoreField, r.getOperatore());
			putValue(idField, r.getId());
			putValue(id_in_altre_retiField, r.getId_in_altre_reti());
			putValue(id_caposaldo_principaleField, r.getId_caposaldo_principale());
			putValue(appartenenzaField, r.getAppartenenza());
			putValue(affidabilitaField, r.getAffidabilita());
			putValue(latitudineField, r.getLatitude());
			putValue(longitudineField, r.getLongitude());
			putValue(altitudineField, r.getAltitude());
			putValue(accuracyField, r.getAccuracy());
			putValue(ubicazioneField, r.getUbicazione());
			putValue(indirizzoField, r.getIndirizzo());
			putValue(accessoField, r.getAccesso());
			putValue(posizione_contrassegnoField, r.getPosizione_contrassegno());
			putValue(materializzazioneField, r.getMaterializzazione());
			putValue(contrassegno_ancoratoField, r.getContrassegno_ancorato());
			putValue(contrassegno_danneggiatoField, r.getContrassegno_danneggiato());
			putValue(descrizione_danneggiamentoField, r.getDescrizione_danneggiamento());
			putValue(tipologia_fondazioneField, r.getTipologia_fondazione());
			putValue(anomalie_manufattoField, r.getAnomalie_manufatto());
			putValue(descrizione_anomalieField, r.getDescrizione_anomalie());
			putValue(note_rilevantiField, r.getNote_rilevanti());
			putValue(descr_note_rilevantiField, r.getDescr_note_rilevanti());
			putValue(altre_note_rilevantiField, r.getAltre_note_rilevanti());
			putValue(tipo_contesto_ambientaleField, r.getTipo_contesto_ambientale());
			putValue(altro_tipo_contesto_ambientaleField, r.getAltro_tipo_contesto_ambientale());
			putValue(esistenteField, r.getEsistente());
			putValue(stato_scomparsoField, r.getStato_scomparso());
			putValue(note_scomparsoField, r.getNote_scomparso());
			putValue(altre_scomparsoField, r.getAltre_scomparso());
			putValue(necessita_ripristinoField, r.getNecessita_ripristino());
			putValue(descrizione_ripristinoField, r.getDescrizione_ripristino());
			putValue(latitudine_ripristinoField, r.getLatitude_ripristino());
			putValue(longitudine_ripristinoField, r.getLongitude_ripristino());
			putValue(altitudine_ripristinoField, r.getAltitude_ripristino());
			putValue(accuracy_ripristinoField, r.getAccuracy_ripristino());
			putValue(imageManufattoField, r.getFoto_manufatto());
			putValue(imagePanoramicaField, r.getFoto_panoramica());
			putValue(imageAggiornataField, r.getFoto_aggiornata());
			putValue(imageDanno_contrassegnoField, r.getFoto_danno_contrassegno());
			putValue(imageDanno_manufattoField, r.getFoto_danno_manufatto());
			putValue(imageSito_ripristinoField, r.getFoto_sito_ripristino());
			putValue(instanceidField, r.getInstanceID());
			putValue(modificaField, r.getModifica());
		}
		formChanged = false;
		String scrollScript = "window.document.getElementById('" + getId() + "').scrollTop = 0;";
		Page.getCurrent().getJavaScript().execute(scrollScript);
	}

	public EntityItem<Ricognizione> readItem() {
		if(formChanged) {
			try {
				Ricognizione r = entityItem.getEntity();
				r.setInizio(new Timestamp(inizioField.getValue().getTime()));
				r.setFine(new Timestamp(fineField.getValue().getTime()));
				r.setOperatore(String.valueOf(operatoreField.getValue()));
				r.setId(idField.getValue());
				r.setId_in_altre_reti(id_in_altre_retiField.getValue());
				r.setId_caposaldo_principale(id_caposaldo_principaleField.getValue());
				r.setLatitude(Double.parseDouble(latitudineField.getValue().replace(',', '.')));
				r.setLongitude(Double.parseDouble(longitudineField.getValue().replace(',', '.')));
				r.setAltitude(Double.parseDouble(altitudineField.getValue().replace(',', '.')));
				r.setAccuracy(Double.parseDouble(accuracyField.getValue().replace(',', '.')));
				r.setUbicazione(ubicazioneField.getValue());
				r.setIndirizzo(indirizzoField.getValue());
				r.setAccesso(accessoField.getValue());
				r.setPosizione_contrassegno(posizione_contrassegnoField.getValue());
				r.setMaterializzazione(String.valueOf(materializzazioneField.getValue()));
				r.setContrassegno_ancorato(String.valueOf(contrassegno_ancoratoField.getValue()));
				r.setContrassegno_danneggiato(String.valueOf(contrassegno_danneggiatoField.getValue()));
				r.setDescrizione_danneggiamento(descrizione_danneggiamentoField.getValue());
				r.setTipologia_fondazione(String.valueOf(tipologia_fondazioneField.getValue()));
				r.setAnomalie_manufatto(String.valueOf(anomalie_manufattoField.getValue()));
				r.setDescrizione_anomalie(descrizione_anomalieField.getValue());
				r.setNote_rilevanti(String.valueOf(note_rilevantiField.getValue()));
				r.setDescr_note_rilevanti(String.valueOf(descr_note_rilevantiField.getValue()));
				r.setAltre_note_rilevanti(altre_note_rilevantiField.getValue());
				r.setTipo_contesto_ambientale(String.valueOf(tipo_contesto_ambientaleField.getValue()));
				r.setAltro_tipo_contesto_ambientale(String.valueOf(altro_tipo_contesto_ambientaleField.getValue()));
				//r.setInstanceid(instanceidField.getValue());
				//r.setModifica(modificaField.getValue());
				r.setAppartenenza(appartenenzaField.getValue());
				r.setEsistente(String.valueOf(esistenteField.getValue()));
				r.setStato_scomparso(String.valueOf(stato_scomparsoField.getValue()));
				r.setNote_scomparso(String.valueOf(note_scomparsoField.getValue()));
				r.setAltre_scomparso(altre_scomparsoField.getValue());
				r.setNecessita_ripristino(String.valueOf(necessita_ripristinoField.getValue()));
				r.setDescrizione_ripristino(descrizione_ripristinoField.getValue());
				r.setLatitude_ripristino(Double.parseDouble(latitudine_ripristinoField.getValue().replace(',', '.')));
				r.setLongitude_ripristino(Double.parseDouble(longitudine_ripristinoField.getValue().replace(',', '.')));
				r.setAltitude_ripristino(Double.parseDouble(altitudine_ripristinoField.getValue().replace(',', '.')));
				r.setAccuracy_ripristino(Double.parseDouble(accuracy_ripristinoField.getValue().replace(',', '.')));
				/*r.setImageManufatto(imageManufattoField.getValue());
				r.setImagePanoramica(imagePanoramicaField.getValue());
				r.setImageAggiornata(imageAggiornataField.getValue());
				r.setImageDanno_contrassegno(imageDanno_contrassegnoField.getValue());
				r.setImageDanno_manufatto(imageDanno_manufattoField.getValue());
				r.setImageSito_ripristino(imageSito_ripristinoField.getValue());*/
			} catch(Exception e) {
				Notification n = new Notification("Errore durante la lettura dei campi", Type.ERROR_MESSAGE);
				n.setDelayMsec(500);
				n.show(getUI().getPage());
				e.printStackTrace();
			}
		}
		return entityItem;
	}

	private void putValue(TextField field, Object value) {
		if(value == null)
			field.setValue("");
		else
			field.setValue(String.valueOf(value));
	}

	private void putValue(TextArea field, Object value) {
		if(value == null)
			field.setValue("");
		else
			field.setValue(String.valueOf(value));
	}

	private void putValue(DateField field, Object value) {
		if(value == null)
			field.setValue(null);
		else
			if(value instanceof Date)
				field.setValue((Date)value);
	}

	private void putValue(ComboBox field, Object value) {
		if(value == null)
			field.setValue(null);
		else {
			for(Object id : field.getItemIds()) {
				if(String.valueOf(id).equalsIgnoreCase(String.valueOf(value))) {
					field.setValue(id);
					break;
				}
			}
		}
	}

	private void putValue(Image image, String imageName) {
		Image replacement = new Image(image.getCaption());
		replacement.setId(image.getId());
		
		if(imageName == null || imageName.length() == 0) {
			replacement.setSource(null);
			replacement.setWidth(Setting.getAsString("PHOTO_THUMB_WIDTH"));
			replacement.setAlternateText("upload");
		} else {
			replacement.setSource(new ExternalResource(Setting.getAsString("PHOTO_BASE_URL") + imageName));
			replacement.setWidth(Setting.getAsString("PHOTO_THUMB_WIDTH"));
			replacement.setHeight(Setting.getAsString("PHOTO_THUMB_HEIGHT"));
			replacement.setAlternateText(imageName);
		}
		
		if(formLayout.getComponentIndex(image) >= 0) {
			formLayout.addComponent(replacement, formLayout.getComponentIndex(image));
			formLayout.removeComponent(image);
			
			switch(image.getId()) {
			case "manufatto" : { imageManufattoField = replacement; break; }
			case "panoramica" : { imagePanoramicaField = replacement; break; }
			case "danno_contrassegno" : { imageDanno_contrassegnoField = replacement; break; }
			case "danno_manufatto" : { imageDanno_manufattoField = replacement; break; }
			case "aggiornata" : { imageAggiornataField = replacement; break; }
			case "sito_ripristino" : { imageSito_ripristinoField = replacement; break; }
			}
		}
		
		replacement.addClickListener(new MouseEvents.ClickListener() {
			private static final long serialVersionUID = 6730791563317659361L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				showImage(replacement);
			}
		});
		
		/*
		boolean needsReplacement = image.getSource() != null;
		Image replacement = needsReplacement ? new Image(image.getCaption()) : image;
		replacement.setId(image.getId());
		
		if(imageName == null || imageName.length() == 0) {
			replacement.setSource(null);
			replacement.setWidth(Setting.getAsString("PHOTO_THUMB_WIDTH"));
			replacement.setAlternateText("upload");
		} else {
			replacement.setSource(new ExternalResource(Setting.getAsString("PHOTO_BASE_URL") + imageName));
			replacement.setWidth(Setting.getAsString("PHOTO_THUMB_WIDTH"));
			replacement.setHeight(Setting.getAsString("PHOTO_THUMB_HEIGHT"));
			replacement.setAlternateText(imageName);
		}
		
		if(needsReplacement) {
			formLayout.addComponent(replacement, formLayout.getComponentIndex(image));
			formLayout.removeComponent(image);
		}
		if(replacement.getListeners(com.vaadin.event.MouseEvents.ClickEvent.class).size() == 0) {
			replacement.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 6730791563317659361L;
	
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					showImage(image);
				}
			});
		}*/
	}

	private void showImage(final Image image) {
		final Window subWindow = new Window("Immagine "+image.getAlternateText());
		subWindow.setSizeFull();
		subWindow.setModal(true);
		VerticalLayout layout = new VerticalLayout();
		subWindow.setContent(layout);
		layout.setSizeFull();

		final Image imageFull = new Image();
		if(image.getSource() != null) {
			imageFull.setSource(image.getSource());
		}
		imageFull.setSizeFull();
		layout.addComponent(imageFull);

		final Upload upload = new Upload("Carica immagine", null);
		upload.setReceiver(new Upload.Receiver() {
			private static final long serialVersionUID = 5625837892202003151L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				try {
					final File tempFile = new File(Helper.tempFolder(), filename);

					// Pulizia dei listener
					for(Object l : upload.getListeners(Upload.FinishedEvent.class)) {
						if(l instanceof Upload.FinishedListener)
							upload.removeFinishedListener((Upload.FinishedListener)l);
					}
					// Listener per il completamento upload
					upload.addFinishedListener(new Upload.FinishedListener() {
						private static final long serialVersionUID = 1561672558167842612L;

						@Override
						public void uploadFinished(Upload.FinishedEvent finishedEvent) {
							String filename = idField.getValue() + "_" + Helper.dateYYYYMMDD(inizioField.getValue()) + "_" + image.getId() + "." 
									+ tempFile.getName().substring(tempFile.getName().lastIndexOf('.') + 1);
							try {
								ImageManager.sendFile(tempFile, filename);
								_refreshPhoto(image, imageFull, filename);
								
								// Chiudo la finestra
								subWindow.close();
							} catch (Exception e) {
								Notification n = new Notification("Errore upload", Type.ERROR_MESSAGE);
								n.setDelayMsec(500);
								n.show(getUI().getPage());
								e.printStackTrace();
							} finally {
								tempFile.deleteOnExit();
								tempFile.delete();
							}
						}
					});

					return new FileOutputStream(tempFile);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		upload.setImmediate(false);
		
		Button clearImageButton = new Button("Elimina immagine");
		clearImageButton.addStyleName(ValoTheme.BUTTON_DANGER);
		clearImageButton.setIcon(FontAwesome.REMOVE);
		clearImageButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2886430423806658730L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(CalciferUI.getCurrent(),
						"Eliminazione definitiva",
						"Vuoi davvero eliminare definitivamente?",
						"Si", "No", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 4744786090942500378L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								String filename = "";
								if(image.getSource() instanceof ExternalResource) {
									filename = ((ExternalResource)image.getSource()).getURL().replaceAll(Setting.getAsString("PHOTO_BASE_URL"), "");
								}
								if(ImageManager.deleteImage(filename)) {
									_refreshPhoto(image, imageFull, null);
								}
								
								// Chiudo la finestra
								subWindow.close();
							} catch (Exception e) {
								Notification n = new Notification("Errore upload", Type.ERROR_MESSAGE);
								n.setDelayMsec(500);
								n.show(getUI().getPage());
								e.printStackTrace();
							}
						}
					}
				});
			}
		});

		HorizontalLayout controls = new HorizontalLayout();
		controls.setWidth(100, Unit.PERCENTAGE);
		controls.addComponent(upload);
		controls.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
		controls.setExpandRatio(upload, 1);
		controls.addComponent(clearImageButton);
		controls.setComponentAlignment(clearImageButton, Alignment.MIDDLE_RIGHT);
		
		layout.addComponent(controls);
		layout.setExpandRatio(imageFull, 2);

		CalciferUI.get().addWindow(subWindow);
	}
	
	private void _refreshPhoto(final Image image, final Image imageFull, String filename) throws Exception {
		switch(image.getId()) {
		case "manufatto" : { entityItem.getEntity().setFoto_manufatto(filename); break; }
		case "panoramica" : { entityItem.getEntity().setFoto_panoramica(filename); break; }
		case "danno_contrassegno" : { entityItem.getEntity().setFoto_danno_contrassegno(filename); break; }
		case "danno_manufatto" : { entityItem.getEntity().setFoto_danno_manufatto(filename); break; }
		case "aggiornata" : { entityItem.getEntity().setFoto_aggiornata(filename); break; }
		case "sito_ripristino" : { entityItem.getEntity().setFoto_sito_ripristino(filename); break; }
		default : throw new Exception("Tipo immagine non trovato: "+image.getId()+" per foto "+filename);
		}
		putValue(imageFull, filename);
		imageFull.setSizeFull();
		putValue(image, filename);
		formHasChanged();
	}

}