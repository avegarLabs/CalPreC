package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CuadrillaConstruccionView extends RecursiveTreeObject<CuadrillaConstruccionView> {

    public IntegerProperty id;
    public StringProperty codigo;
    public StringProperty descripcion;
    public StringProperty grupo;
    public StringProperty brigada;
    public StringProperty empresa;

    public CuadrillaConstruccionView(int id, String codigo, String descripcion, String grupo, String brigada, String empresa) {
        this.id = new SimpleIntegerProperty(id);
        this.codigo = new SimpleStringProperty(codigo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.grupo = new SimpleStringProperty(grupo);
        this.brigada = new SimpleStringProperty(brigada);
        this.empresa = new SimpleStringProperty(empresa);
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

    public String getGrupo() {
        return grupo.get();
    }

    public void setGrupo(String grupo) {
        this.grupo.set(grupo);
    }

    public StringProperty grupoProperty() {
        return grupo;
    }

    public String getBrigada() {
        return brigada.get();
    }

    public void setBrigada(String brigada) {
        this.brigada.set(brigada);
    }

    public StringProperty brigadaProperty() {
        return brigada;
    }

    public String getEmpresa() {
        return empresa.get();
    }

    public void setEmpresa(String empresa) {
        this.empresa.set(empresa);
    }

    public StringProperty empresaProperty() {
        return empresa;
    }
}
