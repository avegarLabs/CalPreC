package models;

public class SalarioUORVReportModel {

    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String codigo;
    public String descripcion;
    public String um;
    public double cantidad;
    public String codeRv;
    public String descriprv;
    public String umrv;
    public double cantidadrv;
    public double horas;
    public double salario;

    public SalarioUORVReportModel(String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String codigo, String descripcion, String um, double cantidad, String codeRv, String descriprv, String umrv, double cantidadrv, double horas, double salario) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.codeRv = codeRv;
        this.descriprv = descriprv;
        this.umrv = umrv;
        this.cantidadrv = cantidadrv;
        this.horas = horas;
        this.salario = salario;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodeRv() {
        return codeRv;
    }

    public void setCodeRv(String codeRv) {
        this.codeRv = codeRv;
    }

    public String getDescriprv() {
        return descriprv;
    }

    public void setDescriprv(String descriprv) {
        this.descriprv = descriprv;
    }

    public String getUmrv() {
        return umrv;
    }

    public void setUmrv(String umrv) {
        this.umrv = umrv;
    }

    public double getCantidadrv() {
        return cantidadrv;
    }

    public void setCantidadrv(double cantidadrv) {
        this.cantidadrv = cantidadrv;
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
