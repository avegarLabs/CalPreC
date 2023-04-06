package models;

import java.util.Objects;

public class CLGenericModel {
    public String obra;
    public String empresa;
    public String zona;
    public String objeto;
    public String especialidad;

    public CLGenericModel(String obra, String empresa, String zona, String objeto, String especialidad) {
        this.obra = obra;
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.especialidad = especialidad;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CLGenericModel that = (CLGenericModel) o;
        return Objects.equals(obra, that.obra) && Objects.equals(empresa, that.empresa) && Objects.equals(zona, that.zona) && Objects.equals(objeto, that.objeto) && Objects.equals(especialidad, that.especialidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obra, empresa, zona, objeto, especialidad);
    }
}
