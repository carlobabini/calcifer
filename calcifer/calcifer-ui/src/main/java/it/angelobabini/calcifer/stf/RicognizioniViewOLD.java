package it.angelobabini.calcifer.stf;

import it.angelobabini.calcifer.samples.ResetButtonForTextField;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class RicognizioniViewOLD extends CssLayout implements View {
	private static final long serialVersionUID = -1208089065976198967L;

	public static final String VIEW_NAME = "Ricognizioni";
	//private RicognizioniLogic viewLogic = new RicognizioniLogic(this);
	private RicognizioneGridOLD grid;
	private RicognizioneFormOLD form;
	private Button newRicognizione;

	public RicognizioniViewOLD() {
		setSizeFull();
		addStyleName("crud-view");
		HorizontalLayout topLayout = createTopBar();

		grid = new RicognizioneGridOLD();
		grid.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = -7858636858673847889L;

			@Override
			public void select(SelectionEvent event) {
				if(grid.getSelectedRow() != null) {
					//System.out.println(grid.getSelectedRow().getClass() + " - "+ grid.getSelectedRow());
					Item i = grid.getContainerDataSource().getItem(grid.getSelectedRow());
					System.out.println(i.getClass() + " - "+ i);
					editRicognizione(i);
				}
			}
		});

		form = new RicognizioneFormOLD();

		VerticalLayout barAndGridLayout = new VerticalLayout();
		barAndGridLayout.addComponent(topLayout);
		barAndGridLayout.addComponent(grid);
		barAndGridLayout.setMargin(true);
		barAndGridLayout.setSpacing(true);
		barAndGridLayout.setSizeFull();
		barAndGridLayout.setExpandRatio(grid, 1);
		barAndGridLayout.setStyleName("crud-main-layout");

		addComponent(barAndGridLayout);
		addComponent(form);

		//viewLogic.init();

		J2EEConnectionPool fwConnectionPool = null;
		TableQuery tq = null;
		try {
			fwConnectionPool = new J2EEConnectionPool("java:jboss/datasources/PostgreSQLDS");
			tq = new TableQuery("ricognizioni", fwConnectionPool);

			SQLContainer container = new SQLContainer(tq);
			container.setAutoCommit(false);
			grid.setContainerDataSource(container);
		} catch (Exception e) {
			e.printStackTrace();
			showError("Errore DB: "+e.getClass()+" "+e.getMessage());
		}
		grid.initGrid();
	}

	public HorizontalLayout createTopBar() {
		TextField filter = new TextField();
		filter.setStyleName("filter-textfield");
		filter.setInputPrompt("Filter");
		ResetButtonForTextField.extend(filter);
		filter.setImmediate(true);
		filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
			private static final long serialVersionUID = 2844282463976920053L;

			@Override
			public void textChange(FieldEvents.TextChangeEvent event) {
				//TODO grid.setFilter(event.getText());
			}
		});

		newRicognizione = new Button("New product");
		newRicognizione.addStyleName(ValoTheme.BUTTON_PRIMARY);
		newRicognizione.setIcon(FontAwesome.PLUS_CIRCLE);
		newRicognizione.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				//viewLogic.newRicognizione();
			}
		});

		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.setWidth("100%");
		topLayout.addComponent(filter);
		topLayout.addComponent(newRicognizione);
		topLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
		topLayout.setExpandRatio(filter, 1);
		topLayout.setStyleName("top-bar");

		filter.setEnabled(false);
		newRicognizione.setEnabled(false);

		return topLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		//viewLogic.enter(event.getParameters());
	}

	public void editRicognizione(Item item) {
		if (item != null) {
			form.addStyleName("visible");
			form.setEnabled(true);
		} else {
			form.removeStyleName("visible");
			form.setEnabled(false);
		}
		form.editItem(item);
	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	public void showSaveNotification(String msg) {
		Notification.show(msg, Type.TRAY_NOTIFICATION);
	}

	public void clearSelection() {
		grid.getSelectionModel().reset();
	}

	public void selectRow(Ricognizione row) {
		((SelectionModel.Single) grid.getSelectionModel()).select(row);
	}

}
