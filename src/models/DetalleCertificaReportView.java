package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class DetalleCertificaReportView extends RecursiveTreeObject<DetalleCertificaReportView> {

    public IntegerProperty id;
    public StringProperty code;
    public StringProperty descripcion;
    public StringProperty um;
    public StringProperty label;
    public DoubleProperty cantidad;
    public DoubleProperty precio;
    public DoubleProperty costo;
    public StringProperty fini;
    public StringProperty ffin;


    public DetalleCertificaReportView(Integer id, String code, String descripcion, String um, String label, Double cantidad, Double precio, Double costo, String fini, String ffin) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.label = new SimpleStringProperty(label);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio);
        this.costo = new SimpleDoubleProperty(costo);
        this.fini = new SimpleStringProperty(fini);
        this.ffin = new SimpleStringProperty(ffin);
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

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
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

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public StringProperty labelProperty() {
        return label;
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

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public DoubleProperty precioProperty() {
        return precio;
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

    public String getFini() {
        return fini.get();
    }

    public void setFini(String fini) {
        this.fini.set(fini);
    }

    public StringProperty finiProperty() {
        return fini;
    }

    public String getFfin() {
        return ffin.get();
    }

    public void setFfin(String ffin) {
        this.ffin.set(ffin);
    }

    public StringProperty ffinProperty() {
        return ffin;
    }
}
