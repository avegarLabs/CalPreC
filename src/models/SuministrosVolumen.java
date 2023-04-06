package models;

public class SuministrosVolumen {
    public int idRec;
    public Double cant;

    public SuministrosVolumen(int idRec, Double cant) {
        this.idRec = idRec;
        this.cant = cant;
    }

    public int getIdRec() {
        return idRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }
}
