package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ManoObraRenglonVariante extends RecursiveTreeObject<ManoObraRenglonVariante> {

    public StringProperty codigo;
    public StringProperty escala;
    public DoubleProperty norma;
    public StringProperty descripcion;

    public ManoObraRenglonVariante(String codigo, String escala, double norma, String descripcion) {
        this.codigo = new SimpleStringProperty(codigo);
        this.escala = new SimpleStringProperty(escala);
        this.norma = new SimpleDoubleProperty(norma);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getEscala() {
        return escala.get();
    }

    public void setEscala(String escala) {
        this.escala.set(escala);
    }

    public StringProperty escalaProperty() {
        return escala;
    }

    public double getNorma() {
        return norma.get();
    }

    public void setNorma(double norma) {
        this.norma.set(norma);
    }

    public DoubleProperty normaProperty() {
        return norma;
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
}
