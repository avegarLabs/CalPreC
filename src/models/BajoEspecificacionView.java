package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class BajoEspecificacionView extends RecursiveTreeObject<BajoEspecificacionView> {

    public IntegerProperty id;
    public IntegerProperty idUo;
    public IntegerProperty idSum;
    public IntegerProperty idRV;
    public StringProperty codigo;
    public StringProperty descripcion;
    public DoubleProperty precio;
    public StringProperty um;
    public DoubleProperty peso;
    public DoubleProperty cantidadBe;
    public DoubleProperty costoBe;
    public StringProperty tipo;


    public BajoEspecificacionView(int id, int idUo, int idSum, int idRV, String codigo, String descripcion, double precio, String um, double peso, double cantidadBe, double costoBe, String tipo) {
        this.id = new SimpleIntegerProperty(id);
        this.idUo = new SimpleIntegerProperty(idUo);
        this.idSum = new SimpleIntegerProperty(idSum);
        this.idRV = new SimpleIntegerProperty(idRV);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleDoubleProperty(precio);
        this.um = new SimpleStringProperty(um);
        this.peso = new SimpleDoubleProperty(peso);
        this.cantidadBe = new SimpleDoubleProperty(cantidadBe);
        this.costoBe = new SimpleDoubleProperty(costoBe);
        this.tipo = new SimpleStringProperty(tipo);
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

    public int getIdUo() {
        return idUo.get();
    }

    public void setIdUo(int idUo) {
        this.idUo.set(idUo);
    }

    public IntegerProperty idUoProperty() {
        return idUo;
    }

    public int getIdSum() {
        return idSum.get();
    }

    public void setIdSum(int idSum) {
        this.idSum.set(idSum);
    }

    public IntegerProperty idSumProperty() {
        return idSum;
    }

    public int getIdRV() {
        return idRV.get();
    }

    public void setIdRV(int idRV) {
        this.idRV.set(idRV);
    }

    public IntegerProperty idRVProperty() {
        return idRV;
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

    public double getCantidadBe() {
        return cantidadBe.get();
    }

    public void setCantidadBe(double cantidadBe) {
        this.cantidadBe.set(cantidadBe);
    }

    public DoubleProperty cantidadBeProperty() {
        return cantidadBe;
    }

    public double getCostoBe() {
        return costoBe.get();
    }

    public void setCostoBe(double costoBe) {
        this.costoBe.set(costoBe);
    }

    public DoubleProperty costoBeProperty() {
        return costoBe;
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

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public DoubleProperty precioProperty() {
        return precio;
    }
}
