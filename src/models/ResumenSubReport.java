package models;

public class ResumenSubReport {

    public Integer idConcept;
    public String concept;
    public Double coef;
    public String formula;
    public String calcular;
    public Double porciento;

    public ResumenSubReport(Integer idConcept, String concept, Double coef, String formula, String calcular, Double porciento) {
        this.idConcept = idConcept;
        this.concept = concept;
        this.coef = coef;
        this.formula = formula;
        this.calcular = calcular;
        this.porciento = porciento;
    }

    public Integer getIdConcept() {
        return idConcept;
    }

    public void setIdConcept(Integer idConcept) {
        this.idConcept = idConcept;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public Double getCoef() {
        return coef;
    }

    public void setCoef(Double coef) {
        this.coef = coef;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getCalcular() {
        return calcular;
    }

    public void setCalcular(String calcular) {
        this.calcular = calcular;
    }

    public Double getPorciento() {
        return porciento;
    }

    public void setPorciento(Double porciento) {
        this.porciento = porciento;
    }
}
