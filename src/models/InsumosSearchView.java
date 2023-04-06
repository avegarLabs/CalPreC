package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InsumosSearchView {

    public StringProperty zona;
    public StringProperty objeto;
    public StringProperty nivel;
    public StringProperty especialidad;
    public StringProperty subespecialidad;
    public StringProperty unidadObra;
    public StringProperty suministro;
    public StringProperty renglon;
    public DoubleProperty cant;
    public DoubleProperty cost;

    public InsumosSearchView(String zona, String objeto, String nivel, String especialidad, String subespecialidad, String unidadObra, String sumin, String renglon, Double cant, Double cost) {
        this.zona = new SimpleStringProperty(zona);
        this.objeto = new SimpleStringProperty(objeto);
        this.nivel = new SimpleStringProperty(nivel);
        this.especialidad = new SimpleStringProperty(especialidad);
        this.subespecialidad = new SimpleStringProperty(subespecialidad);
        this.unidadObra = new SimpleStringProperty(unidadObra);
        this.suministro = new SimpleStringProperty(sumin);
        this.renglon = new SimpleStringProperty(renglon);
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

    public String getSuministro() {
        return suministro.get();
    }

    public void setSuministro(String suministro) {
        this.suministro.set(suministro);
    }

    public StringProperty suministroProperty() {
        return suministro;
    }

    public String getRenglon() {
        return renglon.get();
    }

    public void setRenglon(String renglon) {
        this.renglon.set(renglon);
    }

    public StringProperty renglonProperty() {
        return renglon;
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
