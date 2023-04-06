package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class DetalleCertificaView extends RecursiveTreeObject<DetalleCertificaView> {

    public IntegerProperty id;
    public StringProperty code;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty cantidad;
    public DoubleProperty costo;
    public DoubleProperty disp;
    public StringProperty fini;
    public StringProperty ffin;


    public DetalleCertificaView(Integer id, String code, String descripcion, String um, Double cantidad, Double costo, Double disp, String fini, String ffin) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.costo = new SimpleDoubleProperty(costo);
        this.disp = new SimpleDoubleProperty(disp);
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

    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
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

    public double getDisp() {
        return disp.get();
    }

    public void setDisp(double disp) {
        this.disp.set(disp);
    }

    public DoubleProperty dispProperty() {
        return disp;
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
