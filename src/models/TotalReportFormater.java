package models;

import java.util.ArrayList;
import java.util.Objects;

public class TotalReportFormater {
    public int idEmpresa;
    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public double materiales;
    public double mano;
    public double equipos;
    public double costoTotal;
    public ArrayList<ConceptosReporte> bodyConceptos;

    public TotalReportFormater(int idEmpresa, String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, double materiales, double mano, double equipos, double costoTotal, ArrayList<ConceptosReporte> bodyConceptos) {
        this.idEmpresa = idEmpresa;
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.materiales = materiales;
        this.mano = mano;
        this.equipos = equipos;
        this.costoTotal = costoTotal;
        this.bodyConceptos = bodyConceptos;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    public String getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad = subespecialidad;
    }

    public double getMateriales() {
        return materiales;
    }

    public void setMateriales(double materiales) {
        this.materiales = materiales;
    }

    public double getMano() {
        return mano;
    }

    public void setMano(double mano) {
        this.mano = mano;
    }

    public double getEquipos() {
        return equipos;
    }

    public void setEquipos(double equipos) {
        this.equipos = equipos;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public ArrayList<ConceptosReporte> getBodyConceptos() {
        return bodyConceptos;
    }

    public void setBodyConceptos(ArrayList<ConceptosReporte> bodyConceptos) {
        this.bodyConceptos = bodyConceptos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalReportFormater that = (TotalReportFormater) o;
        return idEmpresa == that.idEmpresa &&
                Double.compare(that.materiales, materiales) == 0 &&
                Double.compare(that.mano, mano) == 0 &&
                Double.compare(that.equipos, equipos) == 0 &&
                Double.compare(that.costoTotal, costoTotal) == 0 &&
                empresa.equals(that.empresa) &&
                zona.equals(that.zona) &&
                objeto.equals(that.objeto) &&
                nivel.equals(that.nivel) &&
                especialidad.equals(that.especialidad) &&
                subespecialidad.equals(that.subespecialidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpresa, empresa, zona, objeto, nivel, especialidad, subespecialidad, materiales, mano, equipos, costoTotal);
    }
}
