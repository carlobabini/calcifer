package it.angelobabini.calcifer.authentication;

import it.angelobabini.calcifer.backend.Utente;
import it.angelobabini.calcifer.backend.UtenteDAO;

/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
    	Utente utente = UtenteDAO.canLogin(username, password);
        if (utente == null)
            return false;

        CurrentUser.set(utente.getUsername());
        return true;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRole(String role) {
    	Utente utente = UtenteDAO.readUtente(CurrentUser.get());
    	String userRole = utente != null ? utente.getRole() : "";
        if (userRole.equals(role)) {
            return true;
        }
        return false;
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

}
