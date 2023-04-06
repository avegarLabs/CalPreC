package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresagastosPK.class)
public class Empresagastos {
    private int empresaconstructoraId;
    private int conceptosgastoId;
    private String formula;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Conceptosgasto conceptosgastoByConceptosgastoId;
    private Double coeficiente;
    private Integer calcular;
    private Double porciento;

    @Id
    @Column(name = "empresaconstructora__id", nullable = false)
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "conceptosgasto__id", nullable = false)
    public int getConceptosgastoId() {
        return conceptosgastoId;
    }

    public void setConceptosgastoId(int conceptosgastoId) {
        this.conceptosgastoId = conceptosgastoId;
    }

    @Basic
    @Column(name = "formula", nullable = true, length = 50)
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresagastos that = (Empresagastos) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                conceptosgastoId == that.conceptosgastoId &&
                Objects.equals(formula, that.formula);
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, conceptosgastoId, formula);
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora__id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "conceptosgasto__id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Conceptosgasto getConceptosgastoByConceptosgastoId() {
        return conceptosgastoByConceptosgastoId;
    }

    public void setConceptosgastoByConceptosgastoId(Conceptosgasto conceptosgastoByConceptosgastoId) {
        this.conceptosgastoByConceptosgastoId = conceptosgastoByConceptosgastoId;
    }

    @Basic
    @Column(name = "coeficiente")
    public Double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(Double coeficiente) {
        this.coeficiente = coeficiente;
    }

    @Basic
    @Column(name = "calcular")
    public Integer getCalcular() {
        return calcular;
    }

    public void setCalcular(Integer calcular) {
        this.calcular = calcular;
    }

    @Basic
    @Column(name = "porciento", nullable = true, precision = 0)
    public Double getPorciento() {
        return porciento;
    }

    public void setPorciento(Double porciento) {
        this.porciento = porciento;
    }
}
