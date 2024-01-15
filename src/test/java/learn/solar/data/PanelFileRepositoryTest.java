package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class PanelFileRepositoryTest {
    private String SEED_PATH = "./data/panels-seed.csv";
    private String TEST_PATH = "./data/panels-test.csv";

    private PanelFileRepository repository = new PanelFileRepository(TEST_PATH);

    @BeforeEach
    void setup() throws IOException {
        Files.copy(Paths.get(SEED_PATH),
                Paths.get(TEST_PATH),
                StandardCopyOption.REPLACE_EXISTING);
    }
    @Test
    void shouldFindBySection() throws DataException{
        List<Panel> ridge = repository.findBySection("The Ridge");
        assertNotNull(ridge);
        assertEquals(5, ridge.size());
    }

    @Test
    void shouldNotFindBySection() throws DataException {
        List<Panel> none = repository.findBySection("Nothing");
        assertEquals(0, none.size());
    }

    @Test
    void shouldAddPanel() throws DataException {
        Panel panel = new Panel();
        panel.setId(6);
        panel.setSection("Testing");
        panel.setRow(1);
        panel.setColumn(1);
        panel.setInstallationYear(2024);
        panel.setMaterial(Material.AMORPHOUS_SILICON);
        panel.setTracking(false);

        Panel actual = repository.add(panel);
        assertNotNull(actual);
        assertEquals(6, actual.getId());
    }

    @Test
    void shouldUpdateExisting() throws DataException {
        Panel panel = new Panel();
        panel.setId(3);
        panel.setSection("Updated Ridge");
        panel.setMaterial(Material.CADMIUM_TELLURIDE);
        panel.setTracking(false);

        boolean success = repository.update(panel);
        assertTrue(success);

        List<Panel> actual = repository.findBySection("Updated Ridge");
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Panel panel = new Panel();
        panel.setId(100000);

        boolean actual = repository.update(panel);
        assertFalse(actual);
    }

    @Test
    void shouldDeleteExisting() throws DataException {
        boolean actual = repository.deleteById(2);
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteMissing() throws DataException {
        boolean actual = repository.deleteById(23444);
        assertFalse(actual);
    }
}
