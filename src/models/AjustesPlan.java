package models;

public class AjustesPlan {

    public Integer idUnidad;
    public double materialesUO;
    public double manoUO;
    public double equiposUO;
    public double salarioUO;
    public double salariocucUO;
    public double cantidad;
    public double cantidadplan;
    public double materiales;
    public double manoObra;
    public double equipo;
    public double salario;
    public double salariocuc;
    public Integer brigadaId;
    public Integer grupoId;
    public Integer cuadrillaId;
    public double cantCertf;
    public double pendientes;

    public AjustesPlan(Integer idUnidad, double materialesUO, double manoUO, double equiposUO, double salarioUO, double salariocucUO, double cantidad, double cantidadplan, double materiales, double manoObra, double equipo, double salario, double salariocuc, Integer brigadaId, Integer grupoId, Integer cuadrillaId, double cantCertf, double pendientes) {
        this.idUnidad = idUnidad;
        this.materialesUO = materialesUO;
        this.manoUO = manoUO;
        this.equiposUO = equiposUO;
        this.salarioUO = salarioUO;
        this.salariocucUO = salariocucUO;
        this.cantidad = cantidad;
        this.cantidadplan = cantidadplan;
        this.materiales = materiales;
        this.manoObra = manoObra;
        this.equipo = equipo;
        this.salario = salario;
        this.salariocuc = salariocuc;
        this.brigadaId = brigadaId;
        this.grupoId = grupoId;
        this.cuadrillaId = cuadrillaId;
        this.cantCertf = cantCertf;
        this.pendientes = pendientes;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public double getMaterialesUO() {
        return materialesUO;
    }

    public void setMaterialesUO(double materialesUO) {
        this.materialesUO = materialesUO;
    }

    public double getManoUO() {
        return manoUO;
    }

    public void setManoUO(double manoUO) {
        this.manoUO = manoUO;
    }

    public double getEquiposUO() {
        return equiposUO;
    }

    public void setEquiposUO(double equiposUO) {
        this.equiposUO = equiposUO;
    }

    public double getSalarioUO() {
        return salarioUO;
    }

    public void setSalarioUO(double salarioUO) {
        this.salarioUO = salarioUO;
    }

    public double getSalariocucUO() {
        return salariocucUO;
    }

    public void setSalariocucUO(double salariocucUO) {
        this.salariocucUO = salariocucUO;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCantidadplan() {
        return cantidadplan;
    }

    public void setCantidadplan(double cantidadplan) {
        this.cantidadplan = cantidadplan;
    }

    public double getMateriales() {
        return materiales;
    }

    public void setMateriales(double materiales) {
        this.materiales = materiales;
    }

    public double getManoObra() {
        return manoObra;
    }

    public void setManoObra(double manoObra) {
        this.manoObra = manoObra;
    }

    public double getEquipo() {
        return equipo;
    }

    public void setEquipo(double equipo) {
        this.equipo = equipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getSalariocuc() {
        return salariocuc;
    }

    public void setSalariocuc(double salariocuc) {
        this.salariocuc = salariocuc;
    }

    public Integer getBrigadaId() {
        return brigadaId;
    }

    public void setBrigadaId(Integer brigadaId) {
        this.brigadaId = brigadaId;
    }

    public Integer getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Integer grupoId) {
        this.grupoId = grupoId;
    }

    public Integer getCuadrillaId() {
        return cuadrillaId;
    }

    public void setCuadrillaId(Integer cuadrillaId) {
        this.cuadrillaId = cuadrillaId;
    }

    public double getCantCertf() {
        return cantCertf;
    }

    public void setCantCertf(double cantCertf) {
        this.cantCertf = cantCertf;
    }

    public double getPendientes() {
        return pendientes;
    }

    public void setPendientes(double pendientes) {
        this.pendientes = pendientes;
    }
}

