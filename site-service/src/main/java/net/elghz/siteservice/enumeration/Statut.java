package net.elghz.siteservice.enumeration;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Statut {
    EN_SERVICE("En service"),
    HORS_SERVICE("Hors service"),
    EN_MAINTENANCE("En maintenance");

    private final String value;

    Statut(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Statut fromValue(String value) {
        for (Statut statut : Statut.values()) {
            if (statut.value.equalsIgnoreCase(value.trim())) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}