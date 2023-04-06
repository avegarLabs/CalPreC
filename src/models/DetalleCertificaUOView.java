package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class DetalleCertificaUOView extends RecursiveTreeObject<DetalleCertificaUOView> {

    public IntegerProperty id;
    public JFXCheckBox code;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty cantidad;
    public DoubleProperty costo;
    public DoubleProperty disp;
    public StringProperty fini;
    public StringProperty ffin;


    public DetalleCertificaUOView(Integer id, JFXCheckBox checkBox, String descrip, String um, Double cant, Double costo, Double cantDisponible, String fini, String ffin) {
        this.id = new SimpleIntegerProperty(id);
        this.code = checkBox;
        this.descripcion = new SimpleStringProperty(descrip);
        this.um = new SimpleStringProperty(um);
        this.cantidad = new SimpleDoubleProperty(cant);
        this.costo = new SimpleDoubleProperty(costo);
        this.disp = new SimpleDoubleProperty(cantDisponible);
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

    public JFXCheckBox getCode() {
        return code;
    }

    public void setCode(JFXCheckBox code) {
        this.code = code;
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
