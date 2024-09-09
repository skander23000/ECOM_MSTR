package fr.ecom.mstr.tire.domain.enumeration;

/**
 * The ChargeIndex enumeration.
 */
public enum ChargeIndex {
    TWENTY("80"),
    SIXTY("250"),
    NINETY("600"),
    ONE_HUNDRED_TWENTY("1400");

    private final String value;

    ChargeIndex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
