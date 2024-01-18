package learn.solar.ui;

import learn.solar.data.DataException;
import learn.solar.domain.PanelResult;
import learn.solar.domain.PanelService;
import learn.solar.models.Panel;

import java.util.List;

// the purpose of controller is to mediate between the view and domain layer
// it has 2 dependencies, view and service
// you will never use the console inside your controller
public class Controller {
    private final View view;
    private final PanelService service;

    public Controller(View view, PanelService service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        view.displayHeader("Welcome to Solar Farm");
        try{
            runMenu();
        } catch (DataException ex) {
            view.displayText("Something went wrong");
            view.displayText(ex.getMessage());
        }
        view.displayText("Goodbye!");
    }

    private void runMenu() throws DataException {
        boolean exit = false;
        while(!exit) {
            int selection = view.chooseOptionFromMenu();
            switch(selection){
                case 0:
                    exit = true;
                    break;
                case 1:
                    viewBySection();
                    break;
                case 2:
                    addPanel();
                    break;
                case 3:
                    updatePanel();
                    break;
                case 4:
                    deletePanel();
                    break;
            }
        }
    }

    private void addPanel() throws DataException {
        view.displayHeader("Create a Panel");
        Panel panel = view.makePanel();
        PanelResult result = service.add(panel);
        if(result.isSuccess()) {
            view.displayText("Your Panel was successfully created.");
        } else {
            view.displayErrors(result.getMessages());
        }
    }

    private void viewBySection() throws DataException {
        view.displayHeader("View Panels by Section");
        String section = view.readSection("Enter the section name: ");
        List<Panel> panels = service.findBySection(section);
        view.printPanels(section, panels);
    }

    private void updatePanel() throws DataException {
        view.displayHeader("Update a Panel");
        String section = view.readSection("Enter the section to choose a panel from (for updating): ");
        List<Panel> panels = service.findBySection(section);
        Panel panelToUpdate = view.choosePanel(section, panels);
        if (panelToUpdate != null) {
            Panel updatedPanel = view.makePanel();
            updatedPanel.setId(panelToUpdate.getId());
            PanelResult result = service.update(updatedPanel);
            if(result.isSuccess()) {
                view.displayText("Your panel was successfully updated.");
            } else {
                view.displayErrors(result.getMessages());
            }
        }
    }

    private void deletePanel() throws DataException {
        view.displayHeader("Delete a Panel");
        String section = view.readSection("Enter the section to choose a panel from (for deletion): ");
        List<Panel> panels = service.findBySection(section);
        Panel panelToDelete = view.choosePanel(section, panels);

        if (panelToDelete == null) {
            view.displayText("No panel selected or found.");
            return;
        }

        boolean confirmDelete = view.confirm("Are you sure you want to delete this panel? [y/n]: ");
        if (confirmDelete) {
            PanelResult result = service.deleteById(panelToDelete.getId());
            if (result.isSuccess()) {
                view.displayText("Panel successfully deleted.");
            } else {
                view.displayText("Panel could not be deleted.");
            }
        } else {
            view.displayText("Panel deletion cancelled.");
        }
    }
}
