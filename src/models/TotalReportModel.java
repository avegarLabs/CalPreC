package models;

import java.util.Objects;

public class TotalReportModel {
    public int idEmpresa;
    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public double costMat;
    public double costMano;
    public double costEquip;
    private double total;

    public TotalReportModel(int idEmpresa, String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, double costMat, double costMano, double costEquip, double total) {
        this.idEmpresa = idEmpresa;
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.costMat = costMat;
        this.costMano = costMano;
        this.costEquip = costEquip;
        this.total = total;


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

    public double getCostMat() {
        return costMat;
    }

    public void setCostMat(double costMat) {
        this.costMat = costMat;
    }

    public double getCostMano() {
        return costMano;
    }

    public void setCostMano(double costMano) {
        this.costMano = costMano;
    }

    public double getCostEquip() {
        return costEquip;
    }

    public void setCostEquip(double costEquip) {
        this.costEquip = costEquip;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalReportModel that = (TotalReportModel) o;
        return Objects.equals(empresa, that.empresa) && Objects.equals(zona, that.zona) && Objects.equals(objeto, that.objeto) && Objects.equals(nivel, that.nivel) && Objects.equals(especialidad, that.especialidad) && Objects.equals(subespecialidad, that.subespecialidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresa, zona, objeto, nivel, especialidad, subespecialidad);
    }
}