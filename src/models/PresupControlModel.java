package models;

public class PresupControlModel {

    public int obraId;
    public int conceptoId;
    public Double valor;

    public PresupControlModel(int obraId, int conceptoId, Double valor) {
        this.obraId = obraId;
        this.conceptoId = conceptoId;
        this.valor = valor;
    }


    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoId) {
        this.conceptoId = conceptoId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
