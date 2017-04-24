package it.angelobabini.calcifer.admin;

import it.angelobabini.calcifer.backend.CRUDLogic;
import it.angelobabini.calcifer.backend.Utente;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class AdminUserForm extends FormLayout {
	private static final long serialVersionUID = 65416754150707888L;

	private final BeanFieldGroup<Utente> fieldGroup = new BeanFieldGroup<Utente>(Utente.class);
	private final TextField username = new TextField("Username");
	private final TextField name = new TextField("Nome");
	private final TextField email = new TextField("Email");
	private final ComboBox role = new ComboBox("Ruolo");
	private final PasswordField password = new PasswordField("Password");
	private final PasswordField passwordChangeField = new PasswordField("Nuova password");
	private final PasswordField passwordCheckField = new PasswordField("Verifica nuova password");
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
		addComponent(passwordChangeField);
		addComponent(passwordCheckField);
		addComponent(saveButton);
		addComponent(cancelButton);
		addComponent(deleteButton);
		
		fieldGroup.bindMemberFields(this);
		role.addItem("ADMIN");
		
		ValueChangeListener valueListener = new ValueChangeListener() {
			private static final long serialVersionUID = -8248645837076024474L;

			@Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };
        for (Field<?> f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
        }
        
		password.setVisible(false);
        passwordChangeField.removeValueChangeListener(valueListener);
        passwordCheckField.removeValueChangeListener(valueListener);
        
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
                }
            }
        });

        cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2577184061694953438L;

			@Override
            public void buttonClick(ClickEvent event) {
				//logic.saveEntity(fieldGroup.getItemDataSource().getBean());
				logic.editEntity(null);
            }
        });

        deleteButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5848312261802628146L;

			@Override
            public void buttonClick(ClickEvent event) {
                logic.deleteEntity(fieldGroup.getItemDataSource().getBean());
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
}
