package AccessMigration.model;

public class rvComposition {

    public String insumo;
    public String tip;
    public double norma;
    public int uso;

    public rvComposition(String insumo, String tip, double norma, int uso) {
        this.insumo = insumo;
        this.tip = tip;
        this.norma = norma;
        this.uso = uso;
    }

    public String getInsumo() {
        return insumo;
    }

    public void setInsumo(String insumo) {
        this.insumo = insumo;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
    }

    public int getUso() {
        return uso;
    }

    public void setUso(int uso) {
        this.uso = uso;
    }
}
