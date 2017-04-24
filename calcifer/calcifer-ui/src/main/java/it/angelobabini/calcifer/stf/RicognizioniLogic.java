package it.angelobabini.calcifer.stf;

import it.angelobabini.calcifer.CalciferUI;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;

import com.vaadin.server.Page;

public class RicognizioniLogic {

    private RicognizioniView view;

    public RicognizioniLogic(RicognizioniView ricognizioniView) {
        view = ricognizioniView;
    }
    
    public void init() {
        editRicognizione(null);
        // Hide and disable if not admin
        /*if (!CalciferUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewProductEnabled(false);
        }*/

        //view.showProducts(DataService.get().getAllProducts());
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String ricognizioneId) {
        String fragmentParameter;
        if (ricognizioneId == null || ricognizioneId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = ricognizioneId;
        }

        Page page = CalciferUI.get().getPage();
        page.setUriFragment("!" + RicognizioniView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void enter(String ricognizioneId) {
        if (ricognizioneId != null && !ricognizioneId.isEmpty()) {
            if (ricognizioneId.equals("new")) {
                newRicognizione();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    Ricognizione ricognizione = findRicognizione(ricognizioneId);
                    view.selectRow(ricognizione);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private Ricognizione findRicognizione(String ricognizioneId) {
    	return null;
        //return DataService.get().getProductById(productId);
    }

    public void cancelRicognizione() {
        setFragmentParameter("");
        view.clearSelection();
        view.editRicognizione(null);
    }

    public void saveRicognizione(Ricognizione ricognizione) {
        view.showSaveNotification(ricognizione.getId() + " updated");
        view.clearSelection();
        view.editRicognizione(null);
        //view.refreshRicognizione(ricognizione);
        setFragmentParameter("");
    }

    public void deleteRicognizione(Ricognizione ricognizione) {
        /*DataService.get().deleteRicognizione(ricognizione.getId());
        view.showSaveNotification(ricognizione.getId() + " removed");

        view.clearSelection();
        view.editRicognizione(null);
        view.removeRicognizione(ricognizione);
        setFragmentParameter("");*/
    }

    public void editRicognizione(Ricognizione ricognizione) {
        if (ricognizione == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(ricognizione.getId() + "");
        }
        view.editRicognizione(ricognizione);
    }

    public void newRicognizione() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editRicognizione(new Ricognizione());
    }

    public void rowSelected(Ricognizione ricognizione) {
        if (CalciferUI.get().getAccessControl().isUserInRole("admin")) {
            view.editRicognizione(ricognizione);
        }
    }
}
