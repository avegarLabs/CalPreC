package models;

public class ObraEmpresaNiveleEspecificosCostos {

    public int idObra;
    public int idEmpresa;
    public double costoMateriales;
    public double costoMano;
    public double costEquipo;

    public ObraEmpresaNiveleEspecificosCostos(int idObra, int idEmpresa, double costoMateriales, double costoMano, double costEquipo) {
        this.idObra = idObra;
        this.idEmpresa = idEmpresa;
        this.costoMateriales = costoMateriales;
        this.costoMano = costoMano;
        this.costEquipo = costEquipo;
    }

    public int getIdObra() {
        return idObra;
    }

    public void setIdObra(int idObra) {
        this.idObra = idObra;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public double getCostoMateriales() {
        return costoMateriales;
    }

    public void setCostoMateriales(double costoMateriales) {
        this.costoMateriales = costoMateriales;
    }

    public double getCostoMano() {
        return costoMano;
    }

    public void setCostoMano(double costoMano) {
        this.costoMano = costoMano;
    }

    public double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(double costEquipo) {
        this.costEquipo = costEquipo;
    }
}
