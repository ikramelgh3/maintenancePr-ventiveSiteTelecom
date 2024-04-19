package net.elghz.planningmaintenance.enumeration;

public enum PlanningStatus {

    EN_ATTENTE_VALIDATION("En attente de validation"),
    VALIDEE("Validée"),
    EN_ATTENTE("En attente"),
    EN_COURS("En cours"),
    TERMINE("Terminé"),
    ANNULE("Annulé");

    private final String label;

    PlanningStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
