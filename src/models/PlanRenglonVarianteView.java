package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class PlanRenglonVarianteView extends RecursiveTreeObject<PlanRenglonVarianteView> {

    public IntegerProperty id;
    public JFXCheckBox codigoRV;
    public DoubleProperty cantRV;
    public DoubleProperty disponible;
    public DoubleProperty cantCertificada;
    public DoubleProperty costoMaterial;
    public DoubleProperty costoMano;
    public DoubleProperty costoEquipo;
    public StringProperty descripcion;
    public DoubleProperty costoTotal;

    public PlanRenglonVarianteView(int id, JFXCheckBox codigoRV, Double cantRV, Double disponible, Double cantCertificada, Double costoMaterial, Double costoMano, Double costoEquipo, String descripcion, Double costoTotal) {
        this.id = new SimpleIntegerProperty(id);
        this.codigoRV = codigoRV;
        this.cantRV = new SimpleDoubleProperty(cantRV);
        this.disponible = new SimpleDoubleProperty(disponible);
        this.cantCertificada = new SimpleDoubleProperty(cantCertificada);
        this.costoMaterial = new SimpleDoubleProperty(costoMaterial);
        this.costoMano = new SimpleDoubleProperty(costoMano);
        this.costoEquipo = new SimpleDoubleProperty(costoEquipo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.costoTotal = new SimpleDoubleProperty(costoTotal);
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

    public double getCostoMano() {
        return costoMano.get();
    }

    public void setCostoMano(double costoMano) {
        this.costoMano.set(costoMano);
    }

    public DoubleProperty costoManoProperty() {
        return costoMano;
    }

    public double getCostoEquipo() {
        return costoEquipo.get();
    }

    public void setCostoEquipo(double costoEquipo) {
        this.costoEquipo.set(costoEquipo);
    }

    public DoubleProperty costoEquipoProperty() {
        return costoEquipo;
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

    public double getCostoTotal() {
        return costoTotal.get();
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal.set(costoTotal);
    }

    public DoubleProperty costoTotalProperty() {
        return costoTotal;
    }
}
