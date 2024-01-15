package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class PanelFileRepository {
    private String filePath;

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }

    public List<Panel> findAll() {
        ArrayList<Panel> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",");
                if(fields.length == 7) {
                    Panel panel = new Panel();
                    panel.setId(Integer.parseInt(fields[0]));
                    panel.setSection(fields[1]);
                    panel.setRow(Integer.parseInt(fields[2]));
                    panel.setColumn(Integer.parseInt(fields[3]));
                    panel.setInstallationYear(Integer.parseInt(fields[4]));
                    panel.setMaterial(Material.findByAbbreviation(fields[5]));
                    panel.setTracking(Boolean.parseBoolean(fields[6]));
                    result.add(panel);
                }
            }
        } catch(IOException ex) {

        }
        return result;
    }
}
