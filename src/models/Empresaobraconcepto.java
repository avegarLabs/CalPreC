package models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(EmpresaobraconceptoPK.class)
public class Empresaobraconcepto {
    private int empresaconstructoraId;
    private int conceptosgastoId;
    private int obraId;
    private double valor;
    private double coeficiente;
    private Empresaconstructora empresaconstructoraByEmpresaconstructoraId;
    private Conceptosgasto conceptosgastoByConceptosgastoId;
    private Obra obraByObraId;
    private double salario;

    @Id
    @Column(name = "empresaconstructora_id")
    public int getEmpresaconstructoraId() {
        return empresaconstructoraId;
    }

    public void setEmpresaconstructoraId(int empresaconstructoraId) {
        this.empresaconstructoraId = empresaconstructoraId;
    }

    @Id
    @Column(name = "conceptosgasto_id")
    public int getConceptosgastoId() {
        return conceptosgastoId;
    }

    public void setConceptosgastoId(int conceptosgastoId) {
        this.conceptosgastoId = conceptosgastoId;
    }

    @Id
    @Column(name = "obra_id")
    public int getObraId() {
        return obraId;
    }

    public void setObraId(int obraId) {
        this.obraId = obraId;
    }

    @Basic
    @Column(name = "valor")
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresaobraconcepto that = (Empresaobraconcepto) o;
        return empresaconstructoraId == that.empresaconstructoraId &&
                conceptosgastoId == that.conceptosgastoId &&
                obraId == that.obraId &&
                Double.compare(that.valor, valor) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(empresaconstructoraId, conceptosgastoId, obraId, valor);
    }

    @ManyToOne
    @JoinColumn(name = "empresaconstructora_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Empresaconstructora getEmpresaconstructoraByEmpresaconstructoraId() {
        return empresaconstructoraByEmpresaconstructoraId;
    }

    public void setEmpresaconstructoraByEmpresaconstructoraId(Empresaconstructora empresaconstructoraByEmpresaconstructoraId) {
        this.empresaconstructoraByEmpresaconstructoraId = empresaconstructoraByEmpresaconstructoraId;
    }

    @ManyToOne
    @JoinColumn(name = "conceptosgasto_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Conceptosgasto getConceptosgastoByConceptosgastoId() {
        return conceptosgastoByConceptosgastoId;
    }

    public void setConceptosgastoByConceptosgastoId(Conceptosgasto conceptosgastoByConceptosgastoId) {
        this.conceptosgastoByConceptosgastoId = conceptosgastoByConceptosgastoId;
    }

    @ManyToOne
    @JoinColumn(name = "obra_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Obra getObraByObraId() {
        return obraByObraId;
    }

    public void setObraByObraId(Obra obraByObraId) {
        this.obraByObraId = obraByObraId;
    }

    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
