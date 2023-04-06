package models;

public class CertificoModel {

    public String empresa;
    public String brigada;
    public String grupo;
    public String cuadrilla;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String uoCode;
    public String uoDescrip;
    public Double certVolum;
    public String um;
    public Double hhAndHE;
    public Double salariomn;
    public Double salarioInc;
    public Double salariocuc;
    public Double costo;

    public CertificoModel(String empresa, String brigada, String grupo, String cuadrilla, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String uoCode, String uoDescrip, Double certVolum, String um, Double hhAndHE, Double salariomn, Double salarioInc, Double salariocuc, Double costo) {
        this.empresa = empresa;
        this.brigada = brigada;
        this.grupo = grupo;
        this.cuadrilla = cuadrilla;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.uoCode = uoCode;
        this.uoDescrip = uoDescrip;
        this.certVolum = certVolum;
        this.um = um;
        this.hhAndHE = hhAndHE;
        this.salariomn = salariomn;
        this.salarioInc = salarioInc;
        this.salariocuc = salariocuc;
        this.costo = costo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
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

    public String getUoCode() {
        return uoCode;
    }

    public void setUoCode(String uoCode) {
        this.uoCode = uoCode;
    }

    public String getUoDescrip() {
        return uoDescrip;
    }

    public void setUoDescrip(String uoDescrip) {
        this.uoDescrip = uoDescrip;
    }

    public Double getCertVolum() {
        return certVolum;
    }

    public void setCertVolum(Double certVolum) {
        this.certVolum = certVolum;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Double getHhAndHE() {
        return hhAndHE;
    }

    public void setHhAndHE(Double hhAndHE) {
        this.hhAndHE = hhAndHE;
    }

    public Double getSalariomn() {
        return salariomn;
    }

    public void setSalariomn(Double salariomn) {
        this.salariomn = salariomn;
    }

    public Double getSalarioInc() {
        return salarioInc;
    }

    public void setSalarioInc(Double salarioInc) {
        this.salarioInc = salarioInc;
    }

    public Double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(Double salariocuc) {
        this.salariocuc = salariocuc;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }
}
