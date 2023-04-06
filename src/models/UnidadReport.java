package models;

public class UnidadReport {

    public String codigRV;
    public String descripRV;
    public String umRV;
    public Double cantrv;
    public Double costUnitario;
    public Double materialrv;
    public Double manorv;
    public Double equiporv;
    public Double salariorv;


    public UnidadReport(String codigRV, String descripRV, String umRV, Double cantrv, Double costUnitario, Double materialrv, Double manorv, Double equiporv, Double salariorv) {
        this.codigRV = codigRV;
        this.descripRV = descripRV;
        this.umRV = umRV;
        this.cantrv = cantrv;
        this.costUnitario = costUnitario;
        this.materialrv = materialrv;
        this.manorv = manorv;
        this.equiporv = equiporv;
        this.salariorv = salariorv;
    }


    public String getCodigRV() {
        return codigRV;
    }

    public void setCodigRV(String codigRV) {
        this.codigRV = codigRV;
    }

    public String getDescripRV() {
        return descripRV;
    }

    public void setDescripRV(String descripRV) {
        this.descripRV = descripRV;
    }

    public String getUmRV() {
        return umRV;
    }

    public void setUmRV(String umRV) {
        this.umRV = umRV;
    }

    public Double getCantrv() {
        return cantrv;
    }

    public void setCantrv(Double cantrv) {
        this.cantrv = cantrv;
    }

    public Double getCostUnitario() {
        return costUnitario;
    }

    public void setCostUnitario(Double costUnitario) {
        this.costUnitario = costUnitario;
    }

    public Double getMaterialrv() {
        return materialrv;
    }

    public void setMaterialrv(Double materialrv) {
        this.materialrv = materialrv;
    }

    public Double getManorv() {
        return manorv;
    }

    public void setManorv(Double manorv) {
        this.manorv = manorv;
    }

    public Double getEquiporv() {
        return equiporv;
    }

    public void setEquiporv(Double equiporv) {
        this.equiporv = equiporv;
    }

    public Double getSalariorv() {
        return salariorv;
    }

    public void setSalariorv(Double salariorv) {
        this.salariorv = salariorv;
    }
}
