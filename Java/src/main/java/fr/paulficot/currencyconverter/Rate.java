package fr.paulficot.currencyconverter;

public class Rate {

    /** nom de la devise */
    private String name;

    /** valeur de la devise par rapport à l'EURO */
    private Double value;

    /**
     * Constructeur de l'objet rate
     *
     * @param name nom de la devise
     * @param value valeur de la devise par rapport à l'EURO
     */
    public Rate(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public String toString() {
        return getName() + " | " + getValue();
    }

}
