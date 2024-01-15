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
}
