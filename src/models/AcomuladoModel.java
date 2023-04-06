package models;

public class AcomuladoModel {
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
    public String codigoUO;
    public String descripcionUO;
    public String umUO;
    public Double cant;
    public Double cantCert;
    public Double monto;
    public Double montocertf;
    public Double cantPend;
    public Double montoPend;

    public AcomuladoModel(Integer idEmpresa, String empresa, Integer idZona, String zona, Integer idObjeto, String objeto, Integer idNivel, String nivel, Integer idEspecialidad, String especialidad, Integer idSub, String subespecialidad, String codigoUO, String descripcionUO, String umUO, Double cant, Double cantCert, Double monto, Double montocertf, Double cantPend, Double montoPend) {
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
        this.codigoUO = codigoUO;
        this.descripcionUO = descripcionUO;
        this.umUO = umUO;
        this.cant = cant;
        this.cantCert = cantCert;
        this.monto = monto;
        this.montocertf = montocertf;
        this.cantPend = cantPend;
        this.montoPend = montoPend;
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

    public String getCodigoUO() {
        return codigoUO;
    }

    public void setCodigoUO(String codigoUO) {
        this.codigoUO = codigoUO;
    }

    public String getDescripcionUO() {
        return descripcionUO;
    }

    public void setDescripcionUO(String descripcionUO) {
        this.descripcionUO = descripcionUO;
    }

    public String getUmUO() {
        return umUO;
    }

    public void setUmUO(String umUO) {
        this.umUO = umUO;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public Double getCantCert() {
        return cantCert;
    }

    public void setCantCert(Double cantCert) {
        this.cantCert = cantCert;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Double getMontocertf() {
        return montocertf;
    }

    public void setMontocertf(Double montocertf) {
        this.montocertf = montocertf;
    }

    public Double getCantPend() {
        return cantPend;
    }

    public void setCantPend(Double cantPend) {
        this.cantPend = cantPend;
    }

    public Double getMontoPend() {
        return montoPend;
    }

    public void setMontoPend(Double montoPend) {
        this.montoPend = montoPend;
    }
}
