package models;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class RenglonVarianteSearchView extends RecursiveTreeObject<RenglonVarianteSearchView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty costomat;
    public DoubleProperty costmano;
    public DoubleProperty costequip;
    public DoubleProperty salario;
    public JFXButton view;

    public RenglonVarianteSearchView(int id, String codigo, String descripcion, String um, double costomat, double costmano, double costequip, double salario, JFXButton button) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.costomat = new SimpleDoubleProperty(costomat);
        this.costmano = new SimpleDoubleProperty(costmano);
        this.costequip = new SimpleDoubleProperty(costequip);
        this.salario = new SimpleDoubleProperty(salario);
        this.view = button;
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

    public double getCostomat() {
        return costomat.get();
    }

    public void setCostomat(double costomat) {
        this.costomat.set(costomat);
    }

    public DoubleProperty costomatProperty() {
        return costomat;
    }

    public double getCostmano() {
        return costmano.get();
    }

    public void setCostmano(double costmano) {
        this.costmano.set(costmano);
    }

    public DoubleProperty costmanoProperty() {
        return costmano;
    }

    public double getCostequip() {
        return costequip.get();
    }

    public void setCostequip(double costequip) {
        this.costequip.set(costequip);
    }

    public DoubleProperty costequipProperty() {
        return costequip;
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

    public JFXButton getView() {
        return view;
    }

    public void setView(JFXButton view) {
        this.view = view;
    }
}
