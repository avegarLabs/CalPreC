package models;

public class DatosPlanificacionMesUORV implements Comparable<DatosPlanificacionMesUORV> {

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
    public Double cantidad;
    public Double costMateriales;
    public Double costMano;
    public Double costEquipo;
    public Double costTotal;

    public String codeRV;
    public String descRV;
    public String umRV;
    public Double cantRVCert;
    public Double costMatRV;
    public Double costManoRV;
    public Double costEquipoRV;
    public Double costTotalCertRV;

    public String brigada;
    public String grupo;
    public String cuadrilla;

    public DatosPlanificacionMesUORV(Integer idEmpresa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, Integer idSub, String subespecialidad, Integer idUnidad, String unidad, String descripUnidad, String uoUM, Double cantidad, Double costMateriales, Double costMano, Double costEquipo, Double costTotal, String codeRV, String descRV, String umRV, Double cantRVCert, Double costMatRV, Double costManoRV, Double costEquipoRV, Double costTotalCertRV, String brigada, String grupo, String cuadrilla) {
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
        this.costMateriales = costMateriales;
        this.costMano = costMano;
        this.costEquipo = costEquipo;
        this.costTotal = costTotal;
        this.codeRV = codeRV;
        this.descRV = descRV;
        this.umRV = umRV;
        this.cantRVCert = cantRVCert;
        this.costMatRV = costMatRV;
        this.costManoRV = costManoRV;
        this.costEquipoRV = costEquipoRV;
        this.costTotalCertRV = costTotalCertRV;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
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

    public Double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(Double costEquipo) {
        this.costEquipo = costEquipo;
    }

    public Double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Double costTotal) {
        this.costTotal = costTotal;
    }

    public String getCodeRV() {
        return codeRV;
    }

    public void setCodeRV(String codeRV) {
        this.codeRV = codeRV;
    }

    public String getDescRV() {
        return descRV;
    }

    public void setDescRV(String descRV) {
        this.descRV = descRV;
    }

    public String getUmRV() {
        return umRV;
    }

    public void setUmRV(String umRV) {
        this.umRV = umRV;
    }

    public Double getCantRVCert() {
        return cantRVCert;
    }

    public void setCantRVCert(Double cantRVCert) {
        this.cantRVCert = cantRVCert;
    }

    public Double getCostMatRV() {
        return costMatRV;
    }

    public void setCostMatRV(Double costMatRV) {
        this.costMatRV = costMatRV;
    }

    public Double getCostManoRV() {
        return costManoRV;
    }

    public void setCostManoRV(Double costManoRV) {
        this.costManoRV = costManoRV;
    }

    public Double getCostEquipoRV() {
        return costEquipoRV;
    }

    public void setCostEquipoRV(Double costEquipoRV) {
        this.costEquipoRV = costEquipoRV;
    }

    public Double getCostTotalCertRV() {
        return costTotalCertRV;
    }

    public void setCostTotalCertRV(Double costTotalCertRV) {
        this.costTotalCertRV = costTotalCertRV;
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

    @Override
    public int compareTo(DatosPlanificacionMesUORV o) {
        if (o.getIdUnidad() > idUnidad) {
            return -1;
        } else if (o.getIdUnidad() > idUnidad) {
            return 0;
        } else {
            return 1;
        }
    }
}
