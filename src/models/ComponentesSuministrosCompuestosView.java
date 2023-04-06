package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class ComponentesSuministrosCompuestosView extends RecursiveTreeObject<ComponentesSuministrosCompuestosView> {
    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty preciomn;
    public DoubleProperty preciomlc;
    public DoubleProperty cantidad;
    public DoubleProperty uso;
    public StringProperty tag;
    public StringProperty tipo;
    public DoubleProperty peso;

    public ComponentesSuministrosCompuestosView(int id, String codigo, String descripcion, String um, double preciomn, double preciomlc, double cantidad, double uso, String tag, String tipo, double peso) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.preciomn = new SimpleDoubleProperty(preciomn);
        this.preciomlc = new SimpleDoubleProperty(preciomlc);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.uso = new SimpleDoubleProperty(uso);
        this.tag = new SimpleStringProperty(tag);
        this.tipo = new SimpleStringProperty(tipo);
        this.peso = new SimpleDoubleProperty(peso);
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

    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
    }

    public double getUso() {
        return uso.get();
    }

    public void setUso(double uso) {
        this.uso.set(uso);
    }

    public DoubleProperty usoProperty() {
        return uso;
    }

    public String getTag() {
        return tag.get();
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public StringProperty tagProperty() {
        return tag;
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

    public double getPeso() {
        return peso.get();
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }

    public DoubleProperty pesoProperty() {
        return peso;
    }
}
