package models;

public class DespachosGeneralReport {

    public String empresa;
    public String zona;
    public String objeto;
    public String especialidad;
    public String codeSum;
    public String descrip;
    public String um;
    public Double cant;
    public Double dispo;

    public DespachosGeneralReport(String empresa, String zona, String objeto, String especialidad, String codeSum, String descrip, String um, Double cant, Double dispo) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.especialidad = especialidad;
        this.codeSum = codeSum;
        this.descrip = descrip;
        this.um = um;
        this.cant = cant;
        this.dispo = dispo;
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

    public String getCodeSum() {
        return codeSum;
    }

    public void setCodeSum(String codeSum) {
        this.codeSum = codeSum;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public Double getDispo() {
        return dispo;
    }

    public void setDispo(Double dispo) {
        this.dispo = dispo;
    }
}
