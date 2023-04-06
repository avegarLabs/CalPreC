package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;

public class ManoObraView extends RecursiveTreeObject<ManoObraView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty grupoesc;
    public DoubleProperty preciomn;
    public DoubleProperty preciomlc;
    public StringProperty um;
    public StringProperty idEmp;


    public ManoObraView(int id, String codigo, String descripcion, String grupoesc, double preciomn, double preciomlc, String um, String ide) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.grupoesc = new SimpleStringProperty(grupoesc);
        this.preciomn = new SimpleDoubleProperty(preciomn);
        this.preciomlc = new SimpleDoubleProperty(preciomlc);
        this.um = new SimpleStringProperty(um);
        this.idEmp = new SimpleStringProperty(ide);
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

    public String getGrupoesc() {
        return grupoesc.get();
    }

    public void setGrupoesc(String grupoesc) {
        this.grupoesc.set(grupoesc);
    }

    public StringProperty grupoescProperty() {
        return grupoesc;
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

    public String getUm() {
        return um.get();
    }

    public void setUm(String um) {
        this.um.set(um);
    }

    public StringProperty umProperty() {
        return um;
    }

    public String getIdEmp() {
        return idEmp.get();
    }

    public void setIdEmp(String idEmp) {
        this.idEmp.set(idEmp);
    }

    public StringProperty idEmpProperty() {
        return idEmp;
    }
}
