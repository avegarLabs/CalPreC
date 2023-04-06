package models;

import com.jfoenix.controls.JFXCheckBox;

public class ValidarDespachosModel {

    public int id;
    public String codigo;
    public String descripcion;
    public Double cant;
    public JFXCheckBox estado;

    public ValidarDespachosModel(int id, String codigo, String descripcion, Double cant, JFXCheckBox estado) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cant = cant;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public JFXCheckBox getEstado() {
        return estado;
    }

    public void setEstado(JFXCheckBox estado) {
        this.estado = estado;
    }


}
