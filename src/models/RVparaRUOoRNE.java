package models;

public class RVparaRUOoRNE {

    public String code;
    public String descripcion;
    public String um;
    public double materiales;
    public double mano;
    public double equipos;
    public double cantDecla;
    public String tipoCosto;

    public RVparaRUOoRNE(String code, String descripcion, String um, double materiales, double mano, double equipos, double cantDecla, String tipoCosto) {
        this.code = code;
        this.descripcion = descripcion;
        this.um = um;
        this.materiales = materiales;
        this.mano = mano;
        this.equipos = equipos;
        this.cantDecla = cantDecla;
        this.tipoCosto = tipoCosto;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public double getMateriales() {
        return materiales;
    }

    public void setMateriales(double materiales) {
        this.materiales = materiales;
    }

    public double getMano() {
        return mano;
    }

    public void setMano(double mano) {
        this.mano = mano;
    }

    public double getEquipos() {
        return equipos;
    }

    public void setEquipos(double equipos) {
        this.equipos = equipos;
    }

    public double getCantDecla() {
        return cantDecla;
    }

    public void setCantDecla(double cantDecla) {
        this.cantDecla = cantDecla;
    }

    public String getTipoCosto() {
        return tipoCosto;
    }

    public void setTipoCosto(String tipoCosto) {
        this.tipoCosto = tipoCosto;
    }
}
