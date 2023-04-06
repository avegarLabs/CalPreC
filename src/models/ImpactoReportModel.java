package models;

public class ImpactoReportModel {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String unidad;
    public String recurso;
    public String descripcion;
    public String um;
    public String grupo;
    public double cantidad;
    public double tar30;
    public double tar30cal;
    public double tar15;
    public double tar15cal;
    public double impacto;

    public ImpactoReportModel(String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String unidad, String recurso, String descripcion, String um, String grupo, double cantidad, double tar30, double tar30cal, double tar15, double tar15cal, double impacto) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.unidad = unidad;
        this.recurso = recurso;
        this.descripcion = descripcion;
        this.um = um;
        this.grupo = grupo;
        this.cantidad = cantidad;
        this.tar30 = tar30;
        this.tar30cal = tar30cal;
        this.tar15 = tar15;
        this.tar15cal = tar15cal;
        this.impacto = impacto;
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

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getTar30() {
        return tar30;
    }

    public void setTar30(double tar30) {
        this.tar30 = tar30;
    }

    public double getTar30cal() {
        return tar30cal;
    }

    public void setTar30cal(double tar30cal) {
        this.tar30cal = tar30cal;
    }

    public double getTar15() {
        return tar15;
    }

    public void setTar15(double tar15) {
        this.tar15 = tar15;
    }

    public double getTar15cal() {
        return tar15cal;
    }

    public void setTar15cal(double tar15cal) {
        this.tar15cal = tar15cal;
    }

    public double getImpacto() {
        return impacto;
    }

    public void setImpacto(double impacto) {
        this.impacto = impacto;
    }
}
