package models;

import java.util.ArrayList;

public class PresupuestoUO {

    public Integer idEmpresa;
    public String empresaName;

    public Integer idZona;
    public String zona;

    public Integer idObjeto;
    public String objeto;

    public Integer idNivel;
    public String nivel;

    public Integer idEsp;
    public String especialidad;
    public Integer idSub;

    public String subespecialidad;
    public String codigo;
    public String descripcion;
    public String um;
    public Double cantidad;
    public Double costUnitario;
    public Double costTotal;

    public Double materiales;
    public Double mano;
    public Double equipo;

    public ArrayList<ConceptosReporte> bodyConceptos;

    public PresupuestoUO(Integer idEmpresa, String empresaName, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEsp, String especialidad, Integer idSub, String subespecialidad, String codigo, String descripcion, String um, Double cantidad, Double costUnitario, Double costTotal, Double materiales, Double mano, Double equipo, ArrayList<ConceptosReporte> bodyConceptos) {
        this.idEmpresa = idEmpresa;
        this.empresaName = empresaName;
        this.idZona = idZona;
        this.zona = zona;
        this.idObjeto = idObjeto;
        this.objeto = objeto;
        this.idNivel = idNivel;
        this.nivel = nivel;
        this.idEsp = idEsp;
        this.especialidad = especialidad;
        this.idSub = idSub;
        this.subespecialidad = subespecialidad;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.costUnitario = costUnitario;
        this.costTotal = costTotal;
        this.materiales = materiales;
        this.mano = mano;
        this.equipo = equipo;
        this.bodyConceptos = bodyConceptos;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmpresaName() {
        return empresaName;
    }

    public void setEmpresaName(String empresaName) {
        this.empresaName = empresaName;
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

    public Integer getIdEsp() {
        return idEsp;
    }

    public void setIdEsp(Integer idEsp) {
        this.idEsp = idEsp;
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

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCostUnitario() {
        return costUnitario;
    }

    public void setCostUnitario(Double costUnitario) {
        this.costUnitario = costUnitario;
    }

    public Double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Double costTotal) {
        this.costTotal = costTotal;
    }

    public Double getMateriales() {
        return materiales;
    }

    public void setMateriales(Double materiales) {
        this.materiales = materiales;
    }

    public Double getMano() {
        return mano;
    }

    public void setMano(Double mano) {
        this.mano = mano;
    }

    public Double getEquipo() {
        return equipo;
    }

    public void setEquipo(Double equipo) {
        this.equipo = equipo;
    }

    public ArrayList<ConceptosReporte> getBodyConceptos() {
        return bodyConceptos;
    }

    public void setBodyConceptos(ArrayList<ConceptosReporte> bodyConceptos) {
        this.bodyConceptos = bodyConceptos;
    }
}
