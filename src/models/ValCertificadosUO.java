package models;

public class ValCertificadosUO {

    public Double cant;
    public Double mat;
    public Double mano;
    public Double equip;
    public Double salario;

    public ValCertificadosUO(Double cant, Double mat, Double mano, Double equip, Double salario) {
        this.cant = cant;
        this.mat = mat;
        this.mano = mano;
        this.equip = equip;
        this.salario = salario;
    }

    public Double getCant() {
        return cant;
    }

    public void setCant(Double cant) {
        this.cant = cant;
    }

    public Double getMat() {
        return mat;
    }

    public void setMat(Double mat) {
        this.mat = mat;
    }

    public Double getMano() {
        return mano;
    }

    public void setMano(Double mano) {
        this.mano = mano;
    }

    public Double getEquip() {
        return equip;
    }

    public void setEquip(Double equip) {
        this.equip = equip;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}
