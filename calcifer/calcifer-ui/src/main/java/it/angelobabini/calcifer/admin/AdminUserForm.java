package it.angelobabini.calcifer.admin;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.backend.CRUDLogic;
import it.angelobabini.calcifer.backend.Utente;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class AdminUserForm extends FormLayout {
	private static final long serialVersionUID = 65416754150707888L;

	private final BeanFieldGroup<Utente> fieldGroup = new BeanFieldGroup<Utente>(Utente.class);
	private final TextField username = new TextField("Username");
	private final TextField name = new TextField("Nome");
	private final TextField email = new TextField("Email");
	private final ComboBox role = new ComboBox("Ruolo");
	private final PasswordField password = new PasswordField("Password");
	private final Button editPasswordButton = new Button("Modifica password");
	
	private final Button saveButton = new Button("Salva");
	private final Button cancelButton = new Button("Annulla");
	private final Button deleteButton = new Button("Elimina");
	
	public AdminUserForm(CRUDLogic<Utente> logic) {
		super();
        addStyleName("product-form");
        addStyleName("product-form-wrapper");
        
		addComponent(username);
		addComponent(name);
		addComponent(email);
		addComponent(role);
		addComponent(password);
		addComponent(editPasswordButton);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		addComponent(buttonLayout);
		
		fieldGroup.bindMemberFields(this);
		role.addItem("ADMIN");
		role.addItem("USER");
		
		ValueChangeListener valueListener = new ValueChangeListener() {
			private static final long serialVersionUID = -8248645837076024474L;

			@Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };
        for (Field<?> f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
            f.setWidth(100, Unit.PERCENTAGE);
        }
        
        fieldGroup.addCommitHandler(new CommitHandler() {
			private static final long serialVersionUID = -1644129073621632116L;

			@Override
            public void preCommit(CommitEvent commitEvent) throws CommitException {
            }

            @Override
            public void postCommit(CommitEvent commitEvent) throws CommitException {
            	logic.saveEntity(fieldGroup.getItemDataSource().getBean());
            }
        });
        
        editPasswordButton.setSizeUndefined();
        editPasswordButton.setIcon(FontAwesome.EDIT);
        editPasswordButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4276205149235631087L;

			@Override
            public void buttonClick(ClickEvent event) {
				editPassword();
            }
        });

		buttonLayout.addComponent(saveButton);
		buttonLayout.setExpandRatio(saveButton, 2);
		saveButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4276205149235631087L;

			@Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();
                } catch (CommitException e) {
                    Notification n = new Notification("Please re-check the fields", Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                    e.printStackTrace();
                }
            }
        });


		buttonLayout.addComponent(cancelButton);
		buttonLayout.setExpandRatio(cancelButton, 2);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2577184061694953438L;

			@Override
            public void buttonClick(ClickEvent event) {
				//logic.saveEntity(fieldGroup.getItemDataSource().getBean());
				logic.editEntity(null);
            }
        });

		buttonLayout.addComponent(deleteButton);
		buttonLayout.setExpandRatio(deleteButton, 0);
		deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
        deleteButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5848312261802628146L;

			@Override
            public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(CalciferUI.getCurrent(),
						"Eliminazione definitiva",
						"Vuoi davvero eliminare definitivamente?",
						"Si", "No", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 4744786090942500378L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
			                logic.deleteEntity(fieldGroup.getItemDataSource().getBean());
						}
					}
				});
            }
        });
    }

    public void editUtente(Utente utente) {
        if (utente == null) {
            utente = new Utente();
        }
        fieldGroup.setItemDataSource(new BeanItem<Utente>(utente));

        // before the user makes any changes, disable validation error indicator
        // of the product name field (which may be empty)
        username.setValidationVisible(false);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId() + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    private void formHasChanged() {
        // show validation errors after the user has changed something
        username.setValidationVisible(true);

        // only products that have been saved should be removable
        boolean canRemoveProduct = false;
        BeanItem<Utente> item = fieldGroup.getItemDataSource();
        if (item != null) {
            Utente utente = item.getBean();
            canRemoveProduct = utente.getUsername() != null && utente.getUsername().trim().length()>0;
        }
        deleteButton.setEnabled(canRemoveProduct);
    }
    
    private void editPassword() {
    	final Window subWindow = new Window("Nuova password");
		subWindow.setModal(true);
		subWindow.center();
		FormLayout layout = new FormLayout();
		subWindow.setContent(layout);
		layout.setMargin(true);

		final PasswordField password1Field = new PasswordField("Nuova password");
		final PasswordField password2Field = new PasswordField("Conferma password");
		final Button confirmButton = new Button("Conferma");
		
		password1Field.setMaxLength(30);
		password2Field.setMaxLength(30);
		layout.addComponent(password1Field);
		layout.addComponent(password2Field);
		layout.addComponent(confirmButton);
		
		confirmButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5848312261802628146L;

			@Override
            public void buttonClick(ClickEvent event) {
				if(password1Field.getValue().trim().equals(password2Field.getValue().trim())) {
					password.setValue(Utente.encodePassword(password1Field.getValue().trim()));
					subWindow.close();
				} else {
					Notification n = new Notification("Le password inserite non coincidono", Type.ERROR_MESSAGE);
					n.setDelayMsec(500);
					n.show(getUI().getPage());
				}
            }
        });

		CalciferUI.get().addWindow(subWindow);
    }
}
