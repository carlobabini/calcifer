package it.angelobabini.calcifer.stf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.HelperFE;
import it.angelobabini.calcifer.backend.Setting;
import it.angelobabini.calcifer.stf.backend.data.ExportImport;
import it.angelobabini.calcifer.stf.backend.data.ImageManager;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

public class RicognizioniDownloader extends FormLayout {
	private static final long serialVersionUID = 8295185508534978318L;

	final RicognizioniView ricognizioniView;
	final CheckBox precedentDataField = new CheckBox("Dati ricognizioni precedenti");
	final CheckBox precedentImageField = new CheckBox("Foto ricognizioni precedenti");
	final CheckBox exportImagesField = new CheckBox("Esportare immagini");
	final CheckBox urlInLinkField = new CheckBox("URL completo nel campo immagini");

	final Button downloadButton = new Button("Download");
	final BrowserFrame browser = new BrowserFrame("");


	public RicognizioniDownloader(RicognizioniView ricognizioniView) {
		this.ricognizioniView = ricognizioniView;

		this.setMargin(true);
		this.addComponent(precedentDataField);
		this.addComponent(precedentImageField);
		this.addComponent(exportImagesField);
		this.addComponent(urlInLinkField);
		this.addComponent(browser);
		this.addComponent(downloadButton);

		precedentDataField.setDescription("Indica se completare le informazioni anagrafiche sul capisaldo con quelle delle ricognizioni precedenti");
		precedentImageField.setDescription("Indica se includere le foto delle ricognizioni precedenti");
		exportImagesField.setDescription("Indica se scaricare le foto assieme al file excel con i dati, altrimenti le foto saranno raggiungibili tramite il browser");
		urlInLinkField.setDescription("Indica se includere nel nome della foto anche il percorso web, non utilizzabile se le immagini vengono esportate nello zip");
		browser.setId("ricognizioni-downloader-iframe");
		browser.setWidth("0px");
		browser.setHeight("0px");

		exportImagesField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2651539947789013799L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(exportImagesField.getValue()) {
					urlInLinkField.setValue(false);
					urlInLinkField.setEnabled(false);
				} else {
					urlInLinkField.setEnabled(true);
				}
			}
		});

		downloadButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		downloadButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					downloadButton.setEnabled(false);
					daje();
				} catch (Exception e) {
					Notification n = new Notification("Errore esportazione", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
					e.printStackTrace();
				}
			}
		});
		
		precedentDataField.setValue(false);
		precedentImageField.setValue(false);
		exportImagesField.setValue(false);
		urlInLinkField.setValue(false);
	}

	private void daje() throws Exception {
		boolean datiRicognizioniPrecedenti = precedentDataField.getValue();
		boolean fotoRicognizioniPrecedenti = precedentImageField.getValue();
		boolean exportImages = exportImagesField.getValue();
		boolean urlInLink = urlInLinkField.getValue();
		String photoBaseUrl = Setting.getAsString("PHOTO_BASE_URL");
		String baseUrl = exportImages ? "" : photoBaseUrl;

		List<Ricognizione> ricognizioniList = new ArrayList<Ricognizione>();
		for(Object id : ricognizioniView.getContainer().getItemIds()) {
			Ricognizione r = ricognizioniView.getContainer().getItem(id).getEntity();
			ricognizioniList.add(r);
			// TODO eventualmente filtrare qui
		}

		File xlsxFile = null;
		CloseableHttpClient httpClient = null;

		try {
			xlsxFile = File.createTempFile("ExportRicognizioni_", ".xlsx");
			// Generazione del file excel
			List<String> fileNames = ExportImport.exportExcel(xlsxFile, ricognizioniList, datiRicognizioniPrecedenti, fotoRicognizioniPrecedenti, baseUrl, urlInLink);
			// Invio l'excel allo storage
			ImageManager.sendFile(xlsxFile, "../" + HelperFE.dateYYYYMMDD() + "_Ricognizioni.xlsx");
			// Se non devo esportare le immagini pulisco la lista
			if(!exportImages) {
				while(fileNames.size() > 0)
					fileNames.remove(0);
			}
			// Aggiungo l'excel ai file da scaricare
			fileNames.add("../" + HelperFE.dateYYYYMMDD() + "_Ricognizioni.xlsx");

			// Redirezione al file zip generato sullo storage
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(fileNames);

			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(photoBaseUrl + "zip_images.php");
			httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Content-type", "application/json");

			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				String s = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
				if(response.getStatusLine().getStatusCode() != 200) {
					//UI notification
					throw new Exception("StatusCode "+response.getStatusLine().getStatusCode());
				} else {
					CalciferUI.get().getPage().open(photoBaseUrl + s, "ricognizioni-downloader-iframe");
				}
			} finally {
				response.close();
			}

		} finally {
			if(xlsxFile != null) {
				xlsxFile.delete();
				xlsxFile.deleteOnExit();
			}
			try {
				httpClient.close();
			} catch(Exception e) {}
		}

		// TODO generare il file excel, caricarlo su server remoto, popolare lista immagini
		/*List<Ricognizione> ricognizioniList = new ArrayList<Ricognizione>();
		List<String> fileNames = new ArrayList<String>();

		for(Object id : ricognizioniView.getContainer().getItemIds()) {
			Ricognizione r = ricognizioniView.getContainer().getItem(id).getEntity();

			boolean keep = true;
			if(dataMin != null && dataMax != null && (r.getInizio().before(dataMin) || r.getInizio().after(dataMax)))
				keep = false;


			if(keep) {
				ricognizioniList.add(r);
				if(images) {
					if(r.getFoto_manufatto() != null && r.getFoto_manufatto().trim().length() > 0)
						fileNames.add(r.getFoto_manufatto());
					if(r.getFoto_panoramica() != null && r.getFoto_panoramica().trim().length() > 0)
						fileNames.add(r.getFoto_panoramica());
					if(r.getFoto_danno_contrassegno() != null && r.getFoto_danno_contrassegno().trim().length() > 0)
						fileNames.add(r.getFoto_danno_contrassegno());
					if(r.getFoto_danno_manufatto() != null && r.getFoto_danno_manufatto().trim().length() > 0)
						fileNames.add(r.getFoto_danno_manufatto());
					if(r.getFoto_aggiornata() != null && r.getFoto_aggiornata().trim().length() > 0)
						fileNames.add(r.getFoto_aggiornata());
					if(r.getFoto_sito_ripristino() != null && r.getFoto_sito_ripristino().trim().length() > 0)
						fileNames.add(r.getFoto_sito_ripristino());
				}
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(fileNames);

		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClients.createDefault();

			HttpPost httpPost = new HttpPost(photoBaseUrl + "zip_images.php");

			httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Content-type", "application/json");

			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				String s = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
				if(response.getStatusLine().getStatusCode() != 200) {
					//UI notification
					throw new Exception("StatusCode "+response.getStatusLine().getStatusCode());
				} else {
					System.out.println("Lanciare download di " + photoBaseUrl + s);
					CalciferUI.get().getPage().open(photoBaseUrl + s, "ricognizioni-downloader-iframe");
				}
			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}*/
	}

}
