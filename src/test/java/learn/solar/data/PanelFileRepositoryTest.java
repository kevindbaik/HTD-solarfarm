package learn.solar.data;

import learn.solar.models.Panel;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class PanelFileRepositoryTest {
    private PanelFileRepository repository = new PanelFileRepository("./data/panels.csv");
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
}
