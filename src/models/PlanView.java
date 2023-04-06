package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class PlanView extends RecursiveTreeObject<PlanView> {

    public IntegerProperty id;
    public StringProperty codeUo;
    public StringProperty descripcion;
    public StringProperty um;
    public DoubleProperty cantidad;
    public DoubleProperty costmaterial;
    public DoubleProperty costmano;
    public DoubleProperty costequipo;
    public DoubleProperty salario;
    public DoubleProperty cucsalario;
    private StringProperty desde;
    private StringProperty hasta;
    private DoubleProperty disponible;


    public PlanView(Integer id, String codeUo, String descripcion, String um, Double cantidad, Double costmaterial, Double costmano, Double costequipo, Double salario, Double cucsalario, String desde, String hasta, double disponible) {
        this.id = new SimpleIntegerProperty(id);
        this.codeUo = new SimpleStringProperty(codeUo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.costmaterial = new SimpleDoubleProperty(costmaterial);
        this.costmano = new SimpleDoubleProperty(costmano);
        this.costequipo = new SimpleDoubleProperty(costequipo);
        this.salario = new SimpleDoubleProperty(salario);
        this.cucsalario = new SimpleDoubleProperty(cucsalario);
        this.desde = new SimpleStringProperty(desde);
        this.hasta = new SimpleStringProperty(hasta);
        this.disponible = new SimpleDoubleProperty(disponible);
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

    public String getCodeUo() {
        return codeUo.get();
    }

    public void setCodeUo(String codeUo) {
        this.codeUo.set(codeUo);
    }

    public StringProperty codeUoProperty() {
        return codeUo;
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

    public double getCostmaterial() {
        return costmaterial.get();
    }

    public void setCostmaterial(double costmaterial) {
        this.costmaterial.set(costmaterial);
    }

    public DoubleProperty costmaterialProperty() {
        return costmaterial;
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

    public double getCostequipo() {
        return costequipo.get();
    }

    public void setCostequipo(double costequipo) {
        this.costequipo.set(costequipo);
    }

    public DoubleProperty costequipoProperty() {
        return costequipo;
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

    public double getCucsalario() {
        return cucsalario.get();
    }

    public void setCucsalario(double cucsalario) {
        this.cucsalario.set(cucsalario);
    }

    public DoubleProperty cucsalarioProperty() {
        return cucsalario;
    }

    public String getDesde() {
        return desde.get();
    }

    public void setDesde(String desde) {
        this.desde.set(desde);
    }

    public StringProperty desdeProperty() {
        return desde;
    }

    public String getHasta() {
        return hasta.get();
    }

    public void setHasta(String hasta) {
        this.hasta.set(hasta);
    }

    public StringProperty hastaProperty() {
        return hasta;
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
}
