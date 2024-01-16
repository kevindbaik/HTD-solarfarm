package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.util.ArrayList;
import java.util.List;

public class PanelRepositoryDouble implements PanelRepository {
    private List<Panel> allPanels = new ArrayList<>();
    public PanelRepositoryDouble() {
        allPanels.add(new Panel(1, "The Ridge", 1, 1, 2014, Material.AMORPHOUS_SILICON, true));
        allPanels.add(new Panel(2, "The Ridge", 1, 2, 2014, Material.CADMIUM_TELLURIDE, true));
        allPanels.add(new Panel(3, "The Ridge", 1, 3, 2015, Material.MULTICRYSTALLINE_SILICON, false));
    }

    @Override
    public List<Panel> findBySection(String section) throws DataException {
        List<Panel> matchingPanels = new ArrayList<>();
        for (Panel panel : allPanels) {
            if (panel.getSection().equalsIgnoreCase(section)) {
                matchingPanels.add(panel);
            }
        }
        return matchingPanels;
    }

    @Override
    public Panel add(Panel panel) throws DataException {
        allPanels.add(panel);
        return panel;
    }

    @Override
    public boolean update(Panel panel) throws DataException {
        return panel.getId() > 0;
    }

    @Override
    public boolean deleteById(int id) throws DataException {
        return id != 999;
    }
}
