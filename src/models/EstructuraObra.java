package models;

public class EstructuraObra {

    public int idOb;
    public int idEmpresaC;
    public String desEmpresaC;
    public int idZon;
    public String descripZon;
    public int idObje;
    public String descripObje;

    public EstructuraObra(int idOb, int idEmpresaC, String desEmpresaC, int idZon, String descripZon, int idObje, String descripObje) {
        this.idOb = idOb;
        this.idEmpresaC = idEmpresaC;
        this.desEmpresaC = desEmpresaC;
        this.idZon = idZon;
        this.descripZon = descripZon;
        this.idObje = idObje;
        this.descripObje = descripObje;
    }

    public int getIdOb() {
        return idOb;
    }

    public void setIdOb(int idOb) {
        this.idOb = idOb;
    }


    public int getIdEmpresaC() {
        return idEmpresaC;
    }

    public void setIdEmpresaC(int idEmpresaC) {
        this.idEmpresaC = idEmpresaC;
    }

    public String getDesEmpresaC() {
        return desEmpresaC;
    }

    public void setDesEmpresaC(String desEmpresaC) {
        this.desEmpresaC = desEmpresaC;
    }

    public int getIdZon() {
        return idZon;
    }

    public void setIdZon(int idZon) {
        this.idZon = idZon;
    }

    public String getDescripZon() {
        return descripZon;
    }

    public void setDescripZon(String descripZon) {
        this.descripZon = descripZon;
    }

    public int getIdObje() {
        return idObje;
    }

    public void setIdObje(int idObje) {
        this.idObje = idObje;
    }

    public String getDescripObje() {
        return descripObje;
    }

    public void setDescripObje(String descripObje) {
        this.descripObje = descripObje;
    }
}
