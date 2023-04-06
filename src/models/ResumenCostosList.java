package models;

public class ResumenCostosList {
    public String indice;
    public String conceptos;
    public String valor;

    public ResumenCostosList(String indice, String conceptos, String valor) {
        this.indice = indice;
        this.conceptos = conceptos;
        this.valor = valor;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getConceptos() {
        return conceptos;
    }

    public void setConceptos(String conceptos) {
        this.conceptos = conceptos;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
