package models;

public class DatosReporteUORV {

    public String codigo;
    public String descripcion;
    public String um;
    public Double cantidad;
    public Double costMateriales;
    public Double costMano;
    public Double costEquip;
    public Double costTotal;

    public DatosReporteUORV(String codigo, String descripcion, String um, Double cantidad, Double costMateriales, Double costMano, Double costEquip, Double costTotal) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.cantidad = cantidad;
        this.costMateriales = costMateriales;
        this.costMano = costMano;
        this.costEquip = costEquip;
        this.costTotal = costTotal;
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

    public Double getCostMateriales() {
        return costMateriales;
    }

    public void setCostMateriales(Double costMateriales) {
        this.costMateriales = costMateriales;
    }

    public Double getCostMano() {
        return costMano;
    }

    public void setCostMano(Double costMano) {
        this.costMano = costMano;
    }

    public Double getCostEquip() {
        return costEquip;
    }

    public void setCostEquip(Double costEquip) {
        this.costEquip = costEquip;
    }

    public Double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Double costTotal) {
        this.costTotal = costTotal;
    }

}
