package models;

public class RCreportModel {
    public String tipo;
    public String code;
    public String description;
    public String um;
    public double cantidad;
    public double precio;
    public double costo;

    public RCreportModel(String tipo, String code, String description, String um, double cantidad, double precio, double costo) {
        this.tipo = tipo;
        this.code = code;
        this.description = description;
        this.um = um;
        this.cantidad = cantidad;
        this.precio = precio;
        this.costo = costo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}

