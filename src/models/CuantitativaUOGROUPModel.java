package models;

import java.util.ArrayList;

public class CuantitativaUOGROUPModel {
    public Integer idEmpresa;
    public String empresa;
    public String zona;
    public String objeto;
    public String nivel;
    public String especialidad;
    public String subespecialidad;
    public Integer idUO;
    public String codeUO;
    public String descripUO;
    public String umUO;
    public Double cantUO;
    public String codigo;
    public String descripcion;
    public String um;
    public String tipo;
    public double cantidad;
    public double precio;
    public double preciomlc;
    public double costoTotal;

    public ArrayList<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels;

    public CuantitativaUOGROUPModel(Integer idEmpresa, String empresa, String zona, String objeto, String nivel, String especialidad, String subespecialidad, Integer idUO, String codeUO, String descripUO, String umUO, Double cantUO, String codigo, String descripcion, String um, String tipo, double cantidad, double precio, double preciomlc, double costoTotal, ArrayList<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels) {
        this.idEmpresa = idEmpresa;
        this.empresa = empresa;
        this.zona = zona;
        this.objeto = objeto;
        this.nivel = nivel;
        this.especialidad = especialidad;
        this.subespecialidad = subespecialidad;
        this.idUO = idUO;
        this.codeUO = codeUO;
        this.descripUO = descripUO;
        this.umUO = umUO;
        this.cantUO = cantUO;
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

    public Integer getIdUO() {
        return idUO;
    }

    public void setIdUO(Integer idUO) {
        this.idUO = idUO;
    }

    public String getCodeUO() {
        return codeUO;
    }

    public void setCodeUO(String codeUO) {
        this.codeUO = codeUO;
    }

    public String getDescripUO() {
        return descripUO;
    }

    public void setDescripUO(String descripUO) {
        this.descripUO = descripUO;
    }

    public String getUmUO() {
        return umUO;
    }

    public void setUmUO(String umUO) {
        this.umUO = umUO;
    }

    public Double getCantUO() {
        return cantUO;
    }

    public void setCantUO(Double cantUO) {
        this.cantUO = cantUO;
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

    public ArrayList<FondoHorarioExplotacionModel> getDatosFondoHorarioExplotacionModels() {
        return datosFondoHorarioExplotacionModels;
    }

    public void setDatosFondoHorarioExplotacionModels(ArrayList<FondoHorarioExplotacionModel> datosFondoHorarioExplotacionModels) {
        this.datosFondoHorarioExplotacionModels = datosFondoHorarioExplotacionModels;
    }
}