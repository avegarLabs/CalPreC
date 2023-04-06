package models;

import java.util.List;

public class DatosCuantitativaGenreralForReportModel {

    public Integer empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public String codigo;
    public String descripcion;
    public String um;
    public String tipo;
    public double cantidad;
    public double precio;
    public double preciomlc;
    public double costoTotal;

    public List<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels;

    public DatosCuantitativaGenreralForReportModel(Integer empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, String codigo, String descripcion, String um, String tipo, double cantidad, double precio, double preciomlc, double costoTotal, List<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels) {
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.preciomlc = preciomlc;
        this.costoTotal = costoTotal;
        this.datosFondoHorarioExplotacionModels = datosFondoHorarioExplotacionModels;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPreciomlc() {
        return preciomlc;
    }

    public void setPreciomlc(double preciomlc) {
        this.preciomlc = preciomlc;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public List<FondoHorarioExplotacionModel> getDatosFondoHorarioExplotacionModels() {
        return datosFondoHorarioExplotacionModels;
    }

    public void setDatosFondoHorarioExplotacionModels(List<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels) {
        this.datosFondoHorarioExplotacionModels = datosFondoHorarioExplotacionModels;
    }
}