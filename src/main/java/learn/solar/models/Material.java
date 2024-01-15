package learn.solar.models;

public enum Material {
    MULTICRYSTALLINE_SILICON("Multicrystalline Silicon", "mc-Si"),
    MONOCRYSTALLINE_SILICON("Monocrystalline Silicon", "mono-Si"),
    AMORPHOUS_SILICON("Amorphous Silicon", "a-Si"),
    CADMIUM_TELLURIDE("Cadmium Telluride", "CdTe"),
    COPPER_INDIUM_GALLIUM_SELENIDE("Copper Indium Gallium Selenide", "CIGS");

    private final String fullName;
    private final String abbreviation;

    Material(String fullName, String abbreviation) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Material findByAbbreviation(String abbreviation) {
        for (Material material : values()) {
            if (material.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                return material;
            }
        }
        throw new IllegalArgumentException("No material found for abbreviation: " + abbreviation);
    }
}

