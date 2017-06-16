package it.angelobabini.calcifer.stf;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import it.angelobabini.calcifer.AdvancedFileDownloader;
import it.angelobabini.calcifer.AdvancedFileDownloader.AdvancedDownloaderListener;
import it.angelobabini.calcifer.AdvancedFileDownloader.DownloaderEvent;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;
import it.angelobabini.calcifer.stf.backend.data.StfDAO;

public class CapisaldoView extends CssLayout implements View {
	private static final long serialVersionUID = 3374432000340882930L;
	
	public static final String VIEW_NAME = "Capisaldo";
	private Button refreshButton = new Button();
	private Button exportExcelButton = new Button("Esporta");
	private final AdvancedFileDownloader downloader = new AdvancedFileDownloader();
	private Button importExcelButton = new Button("Importa");
	//private final RicognizioneGrid grid = new RicognizioneGrid(this);
	//private final RicognizioneForm form = new RicognizioneForm(this);
	private final Grid grid = new Grid();
	private final VerticalLayout form = new VerticalLayout();

	public CapisaldoView() {
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
	}
	
	public HorizontalLayout createTopBar() {
		refreshButton.setIcon(FontAwesome.REFRESH);
		refreshButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				//grid.getContainer().refresh();
				throw new RuntimeException("refreshButton non funziona");
			}
		});
		
		exportExcelButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		exportExcelButton.setIcon(FontAwesome.FILE_EXCEL_O);
		downloader.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(DownloaderEvent downloadEvent) {
            	Ricognizione ricognizione = StfDAO.getRicognizione("instanceCARLO");
            	ricognizione.setId_in_altre_reti("BOIADGIUDA");
            	StfDAO.saveRicognizione(ricognizione);
            	/*downloader.setFilePath("");
            	try {
            		File f = exportExcel();
            		if(f != null) {
            			downloader.setFilePath(f.getAbsolutePath());
            		}
            	} catch (Exception e) {
            		Notification n = new Notification("Errore esportazione", Type.ERROR_MESSAGE);
            		n.setDelayMsec(500);
            		n.show(getUI().getPage());
            		e.printStackTrace();
            	}*/
            }
		});
		downloader.extend(exportExcelButton);
		
		importExcelButton.setIcon(FontAwesome.FILE_EXCEL_O);
		importExcelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				/*try {
					importExcel();
				} catch (Exception e) {
					Notification n = new Notification("Errore importazione", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
					e.printStackTrace();
				}*/
			}
		});

		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.setWidth(100, Unit.PERCENTAGE);
		topLayout.addComponent(refreshButton);
		topLayout.addComponent(exportExcelButton);
		topLayout.addComponent(importExcelButton);
		topLayout.setComponentAlignment(exportExcelButton, Alignment.MIDDLE_RIGHT);
		topLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_LEFT);
		topLayout.setExpandRatio(exportExcelButton, 1);
		topLayout.setExpandRatio(refreshButton, 2);
		topLayout.setStyleName("top-bar");

		return topLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}
