package models;

public class ClPrintModel {

    public String code;
    public String descripcion;
    public String um;
    public double cant;
    public String obser;

    public ClPrintModel(String code, String descripcion, String um, double cant, String obser) {
        this.code = code;
        this.descripcion = descripcion;
        this.um = um;
        this.cant = cant;
        this.obser = obser;
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

    public double getCant() {
        return cant;
    }

    public void setCant(double cant) {
        this.cant = cant;
    }

    public String getObser() {
        return obser;
    }

    public void setObser(String obser) {
        this.obser = obser;
    }
}
