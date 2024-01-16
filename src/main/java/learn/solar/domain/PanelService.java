package learn.solar.domain;

import learn.solar.data.DataException;
import learn.solar.data.PanelRepository;
import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.EnumSet;
import java.util.List;

public class PanelService {

    // required data dependency
    private final PanelRepository repository;

    // constructor
    public PanelService(PanelRepository repository) {
        this.repository = repository;
    }

    // pass-through to repository
    public List<Panel> findBySection(String section) throws DataException {
        return repository.findBySection(section);
    }

    // validate, then add via repository
    public PanelResult add(Panel panel) throws DataException {
        PanelResult result = validate(panel, false);
        if(!result.isSuccess()) {
            return result;
        }

        panel = repository.add(panel);
        result.setPanel(panel);
        return result;
    }

    public PanelResult update(Panel panel) throws DataException {
        PanelResult result = validate(panel, true);
        if(!result.isSuccess()) return result;

        boolean updated = repository.update(panel);
        if(!updated) {
            result.addErrorMessage(String.format("Panel with id: %s does not exist", panel.getId()));
        }
        result.setPanel(panel);
        return result;
    }

    public PanelResult deleteById(int panelId) throws DataException {
        PanelResult result = new PanelResult();
        if(!repository.deleteById(panelId)) {
            result.addErrorMessage(String.format("Panel with id: %s does not exist", panelId));
        }
        return result;
    }

    // general-purpose validation routine
    private PanelResult validate(Panel panel, boolean isUpdate) throws DataException {
        // Section is required and cannot be blank.
        // Row is a positive number less than or equal to 250.
        // Column is a positive number less than or equal to 250.
        // Year Installed must be in the past.
        // Material is required and can only be one of the five materials listed.
        // The combined values of Section, Row, and Column may not be duplicated.
        PanelResult result = new PanelResult();

        if (panel == null) {
            result.addErrorMessage("Panel cannot be null.");
            return result;
        }

        if (panel.getSection() == null || panel.getSection().trim().isEmpty()) {
            result.addErrorMessage("Section is required and cannot be blank.");
            return result;
        }

        if (panel.getRow() <= 0 || panel.getRow() > 250) {
            result.addErrorMessage("Row must be a positive number between 1 - 250.");
            return result;
        }

        if (panel.getColumn() <= 0 || panel.getColumn() > 250) {
            result.addErrorMessage("Column must be a positive number between 1 - 250.");
            return result;
        }

        if (panel.getInstallationYear() > 2024) {
            result.addErrorMessage("Year installed must be in the past.");
            return result;
        }

        if (panel.getMaterial() == null || !EnumSet.allOf(Material.class).contains(panel.getMaterial())) {
            result.addErrorMessage("Material is required and must be one of the five listed.");
            return result;
        }

        if (!isUpdate && isDuplicatePanel(panel)) {
            result.addErrorMessage("A panel with the same Section, Row, and Column already exists.");
            return result;
        }

        return result;
    }

    private boolean isDuplicatePanel(Panel panel) throws DataException {
        List<Panel> existingPanels = repository.findBySection(panel.getSection());
        for (Panel existingPanel : existingPanels) {
            if (existingPanel.getRow() == panel.getRow()
                    && existingPanel.getColumn() == panel.getColumn()
                    && existingPanel.getId() != panel.getId())
                     {
                return true;
            }
        }
        return false;
    }
}
