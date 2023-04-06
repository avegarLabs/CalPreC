package models;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

import java.util.Objects;


public class SuministrosView extends RecursiveTreeObject<SuministrosView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty peso;
    public DoubleProperty preciomn;
    public DoubleProperty preciomlc;
    public StringProperty escala;
    public StringProperty tipo;
    public StringProperty pertene;
    public DoubleProperty salario;
    public JFXCheckBox active;

    public SuministrosView(int id, String codigo, String descripcion, String um, double peso, double preciomn, double preciomlc, String escala, String tipo, String pertenece, double sal, JFXCheckBox active) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.peso = new SimpleDoubleProperty(peso);
        this.preciomn = new SimpleDoubleProperty(preciomn);
        this.preciomlc = new SimpleDoubleProperty(preciomlc);
        this.escala = new SimpleStringProperty(escala);
        this.tipo = new SimpleStringProperty(tipo);
        this.pertene = new SimpleStringProperty(pertenece);
        this.salario = new SimpleDoubleProperty(sal);
        this.active = active;
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

    public double getPeso() {
        return peso.get();
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }

    public DoubleProperty pesoProperty() {
        return peso;
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

    public String getEscala() {
        return escala.get();
    }

    public void setEscala(String escala) {
        this.escala.set(escala);
    }

    public StringProperty escalaProperty() {
        return escala;
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

    public String getPertene() {
        return pertene.get();
    }

    public void setPertene(String pertene) {
        this.pertene.set(pertene);
    }

    public StringProperty perteneProperty() {
        return pertene;
    }

    public double getSalario() {
        return salario.get();
    }

    public void setSalario(double salario) {
        this.salario.set(salario);
    }

    public DoubleProperty salarioProperty() {
        return salario;
    }

    public JFXCheckBox getActive() {
        return active;
    }

    public void setActive(JFXCheckBox active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuministrosView that = (SuministrosView) o;
        return Objects.equals(codigo, that.codigo) &&
                Objects.equals(tipo, that.tipo) &&
                Objects.equals(pertene, that.pertene);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, tipo, pertene);
    }

    @Override
    public String toString() {
        return codigo.getValue().trim() + " " + descripcion.getValue().trim() + " (" + preciomn.getValue() + " / " + um.getValue().trim() + " )";
    }
}
