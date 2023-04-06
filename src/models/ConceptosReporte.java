package models;

public class ConceptosReporte {

    public Integer idEmpresa;
    public String indice;
    public String concepto;
    public String formula;
    public double coeficiente;
    public double valor;

    public ConceptosReporte(Integer idEmpresa, String indice, String concepto, String formula, double coeficiente, double valor) {
        this.idEmpresa = idEmpresa;
        this.indice = indice;
        this.concepto = concepto;
        this.formula = formula;
        this.coeficiente = coeficiente;
        this.valor = valor;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(double coeficiente) {
        this.coeficiente = coeficiente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
