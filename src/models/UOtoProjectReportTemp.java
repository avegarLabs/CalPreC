package models;

import java.util.ArrayList;

public class UOtoProjectReportTemp {

    public int id;
    public String codigo;
    public String descripcion;
    public String um;
    public Double cantidad;
    public double horas;
    public double costMaterial;
    public double costMano;
    public double costEquipo;
    public double costTotal;
    public double avance;
    public double costoEjecutado;
    public double porCientEjec;

    public ArrayList<String> resourcesArrayList;


    public UOtoProjectReportTemp(int id, String codigo, String descripcion, String um, Double cantidad, double horas, double costMaterial, double costMano, double costEquipo, double costTotal, double avance, double costoEjecutado, double porCientEjec, ArrayList<String> resourcesArrayList) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.horas = horas;
        this.costMaterial = costMaterial;
        this.costMano = costMano;
        this.costEquipo = costEquipo;
        this.costTotal = costTotal;
        this.avance = avance;
        this.costoEjecutado = costoEjecutado;
        this.porCientEjec = porCientEjec;
        this.resourcesArrayList = resourcesArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }

    public double getCostMaterial() {
        return costMaterial;
    }

    public void setCostMaterial(double costMaterial) {
        this.costMaterial = costMaterial;
    }

    public double getCostMano() {
        return costMano;
    }

    public void setCostMano(double costMano) {
        this.costMano = costMano;
    }

    public double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(double costEquipo) {
        this.costEquipo = costEquipo;
    }

    public double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(double costTotal) {
        this.costTotal = costTotal;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public double getCostoEjecutado() {
        return costoEjecutado;
    }

    public void setCostoEjecutado(double costoEjecutado) {
        this.costoEjecutado = costoEjecutado;
    }

    public double getPorCientEjec() {
        return porCientEjec;
    }

    public void setPorCientEjec(double porCientEjec) {
        this.porCientEjec = porCientEjec;
    }

    public ArrayList<String> getResourcesArrayList() {
        return resourcesArrayList;
    }

    public void setResourcesArrayList(ArrayList<String> resourcesArrayList) {
        this.resourcesArrayList = resourcesArrayList;
    }
}