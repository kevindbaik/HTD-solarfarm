package learn.solar.domain;

import learn.solar.data.DataException;
import learn.solar.data.PanelRepository;
import learn.solar.data.PanelRepositoryDouble;
import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class PanelServiceTest {
    PanelRepository repository = new PanelRepositoryDouble();
    PanelService service = new PanelService(repository);

    @Test
    public void shouldCreatePanel() throws DataException {
        PanelResult actual = service.add(new Panel(4, "The Ridge", 1, 4, 2016, Material.AMORPHOUS_SILICON, false));
        assertNotNull(actual.getPanel());
        assertTrue(actual.isSuccess());

        assertEquals("The Ridge", actual.getPanel().getSection());
    }

    @Test
    public void shouldNotCreateNullPanel() throws DataException {
        PanelResult actual = service.add(null);
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Panel cannot be null.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelWithBlankSection() throws DataException {
        PanelResult actual = service.add(new Panel(4, "", 1, 4, 2016, Material.AMORPHOUS_SILICON, false));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Section is required and cannot be blank.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelWithInvalidRow() throws DataException {
        PanelResult actual = service.add(new Panel(4, "The Ridge", 1000, 4, 2016, Material.AMORPHOUS_SILICON, false));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Row must be a positive number between 1 - 250.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelWithInvalidColumn() throws DataException {
        PanelResult actual = service.add(new Panel(4, "The Ridge", 1, 4000, 2016, Material.AMORPHOUS_SILICON, false));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Column must be a positive number between 1 - 250.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelWithInvalidYear() throws DataException {
        PanelResult actual = service.add(new Panel(4, "The Ridge", 1, 4, 2026, Material.AMORPHOUS_SILICON, false));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Year installed must be in the past.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelWithInvalidMaterial() throws DataException {
        Panel panel = new Panel(4, "The Ridge", 1, 4, 2016, null, false);
        Material invalidMaterial;
        try {
            invalidMaterial = Material.findByAbbreviation("invalid-abbreviation");
            panel.setMaterial(invalidMaterial);
        } catch (IllegalArgumentException e) {
            panel.setMaterial(null);
        }

        PanelResult actual = service.add(panel);
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("Material is required and must be one of the five listed.", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreatePanelThatExists() throws DataException {
        PanelResult exist = service.add(new Panel(4, "The Ridge", 1, 4, 2016, Material.AMORPHOUS_SILICON, false));
        assertTrue(exist.isSuccess());

        PanelResult actual = service.add(new Panel(5, "The Ridge", 1, 4, 2017, Material.AMORPHOUS_SILICON, false));
        assertFalse(actual.isSuccess());
        assertNull(actual.getPanel());
        assertEquals("A panel with the same Section, Row, and Column already exists.", actual.getMessages().get(0));
    }
}
