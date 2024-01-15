package learn.solar.data;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
public class PanelFileRepository {
    private String filePath;

    public PanelFileRepository(String filePath) {
        this.filePath = filePath;
    }
    public Panel deserialize(String line) {
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
            return panel;
        }
        return null;
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

    public List<Panel> findBySection(String section) {
        List<Panel> allPanels = findAll();
        List<Panel> matchingPanels = new ArrayList<>();

        for(Panel panel : allPanels) {
            if(panel.getSection().equalsIgnoreCase(section)) matchingPanels.add(panel);
        }

        return matchingPanels;
    }

    public Panel add(Panel panel) throws DataException{
        List<Panel> allPanels = findAll();
        int nextId = 0;
        for(Panel p : allPanels) {
            nextId = Math.max(nextId, p.getId());
        }
        nextId++;
        panel.setId(nextId);
        allPanels.add(panel);
        writeAll(allPanels);
        return panel;
    }

    private void writeAll(List<Panel> panels) throws DataException {
        try(PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("id, section, row, column, installationYear, material, tracking");
            for(Panel p : panels) {
                writer.println(serialize(p));
            }
        } catch (IOException ex) {
            throw new DataException(ex.getMessage(), ex);
        }
    }

    public String serialize(Panel panel) {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                panel.getId(),
                panel.getSection(),
                panel.getRow(),
                panel.getColumn(),
                panel.getInstallationYear(),
                panel.getMaterial().getAbbreviation(),
                panel.isTracking()
        );
    }

}
