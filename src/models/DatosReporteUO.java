package models;

public class DatosReporteUO {

    public Integer idEmpresa;
    public Integer zona;
    public Integer objeto;
    public Integer nivel;
    public Integer especialidad;
    public Integer subespecialidad;

    public Double costMateriales;
    public Double costMano;
    public Double costEquip;

    public DatosReporteUO(Integer idEmpresa, Integer zona, Integer objeto, Integer nivel, Integer especialidad, Integer subespecialidad, Double costMateriales, Double costMano, Double costEquip) {
        this.idEmpresa = idEmpresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.costMateriales = costMateriales;
        this.costMano = costMano;
        this.costEquip = costEquip;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getZona() {
        return zona;
    }

    public void setZona(Integer zona) {
        this.zona = zona;
    }

    public Integer getObjeto() {
        return objeto;
    }

    public void setObjeto(Integer objeto) {
        this.objeto = objeto;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Integer especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(Integer subespecialidad) {
        this.subespecialidad = subespecialidad;
    }

    public Double getCostMateriales() {
        return costMateriales;
    }

    public void setCostMateriales(Double costMateriales) {
        this.costMateriales = costMateriales;
    }

    public Double getCostMano() {
        return costMano;
    }

    public void setCostMano(Double costMano) {
        this.costMano = costMano;
    }

    public Double getCostEquip() {
        return costEquip;
    }

    public void setCostEquip(Double costEquip) {
        this.costEquip = costEquip;
    }
}