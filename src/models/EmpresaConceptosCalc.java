package models;

public class EmpresaConceptosCalc {

    public Integer idConcepto;
    public String concepto;
    public Double coeficiente;
    public Integer calcular;


    public EmpresaConceptosCalc(Integer idConcepto, String concepto, Double coeficiente, Integer calcular) {
        this.idConcepto = idConcepto;
        this.concepto = concepto;
        this.coeficiente = coeficiente;
        this.calcular = calcular;
    }

    public Integer getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Integer idConcepto) {
        this.idConcepto = idConcepto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(Double coeficiente) {
        this.coeficiente = coeficiente;
    }

    public Integer getCalcular() {
        return calcular;
    }

    public void setCalcular(Integer calcular) {
        this.calcular = calcular;
    }
}
