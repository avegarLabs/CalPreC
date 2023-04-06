package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class UOSubespecialidadesValoresView extends RecursiveTreeObject<UOSubespecialidadesValoresView> {

    public IntegerProperty id;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty cantida;
    public DoubleProperty materiales;
    public DoubleProperty mano;
    public DoubleProperty equipos;


    public UOSubespecialidadesValoresView(int id, String descripcion, String um, Double cantida, Double materiales, Double mano, Double equipos) {
        this.id = new SimpleIntegerProperty(id);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.cantida = new SimpleDoubleProperty(cantida);
        this.materiales = new SimpleDoubleProperty(materiales);
        this.mano = new SimpleDoubleProperty(mano);
        this.equipos = new SimpleDoubleProperty(equipos);
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

    public double getCantida() {
        return cantida.get();
    }

    public void setCantida(double cantida) {
        this.cantida.set(cantida);
    }

    public DoubleProperty cantidaProperty() {
        return cantida;
    }

    public double getMateriales() {
        return materiales.get();
    }

    public void setMateriales(double materiales) {
        this.materiales.set(materiales);
    }

    public DoubleProperty materialesProperty() {
        return materiales;
    }

    public double getMano() {
        return mano.get();
    }

    public void setMano(double mano) {
        this.mano.set(mano);
    }

    public DoubleProperty manoProperty() {
        return mano;
    }

    public double getEquipos() {
        return equipos.get();
    }

    public void setEquipos(double equipos) {
        this.equipos.set(equipos);
    }

    public DoubleProperty equiposProperty() {
        return equipos;
    }
}
