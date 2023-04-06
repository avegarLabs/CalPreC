package models;

public class RecursosVolumen {
    public int idRec;
    public Double cantidad;

    public RecursosVolumen(int idRec, Double cantidad) {
        this.idRec = idRec;
        this.cantidad = cantidad;
    }

    public int getIdRec() {
        return idRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
}
