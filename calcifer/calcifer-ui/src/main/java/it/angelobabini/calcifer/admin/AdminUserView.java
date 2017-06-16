package it.angelobabini.calcifer.admin;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.backend.CRUDLogic;
import it.angelobabini.calcifer.backend.Utente;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class AdminUserView extends CssLayout implements View, CRUDLogic<Utente> {
	private static final long serialVersionUID = 7528843763453284955L;

	public static final String VIEW_NAME = "Utenti";

	private final Grid grid;
	private JPAContainer<Utente> utenteContainer = null;
	private Button newUser;
	private AdminUserForm form = new AdminUserForm(this);

	public AdminUserView() {
		setSizeFull();
		addStyleName("crud-view");
		HorizontalLayout topLayout = createTopBar();

		grid = new Grid();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(new SelectionListener() {
			private static final long serialVersionUID = -7858636858673847889L;

			@Override
			public void select(SelectionEvent event) {
				Object r = grid.getSelectedRow();
				if(r != null) {
					Utente u = utenteContainer.getItem(r).getEntity();
					if(u != null)
						editEntity(u);
				}
			}
		});

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

		editEntity(null);
		
		/*Utente nuovo = new Utente();
		nuovo.setUsername("gabriele_bitelli");
		nuovo.setName("Gabriele Bitelli");
		nuovo.setPassword(Utente.encodePassword("gabrieleb"));
		nuovo.setEmail("");
		nuovo.setRole("USER");
		new it.angelobabini.calcifer.backend.UtenteDAO().saveEntity(nuovo);*/
	}

	public HorizontalLayout createTopBar() {
		newUser = new Button("New user");
		newUser.addStyleName(ValoTheme.BUTTON_PRIMARY);
		newUser.setIcon(FontAwesome.PLUS_CIRCLE);
		newUser.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9196145233400929398L;

			@Override
			public void buttonClick(ClickEvent event) {
				newEntity();
			}
		});

		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.setWidth("100%");
		topLayout.addComponent(newUser);
		topLayout.setStyleName("top-bar");
		return topLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if(utenteContainer == null) {
			utenteContainer = JPAContainerFactory.make(Utente.class, CalciferUI.PERSISTENCE_UNIT_NAME);
			grid.setContainerDataSource(utenteContainer);
			grid.setSizeFull();
			grid.setEditorEnabled(false);

			grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
				private static final long serialVersionUID = 6012020572584410249L;

				@Override
				public void preCommit(CommitEvent commitEvent) throws CommitException {
					// do nothing
				}

				@Override
				public void postCommit(CommitEvent commitEvent) throws CommitException {
					try {
						utenteContainer.commit();
					} catch (UnsupportedOperationException e) {
						Notification n = new Notification("Errore commit: "+e.getClass()+" "+e.getMessage(), Type.ERROR_MESSAGE);
	                    n.setDelayMsec(500);
	                    n.show(getUI().getPage());
	                    e.printStackTrace();
					}
					utenteContainer.refresh();
				}
			});
			initGrid();
		}
	}

	private void initGrid() {
		grid.removeColumn("password");
		grid.setColumnOrder("username", "name", "email", "role");
	}

	public Utente getSelectedRow() {
		return (Utente)grid.getSelectedRow();
	}

	private void refresh(Utente t) {
		EntityItem<Utente> item = utenteContainer.getItem(t.getUsername());
		if (item != null) {
			// Updated product
			item.getItemProperty("username").fireValueChangeEvent();
		} else {
			// New product
			utenteContainer.addItem(t);
		}
	}

	@Override
	public Utente newEntity() {
		Utente utente = new Utente();
		editEntity(utente);
		return utente;
	}

	@Override
	public void deleteEntity(Utente t) {
		utenteContainer.removeItem(t);
	}

	@Override
	public void saveEntity(Utente t) {
		refresh(t);
		grid.scrollTo(t.getUsername());
	}

	@Override
	public void editEntity(Utente t) {
		if(t != null) {
			form.addStyleName("visible");
			form.setEnabled(true);
		} else {
			form.removeStyleName("visible");
			form.setEnabled(false);
		}
		form.editUtente(t);
	}
}
