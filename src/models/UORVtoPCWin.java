package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UORVtoPCWin {

    private StringProperty code;
    private DoubleProperty cant;
    private DoubleProperty costo;

    public UORVtoPCWin(String code, Double cant, Double costo) {
        this.code = new SimpleStringProperty(code);
        this.cant = new SimpleDoubleProperty(cant);
        this.costo = new SimpleDoubleProperty(costo);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public double getCant() {
        return cant.get();
    }

    public void setCant(double cant) {
        this.cant.set(cant);
    }

    public DoubleProperty cantProperty() {
        return cant;
    }

    public double getCosto() {
        return costo.get();
    }

    public void setCosto(double costo) {
        this.costo.set(costo);
    }

    public DoubleProperty costoProperty() {
        return costo;
    }
}
