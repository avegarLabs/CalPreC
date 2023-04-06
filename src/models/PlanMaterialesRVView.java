package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class PlanMaterialesRVView extends RecursiveTreeObject<PlanMaterialesRVView> {

    public IntegerProperty id;
    public JFXCheckBox codigoRV;
    public DoubleProperty cantRV;
    public DoubleProperty disponible;
    public DoubleProperty cantCertificada;
    public DoubleProperty costoMaterial;
    private StringProperty descripcion;
    private DoubleProperty costo;


    public PlanMaterialesRVView(int id, JFXCheckBox codigoRV, Double cantRV, Double disponible, Double cantCertificada, Double costoMaterial, String descripcion, Double costo) {
        this.id = new SimpleIntegerProperty(id);
        this.codigoRV = codigoRV;
        this.cantRV = new SimpleDoubleProperty(cantRV);
        this.disponible = new SimpleDoubleProperty(disponible);
        this.cantCertificada = new SimpleDoubleProperty(cantCertificada);
        this.costoMaterial = new SimpleDoubleProperty(costoMaterial);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.costo = new SimpleDoubleProperty(costo);
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

    public JFXCheckBox getCodigoRV() {
        return codigoRV;
    }

    public void setCodigoRV(JFXCheckBox codigoRV) {
        this.codigoRV = codigoRV;
    }

    public double getCantRV() {
        return cantRV.get();
    }

    public void setCantRV(double cantRV) {
        this.cantRV.set(cantRV);
    }

    public DoubleProperty cantRVProperty() {
        return cantRV;
    }

    public double getDisponible() {
        return disponible.get();
    }

    public void setDisponible(double disponible) {
        this.disponible.set(disponible);
    }

    public DoubleProperty disponibleProperty() {
        return disponible;
    }

    public double getCantCertificada() {
        return cantCertificada.get();
    }

    public void setCantCertificada(double cantCertificada) {
        this.cantCertificada.set(cantCertificada);
    }

    public DoubleProperty cantCertificadaProperty() {
        return cantCertificada;
    }

    public double getCostoMaterial() {
        return costoMaterial.get();
    }

    public void setCostoMaterial(double costoMaterial) {
        this.costoMaterial.set(costoMaterial);
    }

    public DoubleProperty costoMaterialProperty() {
        return costoMaterial;
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
