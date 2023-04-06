package AccessMigration.model;

public class CalPrecIdComponentes {

    public int empId;
    public int zonaId;
    public int objetoId;
    public int nivId;
    public int espId;
    public int subId;


    public CalPrecIdComponentes(int empId, int zonaId, int objetoId, int nivId, int espId, int subId) {
        this.empId = empId;
        this.zonaId = zonaId;
        this.objetoId = objetoId;
        this.nivId = nivId;
        this.espId = espId;
        this.subId = subId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getZonaId() {
        return zonaId;
    }

    public void setZonaId(int zonaId) {
        this.zonaId = zonaId;
    }

    public int getObjetoId() {
        return objetoId;
    }

    public void setObjetoId(int objetoId) {
        this.objetoId = objetoId;
    }

    public int getNivId() {
        return nivId;
    }

    public void setNivId(int nivId) {
        this.nivId = nivId;
    }

    public int getEspId() {
        return espId;
    }

    public void setEspId(int espId) {
        this.espId = espId;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }
}
