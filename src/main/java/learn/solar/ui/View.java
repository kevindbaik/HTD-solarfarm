package learn.solar.ui;

import learn.solar.models.Material;
import learn.solar.models.Panel;

import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Scanner;

public class View {
    Scanner console = new Scanner(System.in);

    public int chooseOptionFromMenu() {
        displayHeader("Main Menu");
        displayText("0. Exit");
        displayText("1. Find Panels by Section");
        displayText("2. Add a Panel");
        displayText("3. Update a Panel");
        displayText("4. Remove a Panel");
        return readInt("Select [0-4]:", 0,4);
    }

    public Panel makePanel() {
        Panel result = new Panel();
        result.setSection(readSection("Section: "));
        result.setRow(readInt("Row: ", 1, 250));
        result.setColumn(readInt("Column: ", 1, 250));
        result.setMaterial(readMaterial("Material: "));
        result.setInstallationYear(readYear("Installation Year: ", 1950));
        result.setTracking(readBoolean("Tracked[y/n]: "));
        return result;
    }

    public void printPanels(String section, List<Panel> panels) {
        displayHeader("Panels in Section: " + section);
        if (panels.isEmpty()) {
            displayText("No panels found in this section.");
            return;
        }



        System.out.printf("%-4s %-4s %-5s %-20s %-8s%n", "Row", "Col", "Year", "Material", "Tracking");



        for (Panel panel : panels) {
            System.out.printf("%-4d %-4d %-5d %-20s %-8s%n",
                    panel.getRow(),
                    panel.getColumn(),
                    panel.getInstallationYear(),
                    panel.getMaterial().getAbbreviation(),
                    panel.isTracking() ? "Yes" : "No");
        }
    }

    public Panel update(Panel panelToUpdate) {
        String sectionName = readSection("Enter the section name to update a panel: ");

        return panelToUpdate;
    }


    public Panel choosePanel(String sectionName, List<Panel> panels) {
        displayHeader("Panels in Section: " + sectionName);
        if (panels.isEmpty()) {
            displayText("No panels found in this section.");
            return null;
        }

        int index = 1;
        for (Panel panel : panels) {
            displayText(index + ". Row: " + panel.getRow() + ", Column: " + panel.getColumn()
                    + ", Material: " + panel.getMaterial().getFullName());
            index++;
        }

        int panelNumber = readInt("Select a panel (by number): ", 1, panels.size());
        Panel selectedPanel = panels.get(panelNumber - 1);

        return selectedPanel;
    }

    // helper methods
    public void displayHeader(String header) {
        System.out.println();
        System.out.println(header);
        System.out.println("*".repeat(header.length()));
    }

    public void displayText(String line) {
        System.out.println();
        System.out.println(line);
    }

    public void displayErrors(List<String> errors) {
        displayHeader("Errors:");
        for(String error : errors) {
            displayText(error);
        }
        displayText("");
    }

    public String readString(String prompt) {
        displayText(prompt);
        String string = console.nextLine();
        if(string == null || string.isBlank()) {
            displayText("You must enter a value!");
            string = readString(prompt);
        }
        return string;
    }

    public String readSection(String prompt) {
        while (true) {
            String section = readString(prompt).trim();
            if (!section.isEmpty()) {
                return section;
            } else {
                displayText("Section cannot be blank. Please enter a valid section name.");
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String value = readString(prompt);
            try {
                int intValue = Integer.parseInt(value);
                if(intValue < min || intValue > max) {
                    System.out.printf("Please choose another number between [%s - %s]%n", min, max);
                } else return intValue;
            } catch (NumberFormatException ex) {
                System.out.printf("%s is not a valid number.%n", value);
            }
        }
    }

    public int readYear(String prompt, int min) {
        int currentYear = 2024;
        while (true) {
            int year = readInt(prompt + " [" + min + "-" + currentYear + "]: ", min, currentYear);
            if (year <= currentYear) {
                return year;
            } else {
                displayText("Year must not be in the future. Please enter a year between " + min + " and " + currentYear + ".");
            }
        }
    }


    public Material readMaterial(String prompt) {
        displayHeader("Panel Material:");
        for(Material material: Material.values()) {
            displayText(material.getAbbreviation());
        }
        while(true) {
            String selection = readString(prompt).toUpperCase();
            try {
                return Material.findByAbbreviation(selection);
            } catch (IllegalArgumentException ex) {
                System.out.printf("%s is not a material%n", selection);
            }
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readString(prompt + " (y/n): ").trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                displayText("Invalid input. Please enter 'y' (yes) or 'n' (no).");
            }
        }
    }

    public boolean confirm(String prompt) {
        String input = readString(prompt).trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }
}
