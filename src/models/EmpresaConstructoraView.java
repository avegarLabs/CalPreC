package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class EmpresaConstructoraView extends RecursiveTreeObject<EmpresaConstructoraView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public DoubleProperty cuenta731;
    public DoubleProperty cuenta822;
    public DoubleProperty pbruta;


    public EmpresaConstructoraView(int id, String codigo, String descripcion, double cuenta731, double cuenta822, double pbruta) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.cuenta731 = new SimpleDoubleProperty(cuenta731);
        this.cuenta822 = new SimpleDoubleProperty(cuenta822);
        this.pbruta = new SimpleDoubleProperty(pbruta);
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

    public double getCuenta731() {
        return cuenta731.get();
    }

    public void setCuenta731(double cuenta731) {
        this.cuenta731.set(cuenta731);
    }

    public DoubleProperty cuenta731Property() {
        return cuenta731;
    }

    public double getCuenta822() {
        return cuenta822.get();
    }

    public void setCuenta822(double cuenta822) {
        this.cuenta822.set(cuenta822);
    }

    public DoubleProperty cuenta822Property() {
        return cuenta822;
    }

    public double getPbruta() {
        return pbruta.get();
    }

    public void setPbruta(double pbruta) {
        this.pbruta.set(pbruta);
    }

    public DoubleProperty pbrutaProperty() {
        return pbruta;
    }
}
