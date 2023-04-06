package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class UOSUMtoPCWin {

    private StringProperty code;
    private DoubleProperty cant;
    private DoubleProperty costo;

    public UOSUMtoPCWin(String code, Double cant, Double costo) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UOSUMtoPCWin that = (UOSUMtoPCWin) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(cant, that.cant) &&
                Objects.equals(costo, that.costo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, cant, costo);
    }
}
