package net.elghz.actioncorrectiveservice.enumeration;

public enum TypeTechnicien {

    INTERNE("INTERNE"),
    EXTERNE("EXTERNE");

    private final String label;

    TypeTechnicien(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

