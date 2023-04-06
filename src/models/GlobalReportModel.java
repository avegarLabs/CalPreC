package models;

public class GlobalReportModel {

    public String fecha;
    public String concepto;
    public double valores;

    public GlobalReportModel(String fecha, String concepto, double valores) {
        this.fecha = fecha;
        this.concepto = concepto;
        this.valores = valores;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getValores() {
        return valores;
    }

    public void setValores(double valores) {
        this.valores = valores;
    }
}
