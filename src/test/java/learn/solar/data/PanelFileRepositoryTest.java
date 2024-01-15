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
    void shouldFindFivePanels() {
        List<Panel> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(5, actual.size());
    }

    @Test
    void shouldFindBySection(){
        List<Panel> ridge = repository.findBySection("The Ridge");
        assertNotNull(ridge);
        assertEquals(5, ridge.size());
    }

    @Test
    void shouldNotFindBySection() {
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
}
