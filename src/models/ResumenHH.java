package models;

public class ResumenHH {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;

    public double hhCant;
    public double heCant;
    public double hhCert;
    public double heCert;
    public double hhDisp;
    public double heDisp;

    public ResumenHH(String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, double hhCant, double heCant, double hhCert, double heCert, double hhDisp, double heDisp) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.hhCant = hhCant;
        this.heCant = heCant;
        this.hhCert = hhCert;
        this.heCert = heCert;
        this.hhDisp = hhDisp;
        this.heDisp = heDisp;
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

    public double getHhCant() {
        return hhCant;
    }

    public void setHhCant(double hhCant) {
        this.hhCant = hhCant;
    }

    public double getHeCant() {
        return heCant;
    }

    public void setHeCant(double heCant) {
        this.heCant = heCant;
    }

    public double getHhCert() {
        return hhCert;
    }

    public void setHhCert(double hhCert) {
        this.hhCert = hhCert;
    }

    public double getHeCert() {
        return heCert;
    }

    public void setHeCert(double heCert) {
        this.heCert = heCert;
    }

    public double getHhDisp() {
        return hhDisp;
    }

    public void setHhDisp(double hhDisp) {
        this.hhDisp = hhDisp;
    }

    public double getHeDisp() {
        return heDisp;
    }

    public void setHeDisp(double heDisp) {
        this.heDisp = heDisp;
    }
}
