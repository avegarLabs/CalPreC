package models;

public class DatosRenVariantesToRecalc {

    private double costMaterial;
    private double costMano;
    private double costEquipo;

    public DatosRenVariantesToRecalc(double costMaterial, double costMano, double costEquipo) {
        this.costMaterial = costMaterial;
        this.costMano = costMano;
        this.costEquipo = costEquipo;
    }

    public double getCostMaterial() {
        return costMaterial;
    }

    public void setCostMaterial(double costMaterial) {
        this.costMaterial = costMaterial;
    }

    public double getCostMano() {
        return costMano;
    }

    public void setCostMano(double costMano) {
        this.costMano = costMano;
    }

    public double getCostEquipo() {
        return costEquipo;
    }

    public void setCostEquipo(double costEquipo) {
        this.costEquipo = costEquipo;
    }
}
