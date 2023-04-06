package models;

public class EspecialidadesValorIte {

    public String espCode;
    public String descCode;
    public double valorTotal;

    public EspecialidadesValorIte(String espCode, String descCode, double valorTotal) {
        this.espCode = espCode;
        this.descCode = descCode;
        this.valorTotal = valorTotal;
    }

    public String getEspCode() {
        return espCode;
    }

    public void setEspCode(String espCode) {
        this.espCode = espCode;
    }

    public String getDescCode() {
        return descCode;
    }

    public void setDescCode(String descCode) {
        this.descCode = descCode;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
