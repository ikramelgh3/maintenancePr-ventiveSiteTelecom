package net.elghz.siteservice.enumeration;
public enum SiteType {
    FIXE, MOBILE;

    public static SiteType valueOfIgnoreCase(String value) {
        for (SiteType type : SiteType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant " + SiteType.class.getName() + "." + value);
    }
}




