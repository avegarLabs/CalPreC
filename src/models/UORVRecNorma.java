package models;

public class UORVRecNorma {

    public int idUo;
    public int idRV;
    public int idRec;
    public double norma;

    public UORVRecNorma(int idUo, int idRV, int idRec, double norma) {
        this.idUo = idUo;
        this.idRV = idRV;
        this.idRec = idRec;
        this.norma = norma;
    }

    public int getIdUo() {
        return idUo;
    }

    public void setIdUo(int idUo) {
        this.idUo = idUo;
    }

    public int getIdRV() {
        return idRV;
    }

    public void setIdRV(int idRV) {
        this.idRV = idRV;
    }

    public int getIdRec() {
        return idRec;
    }

    public void setIdRec(int idRec) {
        this.idRec = idRec;
    }

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
    }
}
