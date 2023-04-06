package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ControlPresupResumenView extends RecursiveTreeObject<ControlPresupResumenView> {

    public StringProperty presup;
    public DoubleProperty valor;
    public StringProperty fecha;

    public ControlPresupResumenView(String presup, Double valor, String fecha) {
        this.presup = new SimpleStringProperty(presup);
        this.valor = new SimpleDoubleProperty(valor);
        this.fecha = new SimpleStringProperty(fecha);
    }

    public String getPresup() {
        return presup.get();
    }

    public void setPresup(String presup) {
        this.presup.set(presup);
    }

    public StringProperty presupProperty() {
        return presup;
    }

    public double getValor() {
        return valor.get();
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }

    public DoubleProperty valorProperty() {
        return valor;
    }

    public String getFecha() {
        return fecha.get();
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public StringProperty fechaProperty() {
        return fecha;
    }
}
