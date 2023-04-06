package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class SuministrosSemielaboradosView extends RecursiveTreeObject<SuministrosSemielaboradosView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty preciomn;
    public DoubleProperty preciomlc;
    public DoubleProperty peso;
    public String pertenece;

    public SuministrosSemielaboradosView(int id, String codigo, String descripcion, String um, double preciomn, double preciomlc, double peso, String pertenece) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.preciomn = new SimpleDoubleProperty(preciomn);
        this.preciomlc = new SimpleDoubleProperty(preciomlc);
        this.peso = new SimpleDoubleProperty(peso);
        this.pertenece = pertenece;
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

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        this.codigo.set(codigo);
    }

    public StringProperty codigoProperty() {
        return codigo;
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

    public double getPreciomn() {
        return preciomn.get();
    }

    public void setPreciomn(double preciomn) {
        this.preciomn.set(preciomn);
    }

    public DoubleProperty preciomnProperty() {
        return preciomn;
    }

    public double getPreciomlc() {
        return preciomlc.get();
    }

    public void setPreciomlc(double preciomlc) {
        this.preciomlc.set(preciomlc);
    }

    public DoubleProperty preciomlcProperty() {
        return preciomlc;
    }

    public double getPeso() {
        return peso.get();
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }

    public DoubleProperty pesoProperty() {
        return peso;
    }

    public String getPertenece() {
        return pertenece;
    }

    public void setPertenece(String pertenece) {
        this.pertenece = pertenece;
    }

    @Override
    public String toString() {
        return codigo + " " + descripcion;

    }
}
