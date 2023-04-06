package models;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class UOPCWINView {

    private JFXCheckBox codeBox;
    private DoubleProperty cant;
    private DoubleProperty costo;

    public UOPCWINView(JFXCheckBox codeBox, Double cant, Double costo) {
        this.codeBox = codeBox;
        this.cant = new SimpleDoubleProperty(cant);
        this.costo = new SimpleDoubleProperty(costo);
    }

    public JFXCheckBox getCodeBox() {
        return codeBox;
    }

    public void setCodeBox(JFXCheckBox codeBox) {
        this.codeBox = codeBox;
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
