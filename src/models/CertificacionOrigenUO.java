package models;

import java.util.ArrayList;

public class CertificacionOrigenUO {

    public Integer idEmpresa;
    public String empresa;
    public Integer idZona;
    public String zona;
    public Integer idObjeto;
    public String objeto;
    public Integer idNivel;
    public String nivel;
    public Integer idEspecialidad;
    public String especialidad;
    public Integer idSub;
    public String subespecialidad;
    public Integer idUnidad;
    public String unidad;
    public String descripUnidad;
    public String uoUM;
    public double cantidad;
    public double costMat;
    public double costMano;
    public double costEquip;
    public double total;

    public String brigada;
    public String grupo;
    public String cuadrilla;


    public ArrayList<ConceptosReporte> bodyConceptos;

    public CertificacionOrigenUO(Integer idEmpresa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, Integer idSub, String subespecialidad, Integer idUnidad, String unidad, String descripUnidad, String uoUM, double cantidad, double costMat, double costMano, double costEquip, double total, String brigada, String grupo, String cuadrilla, ArrayList<ConceptosReporte> bodyConceptos) {
        this.idEmpresa = idEmpresa;
        this.empresa = empresa;
        this.idZona = idZona;
        this.zona = zona;
        this.idObjeto = idObjeto;
        this.objeto = objeto;
        this.idNivel = idNivel;
        this.nivel = nivel;
        this.idEspecialidad = idEspecialidad;
        this.especialidad = especialidad;
        this.idSub = idSub;
        this.subespecialidad = subespecialidad;
        this.idUnidad = idUnidad;
        this.unidad = unidad;
        this.descripUnidad = descripUnidad;
        this.uoUM = uoUM;
        this.cantidad = cantidad;
        this.costMat = costMat;
        this.costMano = costMano;
        this.costEquip = costEquip;
        this.total = total;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
        this.bodyConceptos = bodyConceptos;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Integer getIdZona() {
        return idZona;
    }

    public void setIdZona(Integer idZona) {
        this.idZona = idZona;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Integer getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Integer idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Integer getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(Integer idNivel) {
        this.idNivel = idNivel;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Integer idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getIdSub() {
        return idSub;
    }

    public void setIdSub(Integer idSub) {
        this.idSub = idSub;
    }

    public String getSubespecialidad() {
        return subespecialidad;
    }

    public void setSubespecialidad(String subespecialidad) {
        this.subespecialidad = subespecialidad;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripUnidad() {
        return descripUnidad;
    }

    public void setDescripUnidad(String descripUnidad) {
        this.descripUnidad = descripUnidad;
    }

    public String getUoUM() {
        return uoUM;
    }

    public void setUoUM(String uoUM) {
        this.uoUM = uoUM;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
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

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCuadrilla() {
        return cuadrilla;
    }

    public void setCuadrilla(String cuadrilla) {
        this.cuadrilla = cuadrilla;
    }

    public ArrayList<ConceptosReporte> getBodyConceptos() {
        return bodyConceptos;
    }

    public void setBodyConceptos(ArrayList<ConceptosReporte> bodyConceptos) {
        this.bodyConceptos = bodyConceptos;
    }
}
