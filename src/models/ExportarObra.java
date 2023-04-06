package models;

import com.jfoenix.controls.JFXCheckBox;

public class ExportarObra {

    public JFXCheckBox empresa;
    public String descripcion;
    public String tipo;

    public ExportarObra(JFXCheckBox empresa, String descripcion, String tipo) {
        this.empresa = empresa;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public JFXCheckBox getEmpresa() {
        return empresa;
    }

    public void setEmpresa(JFXCheckBox empresa) {
        this.empresa = empresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    @Override
    public String toString() {
        return empresa.getText() + " " + descripcion;

    }


}
