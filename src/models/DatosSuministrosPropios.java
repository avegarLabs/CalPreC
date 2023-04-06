package models;

import java.util.Objects;

public class DatosSuministrosPropios {

    public String codigo;
    public String descripcion;
    public String um;
    public String tipo;
    public double cantidad;
    public double precio;
    public double preciomlc;
    public double costoTotal;

    public DatosSuministrosPropios(String codigo, String descripcion, String um, String tipo, double cantidad, double precio, double preciomlc, double costoTotal) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.um = um;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.preciomlc = preciomlc;
        this.costoTotal = costoTotal;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosSuministrosPropios that = (DatosSuministrosPropios) o;
        return Objects.equals(codigo, that.codigo) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(um, that.um) &&
                Objects.equals(tipo, that.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, descripcion, um, tipo);
    }
}
