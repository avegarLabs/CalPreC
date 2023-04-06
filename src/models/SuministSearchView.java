package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SuministSearchView {

    public StringProperty zona;
    public StringProperty objeto;
    public StringProperty nivel;
    public StringProperty especialidad;
    public StringProperty subespecialidad;
    public StringProperty unidadObra;
    public DoubleProperty cant;
    public DoubleProperty cost;

    public SuministSearchView(String zona, String objeto, String nivel, String especialidad, String subespecialidad, String unidadObra, Double cant, Double cost) {
        this.zona = new SimpleStringProperty(zona);
        this.objeto = new SimpleStringProperty(objeto);
        this.nivel = new SimpleStringProperty(nivel);
        this.especialidad = new SimpleStringProperty(especialidad);
        this.subespecialidad = new SimpleStringProperty(subespecialidad);
        this.unidadObra = new SimpleStringProperty(unidadObra);
        this.cant = new SimpleDoubleProperty(cant);
        this.cost = new SimpleDoubleProperty(cost);
    }

    public String getZona() {
        return zona.get();
    }

    public void setZona(String zona) {
        this.zona.set(zona);
    }

    public StringProperty zonaProperty() {
        return zona;
    }

    public String getObjeto() {
        return objeto.get();
    }

    public void setObjeto(String objeto) {
        this.objeto.set(objeto);
    }

    public StringProperty objetoProperty() {
        return objeto;
    }

    public String getNivel() {
        return nivel.get();
    }

    public void setNivel(String nivel) {
        this.nivel.set(nivel);
    }

    public StringProperty nivelProperty() {
        return nivel;
    }

    public String getEspecialidad() {
        return especialidad.get();
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad.set(especialidad);
    }

    public StringProperty especialidadProperty() {
        return especialidad;
    }

    public String getSubespecialidad() {
        return subespecialidad.get();
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad.set(subespecialidad);
    }

    public StringProperty subespecialidadProperty() {
        return subespecialidad;
    }

    public String getUnidadObra() {
        return unidadObra.get();
    }

    public void setUnidadObra(String unidadObra) {
        this.unidadObra.set(unidadObra);
    }

    public StringProperty unidadObraProperty() {
        return unidadObra;
    }

    public double getCant() {
        return cant.get();
    }

    public void setCant(double cant) {
        this.cant.set(cant);
    }

    public DoubleProperty cantProperty() {
        return cant;
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }
}
