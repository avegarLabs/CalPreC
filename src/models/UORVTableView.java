package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class UORVTableView extends RecursiveTreeObject<UORVTableView> {

    private IntegerProperty id;
    private StringProperty codeRV;
    private DoubleProperty cant;
    private DoubleProperty costoTotal;
    private StringProperty descripcion;
    private DoubleProperty peso;
    private StringProperty um;
    private DoubleProperty costoTotalRV;
    private StringProperty isBase;
    private DoubleProperty salario;
    private DoubleProperty salariocuc;
    private StringProperty conMat;
    private StringProperty sobreG;
    private StringProperty grupo;
    private StringProperty subGrupo;

    public UORVTableView(int id, String codeRV, Double cant, Double costoTotal, String descripcion, Double peso, String um, Double costoTotalRV, String isBase, Double salario, Double salariocuc, String conMat, String sobreG, String grupo, String subGrupo) {
        this.id = new SimpleIntegerProperty(id);
        this.codeRV = new SimpleStringProperty(codeRV);
        this.cant = new SimpleDoubleProperty(cant);
        this.costoTotal = new SimpleDoubleProperty(costoTotal);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.peso = new SimpleDoubleProperty(peso);
        this.um = new SimpleStringProperty(um);
        this.costoTotalRV = new SimpleDoubleProperty(costoTotalRV);
        this.isBase = new SimpleStringProperty(isBase);
        this.salario = new SimpleDoubleProperty(salario);
        this.salariocuc = new SimpleDoubleProperty(salariocuc);
        this.conMat = new SimpleStringProperty(conMat);
        this.sobreG = new SimpleStringProperty(sobreG);
        this.grupo = new SimpleStringProperty(grupo);
        this.subGrupo = new SimpleStringProperty(subGrupo);
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

    public String getCodeRV() {
        return codeRV.get();
    }

    public void setCodeRV(String codeRV) {
        this.codeRV.set(codeRV);
    }

    public StringProperty codeRVProperty() {
        return codeRV;
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

    public double getCostoTotal() {
        return costoTotal.get();
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal.set(costoTotal);
    }

    public DoubleProperty costoTotalProperty() {
        return costoTotal;
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

    public double getPeso() {
        return peso.get();
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }

    public DoubleProperty pesoProperty() {
        return peso;
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

    public double getCostoTotalRV() {
        return costoTotalRV.get();
    }

    public void setCostoTotalRV(double costoTotalRV) {
        this.costoTotalRV.set(costoTotalRV);
    }

    public DoubleProperty costoTotalRVProperty() {
        return costoTotalRV;
    }

    public String getIsBase() {
        return isBase.get();
    }

    public void setIsBase(String isBase) {
        this.isBase.set(isBase);
    }

    public StringProperty isBaseProperty() {
        return isBase;
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

    public double getSalariocuc() {
        return salariocuc.get();
    }

    public void setSalariocuc(double salariocuc) {
        this.salariocuc.set(salariocuc);
    }

    public DoubleProperty salariocucProperty() {
        return salariocuc;
    }

    public String getConMat() {
        return conMat.get();
    }

    public void setConMat(String conMat) {
        this.conMat.set(conMat);
    }

    public StringProperty conMatProperty() {
        return conMat;
    }

    public String getSobreG() {
        return sobreG.get();
    }

    public void setSobreG(String sobreG) {
        this.sobreG.set(sobreG);
    }

    public StringProperty sobreGProperty() {
        return sobreG;
    }

    public String getGrupo() {
        return grupo.get();
    }

    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }

    public StringProperty grupoProperty() {
        return grupo;
    }

    public String getSubGrupo() {
        return subGrupo.get();
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo.set(subGrupo);
    }

    public StringProperty subGrupoProperty() {
        return subGrupo;
    }
}
