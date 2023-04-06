package AccessMigration.model;

import java.util.Objects;

public class SumBajoEspPCW {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subEspecialidad;
    public String codeUO;
    public String suminis;
    public Double cantidad;
    public Double costo;


    public SumBajoEspPCW(String empresa, String zona, String objeto, String nivel, String especialidad, String subEspecialidad, String codeUO, String suminis, Double cantidad, Double costo) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subEspecialidad = subEspecialidad;
        this.codeUO = codeUO;
        this.suminis = suminis;
        this.cantidad = cantidad;
        this.costo = costo;
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getSubEspecialidad() {
        return subEspecialidad;
    }

    public void setSubEspecialidad(String subEspecialidad) {
        this.subEspecialidad = subEspecialidad;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
    }

    public String getSuminis() {
        return suminis;
    }

    public void setSuminis(String suminis) {
        this.suminis = suminis;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SumBajoEspPCW that = (SumBajoEspPCW) o;
        return Objects.equals(empresa, that.empresa) &&
                Objects.equals(zona, that.zona) &&
                Objects.equals(objeto, that.objeto) &&
                Objects.equals(nivel, that.nivel) &&
                Objects.equals(especialidad, that.especialidad) &&
                Objects.equals(subEspecialidad, that.subEspecialidad) &&
                Objects.equals(codeUO, that.codeUO) &&
                Objects.equals(suminis, that.suminis) &&
                Objects.equals(cantidad, that.cantidad) &&
                Objects.equals(costo, that.costo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresa, zona, objeto, nivel, especialidad, subEspecialidad, codeUO, suminis, cantidad, costo);
    }
}
