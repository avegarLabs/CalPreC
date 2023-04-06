package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class RenglonVarianteUOView extends RecursiveTreeObject<RenglonVarianteUOView> {

    private IntegerProperty id;
    private StringProperty codigo;
    private StringProperty descripcion;
    private StringProperty um;
    private DoubleProperty cantidad;
    private DoubleProperty costomat;
    private DoubleProperty costmano;
    private DoubleProperty costequip;
    private DoubleProperty total;

    public RenglonVarianteUOView(int id, String codigo, String descripcion, String um, double cantidad, double costomat, double costmano, double costequip, double total) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.um = new SimpleStringProperty(um);
        this.cantidad = new SimpleDoubleProperty(cantidad);
        this.costomat = new SimpleDoubleProperty(costomat);
        this.costmano = new SimpleDoubleProperty(costmano);
        this.costequip = new SimpleDoubleProperty(costequip);
        this.total = new SimpleDoubleProperty(total);
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

    public double getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public DoubleProperty cantidadProperty() {
        return cantidad;
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

    public double getTotal() {
        return total.get();
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
