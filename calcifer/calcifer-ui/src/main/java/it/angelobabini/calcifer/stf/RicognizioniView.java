package it.angelobabini.calcifer.stf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.themes.ValoTheme;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.authentication.CurrentUser;
import it.angelobabini.calcifer.stf.backend.data.ExportImport;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

public class RicognizioniView extends CssLayout implements View {
	private static final long serialVersionUID = -1208089065976198967L;

	public static final String VIEW_NAME = "Ricognizioni";
	private Button refreshButton = new Button();
	private Button importExcelButton = new Button("Importa");
	private Button exportButton = new Button("Esporta Excel ed Immagini");
	private final RicognizioneGrid grid = new RicognizioneGrid(this);
	private final RicognizioneForm form = new RicognizioneForm(this);

	public RicognizioniView() {
		setSizeFull();
		addStyleName("crud-view");
		HorizontalLayout topLayout = createTopBar();
		
		VerticalLayout barAndGridLayout = new VerticalLayout();
		barAndGridLayout.addComponent(topLayout);
		barAndGridLayout.addComponent(grid);
		barAndGridLayout.setMargin(true);
		barAndGridLayout.setSizeFull();
		barAndGridLayout.setExpandRatio(grid, 1);
		barAndGridLayout.setStyleName("crud-main-layout");

		addComponent(barAndGridLayout);
		addComponent(form);

		editRicognizione(null);
		
		grid.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = -7858636858673847889L;

			@Override
			public void select(SelectionEvent event) {
				if(grid.getSelectedRow() != null) {
					EntityItem<Ricognizione> i = getContainer().getItem(grid.getSelectedRow());
					editRicognizione(i);
				}
			}
		});
	}
	
	public HorizontalLayout createTopBar() {
		refreshButton.setIcon(FontAwesome.REFRESH);
		refreshButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				grid.getContainer().refresh();
			}
		});

		exportButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		exportButton.setIcon(FontAwesome.FILE_EXCEL_O);
		exportButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					exportExcelAndImages();
				} catch (Exception e) {
					Notification n = new Notification("Errore esportazione", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
					e.printStackTrace();
				}
			}
		});
		
		importExcelButton.setIcon(FontAwesome.FILE_EXCEL_O);
		importExcelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					importExcel();
				} catch (Exception e) {
					Notification n = new Notification("Errore importazione", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
					e.printStackTrace();
				}
			}
		});

		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.setWidth(100, Unit.PERCENTAGE);
		topLayout.addComponent(refreshButton);
		topLayout.addComponent(exportButton);
		topLayout.addComponent(importExcelButton);
		topLayout.setComponentAlignment(exportButton, Alignment.MIDDLE_RIGHT);
		topLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_LEFT);
		topLayout.setExpandRatio(exportButton, 1);
		topLayout.setExpandRatio(refreshButton, 2);
		topLayout.setStyleName("top-bar");

		return topLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
	
	public JPAContainer<Ricognizione> getContainer() {
		return grid.getContainer();
	}
	
	public void editRicognizione(EntityItem<Ricognizione> entityItem) {
		if(entityItem != null) {
			form.addStyleName("visible");
			form.setEnabled(true);
		} else {
			form.removeStyleName("visible");
			form.setEnabled(false);
		}
		form.writeItem(entityItem);
	}
	
	public void deleteRicognizione(EntityItem<Ricognizione> entityItem) {
		try {
			getContainer().removeItem(entityItem.getItemId());
			getContainer().commit();
		} catch (UnsupportedOperationException e) {
			Notification n = new Notification("Errore cancellazione", Type.ERROR_MESSAGE);
			n.setDelayMsec(500);
			n.show(getUI().getPage());
			e.printStackTrace();
		}
		getContainer().refresh();
		editRicognizione(null);
	}
	
	public void saveRicognizione(EntityItem<Ricognizione> entityItem) {
		try {
			entityItem.getEntity().setModifica(Timestamp.from(Instant.now()));
			getContainer().getEntityProvider().getEntityManager().getTransaction().begin();
			getContainer().getEntityProvider().getEntityManager().merge(entityItem.getEntity());
			getContainer().getEntityProvider().getEntityManager().getTransaction().commit();
			getContainer().refreshItem(entityItem.getItemId());
		} catch (UnsupportedOperationException e) {
			Notification n = new Notification("Errore cancellazione", Type.ERROR_MESSAGE);
			n.setDelayMsec(500);
			n.show(getUI().getPage());
			e.printStackTrace();
		}
		editRicognizione(null);
	}

	private void importExcel() throws Exception {
		final File tempFile = File.createTempFile("temp", ".xlsx");
		final Window subWindow = new Window("Importa file");
		final VerticalLayout layout = new VerticalLayout();

		Upload upload = new Upload("", new Upload.Receiver() {
			private static final long serialVersionUID = 5625837892202003151L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				try {
					/* Here, we'll stored the uploaded file as a temporary file. No doubt there's
		            a way to use a ByteArrayOutputStream, a reader around it, use ProgressListener (and
		            a progress bar) and a separate reader thread to populate a container *during*
		            the update.
		            This is quick and easy example, though.
					 */
					return new FileOutputStream(tempFile);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		upload.addFinishedListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 1561672558167842612L;

			@Override
			public void uploadFinished(Upload.FinishedEvent finishedEvent) {
				try {
					String result = ExportImport.importXLSX(tempFile, CurrentUser.get());
					layout.addComponent(new Label(result));
					layout.removeComponent(upload);
					grid.getContainer().refresh();
				} catch (Exception e) {
					Notification n = new Notification("Errore importazione", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
					e.printStackTrace();
				} finally {
					tempFile.deleteOnExit();
					tempFile.delete();
				}
				
			}
		});
		upload.setImmediate(false);
		
		layout.setMargin(true);
		layout.addComponent(upload);

		subWindow.setModal(true);
        subWindow.setClosable(true);
        subWindow.center();
		subWindow.setContent(layout);
        CalciferUI.get().addWindow(subWindow);
	}
	
	private void exportExcelAndImages() {
		final Window subWindow = new Window("Opzioni esportazione file");
		final RicognizioniDownloader ricognizioneDownloader = new RicognizioniDownloader(this);
		
		subWindow.setModal(true);
        subWindow.setClosable(true);
        subWindow.setResizable(false);
        subWindow.center();
		subWindow.setContent(ricognizioneDownloader);
        CalciferUI.get().addWindow(subWindow);
	}
}
