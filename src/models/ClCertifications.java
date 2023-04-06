package models;

public class ClCertifications {
    public String empresa;
    public String zona;
    public String objeto;
    public String especialidad;
    public String recurso;
    public String descripcion;
    public String um;
    public double presupuesto;
    public double certificacion;
    public double despachos;
    public double diferenciaCert;
    public double diferenciaDes;

    public ClCertifications(String empresa, String zona, String objeto, String especialidad, String recurso, String descripcion, String um, double presupuesto, double certificacion, double despachos, double diferenciaCert, double diferenciaDes) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.especialidad = especialidad;
        this.recurso = recurso;
        this.descripcion = descripcion;
        this.um = um;
        this.presupuesto = presupuesto;
        this.certificacion = certificacion;
        this.despachos = despachos;
        this.diferenciaCert = diferenciaCert;
        this.diferenciaDes = diferenciaDes;
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

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
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

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(double certificacion) {
        this.certificacion = certificacion;
    }

    public double getDespachos() {
        return despachos;
    }

    public void setDespachos(double despachos) {
        this.despachos = despachos;
    }

    public double getDiferenciaCert() {
        return diferenciaCert;
    }

    public void setDiferenciaCert(double diferenciaCert) {
        this.diferenciaCert = diferenciaCert;
    }

    public double getDiferenciaDes() {
        return diferenciaDes;
    }

    public void setDiferenciaDes(double diferenciaDes) {
        this.diferenciaDes = diferenciaDes;
    }
}
