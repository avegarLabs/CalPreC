package models;

public class DespachosConfirm {

    public int idSuministro;
    public String code;
    public String descrip;
    public String um;
    public double cantidad;

    public DespachosConfirm(int idSuministro, String code, String descrip, String um, double cantidad) {
        this.idSuministro = idSuministro;
        this.code = code;
        this.descrip = descrip;
        this.um = um;
        this.cantidad = cantidad;
    }

    public int getIdSuministro() {
        return idSuministro;
    }

    public void setIdSuministro(int idSuministro) {
        this.idSuministro = idSuministro;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
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
}
