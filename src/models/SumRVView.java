package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class SumRVView extends RecursiveTreeObject<SumRVView> {

    public IntegerProperty id;
    public JFXCheckBox codigo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty costo;
    public DoubleProperty cantidad;
    public DoubleProperty usos;
    public StringProperty tipo;


    public SumRVView(int id, JFXCheckBox codigo, String descripcion, String um, Double costo, Double cantidad, Double usos, String tipo) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = codigo;
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.costo = new SimpleDoubleProperty(costo);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.usos = new SimpleDoubleProperty(usos);
        this.tipo = new SimpleStringProperty(tipo);

    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public JFXCheckBox getCodigo() {
        return codigo;
    }

    public void setCodigo(JFXCheckBox codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public String getUm() {
        return um.get();
    }

    public void setUm(String um) {
        this.um.set(um);
    }

    public StringProperty umProperty() {
        return um;
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

    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    public double getUsos() {
        return usos.get();
    }

    public void setUsos(double usos) {
        this.usos.set(usos);
    }

    public DoubleProperty usosProperty() {
        return usos;
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public StringProperty tipoProperty() {
        return tipo;
    }
}
