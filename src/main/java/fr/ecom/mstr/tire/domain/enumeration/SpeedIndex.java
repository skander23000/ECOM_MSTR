package fr.ecom.mstr.tire.domain.enumeration;

/**
 * The SpeedIndex enumeration.
 */
public enum SpeedIndex {
    A_ONE("5"),
    A_EIGHT("40"),
    N("140"),
    Y("300");

    private final String value;

    SpeedIndex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
