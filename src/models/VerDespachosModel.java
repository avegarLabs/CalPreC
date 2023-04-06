package models;

import com.jfoenix.controls.JFXCheckBox;

public class VerDespachosModel {

    public int id;
    public String codigo;
    public Double cant;
    public String fecha;
    public String usuario;
    public JFXCheckBox estado;

    public VerDespachosModel(int id, String codigo, Double cant, String fecha, String usuario, JFXCheckBox estado) {
        this.id = id;
        this.codigo = codigo;
        this.cant = cant;
        this.fecha = fecha;
        this.usuario = usuario;
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

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public JFXCheckBox getEstado() {
        return estado;
    }

    public void setEstado(JFXCheckBox estado) {
        this.estado = estado;
    }
}
