package AccessMigration.model;

public class CompoSemiModel {

    public String suministro;
    public String insumo;
    public String tipo;
    public double cantidad;
    public double usos;

    public CompoSemiModel(String suministro, String insumo, String tipo, double cantidad, double usos) {
        this.suministro = suministro;
        this.insumo = insumo;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.usos = usos;
    }

    public String getSuministro() {
        return suministro;
    }

    public void setSuministro(String suministro) {
        this.suministro = suministro;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
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

    public double getUsos() {
        return usos;
    }

    public void setUsos(double usos) {
        this.usos = usos;
    }
}
